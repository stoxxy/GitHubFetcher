package com.example.githubfetcher.domain

import com.example.githubfetcher.data.Repository

interface GitHubRepoFetcher {
    suspend fun fetchRepos(username: String): List<Repository>
}