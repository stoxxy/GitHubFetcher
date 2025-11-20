package com.example.githubfetcher.domain

import com.example.githubfetcher.data.model.RepositoryEntity

interface GitHubRepoFetcher {
    suspend fun fetchRepos(username: String): List<RepositoryEntity>
}