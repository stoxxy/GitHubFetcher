package com.example.githubfetcher.domain.model

data class Repository(
    val id: Long,
    val name: String,
    val description: String?
)