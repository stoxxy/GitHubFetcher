package com.example.githubfetcher.presentation.model

import android.os.Parcelable
import com.example.githubfetcher.domain.model.Commit
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommitUiModel(
    val commit: CommitContentUiModel
): Parcelable

@Parcelize
data class CommitContentUiModel(
    val url: String,
    val author: CommitAuthorUiModel,
    val message: String
): Parcelable

@Parcelize
data class CommitAuthorUiModel(
    val name: String
): Parcelable

fun Commit.toUi() = CommitUiModel(
    commit = CommitContentUiModel(
        url = this.commit.url,
        author = with(this.commit.author) {
            CommitAuthorUiModel(
                name = name,
            )
        },
        message = this.commit.message
    )
)

fun List<Commit>.toUi() = map { it.toUi() }