package com.example.githubfetcher.presentation

sealed interface GitHubFetcherIntent {
    data class SearchInput(val username: String): GitHubFetcherIntent
    data class SelectRepo(val repoName: String): GitHubFetcherIntent
    data object CloseCommitsScreen: GitHubFetcherIntent
}