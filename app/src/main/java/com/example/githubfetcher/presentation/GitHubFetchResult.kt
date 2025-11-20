package com.example.githubfetcher.presentation

import android.os.Parcelable
import com.example.githubfetcher.presentation.model.RepositoryUiModel
import kotlinx.parcelize.Parcelize

sealed class GitHubFetchResult: Parcelable {
    @Parcelize
    data object None: GitHubFetchResult()
    @Parcelize data object InProgress: GitHubFetchResult()
    @Parcelize data class Success(val repos: List<RepositoryUiModel>, val cached: Boolean = false): GitHubFetchResult()
    @Parcelize data object Error: GitHubFetchResult()
}