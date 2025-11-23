package com.example.githubfetcher.data

import com.example.githubfetcher.data.model.RepositoryEntity

class GitHubFetcherDataSource(
    database: GitHubFetcherDatabase
) {
    private val dao = database.getDao()

    suspend fun saveRepos(repos: List<RepositoryEntity>) {
        dao.clearAndInsert(repos)
    }

    suspend fun getRepos(): List<RepositoryEntity> {
        return dao.getAll()
    }
}