package com.example.githubfetcher.data

import com.example.githubfetcher.domain.GitHubFetcherRepository
import com.example.githubfetcher.domain.GitHubRepoFetcher
import com.example.githubfetcher.util.RemoteGitHubFetchException

class GitHubFetcherRepositoryImpl(
    private val gitHubRepoFetcher: GitHubRepoFetcher,
    private val gitHubFetcherDatabase: GitHubFetcherDatabase
): GitHubFetcherRepository {
    override suspend fun fetchRepos(username: String): List<Repository> {
        var repos: List<Repository>
        try {
            repos = gitHubRepoFetcher.fetchRepos(username).sortedByDescending { it.id }
        } catch (_: Exception) {
            throw RemoteGitHubFetchException()
        }
        gitHubFetcherDatabase.getDao().clearAndInsert(repos)
        return repos
    }

    override suspend fun fetchRecentFromTheDatabase() = gitHubFetcherDatabase.getDao().getAll()
}