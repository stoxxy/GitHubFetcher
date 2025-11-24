package com.example.githubfetcher.presentation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface GitHubFetchResult: Parcelable {
    @Parcelize
    data object None: GitHubFetchResult
    @Parcelize data object InProgress: GitHubFetchResult
    @Parcelize data class Success(val content: List<Parcelable>, val cached: Boolean = false): GitHubFetchResult
    @Parcelize
    data object NoContent: GitHubFetchResult

    @Parcelize data object Error: GitHubFetchResult
}