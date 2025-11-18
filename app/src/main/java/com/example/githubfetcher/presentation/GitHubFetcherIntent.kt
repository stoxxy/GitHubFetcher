package com.example.githubfetcher.presentation

sealed class GitHubFetcherIntent {
    data class SearchInput(val username: String): GitHubFetcherIntent()
}