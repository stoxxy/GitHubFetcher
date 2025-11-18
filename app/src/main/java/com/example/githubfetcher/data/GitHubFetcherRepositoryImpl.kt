package com.example.githubfetcher.data

import com.example.githubfetcher.domain.GitHubFetcherRepository
import com.example.githubfetcher.domain.GitHubRepoFetcher

class GitHubFetcherRepositoryImpl(
    private val gitHubRepoFetcher: GitHubRepoFetcher
): GitHubFetcherRepository {
    override suspend fun fetchRepos(username: String): List<Repository> {
        return gitHubRepoFetcher.fetchRepos(username)
    }
}