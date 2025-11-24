package com.example.githubfetcher.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.githubfetcher.presentation.screens.CommitListScreen
import com.example.githubfetcher.presentation.screens.RepoSearchScreen

@Composable
fun GitHubFetcherScreen(
    modifier: Modifier,
    viewModel: GitHubFetcherViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    GitHubFetcherScreenContent(
        modifier = modifier,
        uiState = uiState,
        onIntent = viewModel::handleIntent
    )
}

@Composable
private fun GitHubFetcherScreenContent(
    modifier: Modifier,
    uiState: UiState,
    onIntent: (GitHubFetcherIntent) -> Unit
) {
    BackHandler(enabled = uiState.commitFetchResult !is GitHubFetchResult.None) {
        onIntent(GitHubFetcherIntent.CloseCommitsScreen)
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(
            targetState = uiState.selectedRepoName,
            transitionSpec = {
                if (targetState.isNotEmpty()) {
                    slideInHorizontally { w -> w } togetherWith slideOutHorizontally { w -> -w }
                } else {
                    slideInHorizontally { w -> -w } togetherWith slideOutHorizontally { w -> w }
                }
            }
        ) { selectedRepoName ->
            if (selectedRepoName.isNotEmpty()){
                CommitListScreen(
                    name = uiState.selectedRepoName,
                    fetchResult = uiState.commitFetchResult,
                    onBack = { onIntent(GitHubFetcherIntent.CloseCommitsScreen) }
                )
            } else {
                RepoSearchScreen(
                    username = uiState.username,
                    fetchResult = uiState.reposFetchResult,
                    onIntent = onIntent
                )
            }
        }
    }
}