package com.example.githubfetcher.domain

import com.example.githubfetcher.domain.model.Repository

interface GitHubFetcherRepository {
    suspend fun fetchRepos(username: String): List<Repository>
    suspend fun fetchRecentFromTheDatabase(): List<Repository>
}