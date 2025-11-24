package com.example.githubfetcher.domain

import com.example.githubfetcher.data.model.RepositoryEntity
import com.example.githubfetcher.domain.model.Commit

interface GitHubRepoFetcher {
    suspend fun fetchRepos(username: String): List<RepositoryEntity>
    suspend fun fetchCommits(username: String, repo: String): List<Commit>
}