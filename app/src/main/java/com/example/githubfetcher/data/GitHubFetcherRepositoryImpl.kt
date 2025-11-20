package com.example.githubfetcher.data

import com.example.githubfetcher.data.model.RepositoryEntity
import com.example.githubfetcher.data.model.toDomain
import com.example.githubfetcher.domain.GitHubFetcherRepository
import com.example.githubfetcher.domain.GitHubRepoFetcher
import com.example.githubfetcher.domain.model.Repository
import com.example.githubfetcher.util.RemoteGitHubFetchException

class GitHubFetcherRepositoryImpl(
    private val gitHubRepoFetcher: GitHubRepoFetcher,
    private val gitHubFetcherDatabase: GitHubFetcherDatabase
): GitHubFetcherRepository {
    override suspend fun fetchRepos(username: String): List<Repository> {
        var reposEntities: List<RepositoryEntity>
        try {
            reposEntities = gitHubRepoFetcher.fetchRepos(username).sortedBy { it.id }
        } catch (_: Exception) {
            throw RemoteGitHubFetchException()
        }
        gitHubFetcherDatabase.getDao().clearAndInsert(reposEntities)
        return reposEntities.toDomain()
    }

    override suspend fun fetchRecentFromTheDatabase() = gitHubFetcherDatabase.getDao().getAll().toDomain()
}