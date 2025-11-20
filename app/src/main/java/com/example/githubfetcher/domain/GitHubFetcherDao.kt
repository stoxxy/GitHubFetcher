package com.example.githubfetcher.domain

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.githubfetcher.data.Repository

@Dao
interface GitHubFetcherDao {
    @Query("SELECT * FROM repository ORDER BY id")
    suspend fun getAll(): List<Repository>

    @Insert
    suspend fun insertAll(repositories: List<Repository>)

    @Query("DELETE FROM repository")
    suspend fun deleteAll()

    @Transaction
    suspend fun clearAndInsert(repositories: List<Repository>) {
        deleteAll()
        insertAll(repositories.take(50))
    }
}