package com.example.githubfetcher.presentation

import android.os.Parcelable
import com.example.githubfetcher.data.Repository
import kotlinx.parcelize.Parcelize

sealed class GitHubFetchResult: Parcelable {
    @Parcelize
    data object None: GitHubFetchResult()
    @Parcelize data object InProgress: GitHubFetchResult()
    @Parcelize data class Success(val repos: List<Repository>): GitHubFetchResult()
    @Parcelize data class Error(val message: String): GitHubFetchResult()
}