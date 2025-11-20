package com.example.githubfetcher.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.githubfetcher.domain.GitHubFetcherDao

@Database(entities = [Repository::class], version = 1)
abstract class GitHubFetcherDatabase: RoomDatabase() {
    abstract fun getDao(): GitHubFetcherDao
}