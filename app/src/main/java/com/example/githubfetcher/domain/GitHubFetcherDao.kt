package com.example.githubfetcher.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.githubfetcher.data.model.RepositoryEntity
import com.example.githubfetcher.domain.model.Repository

@Dao
interface GitHubFetcherDao {
    @Query("SELECT * FROM repository ORDER BY id")
    suspend fun getAll(): List<RepositoryEntity>

    @Insert
    suspend fun insertAll(repositories: List<RepositoryEntity>)

    @Query("DELETE FROM repository")
    suspend fun deleteAll()

    @Transaction
    suspend fun clearAndInsert(repositories: List<RepositoryEntity>) {
        deleteAll()
        insertAll(repositories.take(50))
    }
}