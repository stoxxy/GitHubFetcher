package com.example.githubfetcher

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.githubfetcher.data.GitHubFetcherDataSource
import com.example.githubfetcher.data.GitHubFetcherDatabase
import com.example.githubfetcher.data.model.RepositoryEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GitHubFetcherDataSourceTest {
    private lateinit var dataSource: GitHubFetcherDataSource
    private lateinit var database: GitHubFetcherDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GitHubFetcherDatabase::class.java
        ).build()

        dataSource = GitHubFetcherDataSource(database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insert_oldRecordsRemoved() = runTest {
        val repos = List(10) { RepositoryEntity(id = it.toLong(), USERNAME, "") }
        dataSource.saveRepos(repos)
        assertEquals(dataSource.getRepos(), repos)

        val repos2 = repos.map { it.copy(name = it.name + "_1") }
        dataSource.saveRepos(repos2)
        assertEquals(dataSource.getRepos(), repos2)
    }

    private companion object {
        const val USERNAME = "testUsername"
    }
}