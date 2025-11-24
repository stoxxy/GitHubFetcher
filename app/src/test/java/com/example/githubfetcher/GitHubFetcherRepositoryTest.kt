package com.example.githubfetcher

import com.example.githubfetcher.data.GitHubFetcherDataSource
import com.example.githubfetcher.data.GitHubFetcherDatabase
import com.example.githubfetcher.data.GitHubFetcherRepositoryImpl
import com.example.githubfetcher.data.model.RepositoryEntity
import com.example.githubfetcher.domain.GitHubFetcherRepository
import com.example.githubfetcher.domain.GitHubRepoFetcher
import com.example.githubfetcher.domain.model.Commit
import com.example.githubfetcher.domain.model.CommitContent
import com.example.githubfetcher.util.RemoteGitHubFetchException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import org.junit.Assert.assertThrows
import org.junit.Test

class GitHubFetcherRepositoryTest {
    private val repository: GitHubFetcherRepository
        get() = GitHubFetcherRepositoryImpl(
            gitHubRepoFetcher = gitHubRepoFetcher,
            gitHubFetcherDataSource = gitHubFetcherDataSource
        )
    private var gitHubRepoFetcher: GitHubRepoFetcher = mockk(relaxed = true)
    private var gitHubFetcherDataSource: GitHubFetcherDataSource = mockk(relaxed = true)

    @Test
    fun reposFetch_success_noExceptionThrownAndReposInserted() = runTest {
        val repos = List(10) { RepositoryEntity(id = it.toLong(), USERNAME, "") }
        coEvery { gitHubRepoFetcher.fetchRepos(USERNAME) } returns repos

        repository.fetchRepos(USERNAME)

        coVerify(exactly = 1) {
            gitHubRepoFetcher.fetchRepos(USERNAME)
            gitHubFetcherDataSource.saveRepos(repos)
        }
    }

    @Test
    fun networkReposFetch_error_RemoteGitHubFetchExceptionThrown() {
        coEvery { gitHubRepoFetcher.fetchRepos(USERNAME) } throws RemoteGitHubFetchException(EXCEPTION_MESSAGE)

        assertThrows(RemoteGitHubFetchException::class.java) {
            runBlocking { repository.fetchRepos(USERNAME) }
        }

        coVerify(exactly = 1) { gitHubRepoFetcher.fetchRepos(USERNAME) }
        coVerify(inverse = true) { gitHubFetcherDataSource.saveRepos(any()) }
    }

    @Test
    fun commitsFetch_success_noExceptionThrown() = runTest {
        val commits = List(10) { mockk<Commit>() }
        coEvery { gitHubRepoFetcher.fetchCommits(USERNAME, REPO_NAME) } returns commits

        repository.fetchCommits(USERNAME, REPO_NAME)

        coVerify(exactly = 1) {
            gitHubRepoFetcher.fetchCommits(USERNAME, REPO_NAME)
        }
    }

    @Test
    fun commitsFetch_error_exceptionThrown() {
        coEvery { gitHubRepoFetcher.fetchCommits(USERNAME, REPO_NAME) } throws RemoteGitHubFetchException(EXCEPTION_MESSAGE)

        assertThrows(IOException::class.java) {
            runBlocking { repository.fetchCommits(USERNAME, REPO_NAME) }
        }
    }

    private companion object {
        const val USERNAME = "testUsername"
        const val REPO_NAME = "testRepoName"
        const val EXCEPTION_MESSAGE = "exceptionMessage"
    }
}