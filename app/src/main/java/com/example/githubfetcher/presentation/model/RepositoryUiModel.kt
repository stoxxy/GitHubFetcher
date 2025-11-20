package com.example.githubfetcher.presentation.model

import android.os.Parcelable
import com.example.githubfetcher.domain.model.Repository
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepositoryUiModel(
    val id: Long,
    val name: String,
    val description: String?
): Parcelable

fun Repository.toUi() = RepositoryUiModel(
    id = this.id,
    name = this.name,
    description = this.description
)

fun List<Repository>.toUi() = map { it.toUi() }