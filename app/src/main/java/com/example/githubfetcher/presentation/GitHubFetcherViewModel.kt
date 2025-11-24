package com.example.githubfetcher.presentation

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubfetcher.domain.GitHubFetcherRepository
import com.example.githubfetcher.presentation.model.toUi
import com.example.githubfetcher.util.RemoteGitHubFetchException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class GitHubFetcherViewModel @Inject constructor(
    private val repository: GitHubFetcherRepository,
    dispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    val uiState = savedStateHandle.getStateFlow(UI_STATE_KEY, UiState())

    init {
        uiState.map { it.username }
            .distinctUntilChanged()
            .debounce(SEARCH_DELAY)
            .filter { it.isNotBlank() }
            .flatMapLatest { search(it) }
            .onEach { result -> updateState { it.copy(reposFetchResult = result) } }
            .flowOn(dispatcher)
            .launchIn(viewModelScope)

    }

    fun handleIntent(intent: GitHubFetcherIntent) {
        when (intent) {
            is GitHubFetcherIntent.SearchInput -> {
                updateState { it.copy(username = intent.username) }
            }
            is GitHubFetcherIntent.SelectRepo -> {
                updateState { it.copy(selectedRepoName = intent.repoName) }
                fetchCommits()
            }
            is GitHubFetcherIntent.CloseCommitsScreen -> {
                updateState { it.copy(selectedRepoName = "", commitFetchResult = GitHubFetchResult.None) }
            }
        }
    }

    private fun updateState(update: (UiState) -> UiState) {
        val current = savedStateHandle.get<UiState>(UI_STATE_KEY) ?: UiState()
        savedStateHandle[UI_STATE_KEY] = update(current)
    }

    private fun search(username: String) = flow {
        emit(GitHubFetchResult.InProgress)
        try {
            val repos = repository.fetchRepos(username).toUi()
            emit(GitHubFetchResult.Success(repos))
        } catch (e: Exception) {
            if (e.message?.contains("Not Found") == true) emit(GitHubFetchResult.NoContent).also { return@flow }
            if (e is RemoteGitHubFetchException) {
                // If remote fetching fails, try to fetch recent repositories from local database.
                try {
                    val cachedRepos = repository.fetchRecentFromTheDatabase().toUi()
                    emit(if (cachedRepos.isNotEmpty()) GitHubFetchResult.Success(cachedRepos, true)
                    else GitHubFetchResult.Error)
                } catch (e: Exception) {
                    Log.d(CLASS_NAME, "Fallback error: ${e.message ?: e.toString()}")
                    emit(GitHubFetchResult.Error)
                }
            } else {
                Log.d(CLASS_NAME, "Unknown error: ${e.message ?: e.toString()}")
                emit(GitHubFetchResult.Error)
            }
        }
    }

    private fun fetchCommits() = flow {
        emit(GitHubFetchResult.InProgress)
        try {
            val commits = repository.fetchCommits(uiState.value.username, uiState.value.selectedRepoName)
            emit(GitHubFetchResult.Success(commits.toUi()))
        } catch (e: Exception) {
            if (e.message?.contains("Git Repository is empty.") == true) emit(GitHubFetchResult.NoContent).also { return@flow }
            emit(GitHubFetchResult.Error)
        }
    }.onEach { result -> updateState { it.copy(commitFetchResult = result) } }
        .launchIn(viewModelScope)

    private companion object {
        const val SEARCH_DELAY = 600L
        const val UI_STATE_KEY = "ui_state"
        const val CLASS_NAME = "GitHubFetcherViewModel"
    }
}

@Parcelize
data class UiState(
    val username: String = "",
    val selectedRepoName: String = "",
    val reposFetchResult: GitHubFetchResult = GitHubFetchResult.None,
    val commitFetchResult: GitHubFetchResult = GitHubFetchResult.None
): Parcelable