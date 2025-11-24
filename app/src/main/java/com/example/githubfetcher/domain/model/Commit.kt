package com.example.githubfetcher.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Commit(
    val commit: CommitContent
)

@Serializable
data class CommitContent(
    val url: String,
    val author: CommitAuthor,
    val message: String
)

@Serializable
data class CommitAuthor(
    val name: String,
)

