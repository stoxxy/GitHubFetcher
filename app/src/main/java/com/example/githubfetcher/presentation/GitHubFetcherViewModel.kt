package com.example.githubfetcher.presentation

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubfetcher.data.Repository
import com.example.githubfetcher.domain.GitHubFetcherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
            .debounce(600L)
            .filter { it.isNotBlank() }
            .flatMapLatest { search(it) }
            .onEach { result -> updateState { it.copy(fetchResult = result) } }
            .flowOn(dispatcher)
            .launchIn(viewModelScope)

    }

    fun handleIntent(intent: GitHubFetcherIntent) {
        when (intent) {
            is GitHubFetcherIntent.SearchInput -> {
                updateState { it.copy(username = intent.username) }
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
            val repos = repository.fetchRepos(username)
            emit(GitHubFetchResult.Success(repos))
        } catch (e: Exception) {
            emit(if (e.message?.contains("Not Found") == true) GitHubFetchResult.Success(listOf())
                    else GitHubFetchResult.Error(e.message ?: e.toString()))
        }
    }

    private companion object {
        const val UI_STATE_KEY = "ui_state"
    }
}

@Parcelize
data class UiState(
    val username: String = "",
    val fetchResult: GitHubFetchResult = GitHubFetchResult.None
): Parcelable