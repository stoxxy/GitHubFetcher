package com.example.githubfetcher.data

import com.example.githubfetcher.data.model.RepositoryEntity
import com.example.githubfetcher.data.model.toDomain
import com.example.githubfetcher.domain.GitHubFetcherRepository
import com.example.githubfetcher.domain.GitHubRepoFetcher
import com.example.githubfetcher.domain.model.Commit
import com.example.githubfetcher.domain.model.Repository
import com.example.githubfetcher.util.RemoteGitHubFetchException

class GitHubFetcherRepositoryImpl(
    private val gitHubRepoFetcher: GitHubRepoFetcher,
    private val gitHubFetcherDataSource: GitHubFetcherDataSource
): GitHubFetcherRepository {
    override suspend fun fetchRepos(username: String): List<Repository> {
        var reposEntities: List<RepositoryEntity>
        try {
            reposEntities = gitHubRepoFetcher.fetchRepos(username).sortedBy { it.id }
        } catch (e: Exception) {
            throw RemoteGitHubFetchException(e.message ?: "")
        }
        gitHubFetcherDataSource.saveRepos(reposEntities)
        return reposEntities.toDomain()
    }

    override suspend fun fetchCommits(
        username: String,
        repoName: String
    ): List<Commit> {
        return gitHubRepoFetcher.fetchCommits(username, repoName)
    }

    override suspend fun fetchRecentFromTheDatabase() = gitHubFetcherDataSource.getRepos().toDomain()
}