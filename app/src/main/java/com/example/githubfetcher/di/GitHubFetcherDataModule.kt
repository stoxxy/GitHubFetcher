package com.example.githubfetcher.di

import android.content.Context
import androidx.room.Room
import com.example.githubfetcher.data.GitHubFetcherDataSource
import com.example.githubfetcher.data.GitHubFetcherDatabase
import com.example.githubfetcher.data.GitHubFetcherRepositoryImpl
import com.example.githubfetcher.data.GitHubRepoFetcherImpl
import com.example.githubfetcher.domain.GitHubFetcherRepository
import com.example.githubfetcher.domain.GitHubRepoFetcher
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.engine.cio.CIO

@Module
@InstallIn(SingletonComponent::class)
abstract class GitHubFetcherDataModule {
    @Binds
    abstract fun bindGitHubFetcherRepository(
        impl: GitHubFetcherRepositoryImpl
    ): GitHubFetcherRepository

    @Binds
    abstract fun bindGitHubRepoFetcher(
        impl: GitHubRepoFetcherImpl
    ): GitHubRepoFetcher

    companion object {
        @Provides
        fun provideGitHubRepoFetcher() = GitHubRepoFetcherImpl(engine = CIO.create())
        @Provides
        fun provideGitHubFetcherDatabase(@ApplicationContext context: Context): GitHubFetcherDatabase =
            Room.databaseBuilder(
                context,
                GitHubFetcherDatabase::class.java,
                "github-fetcher-database"
            ).build()

        @Provides
        fun provideGitHubFetcherDataSource(
            gitHubFetcherDatabase: GitHubFetcherDatabase
        ) = GitHubFetcherDataSource(gitHubFetcherDatabase)
        @Provides
        fun provideGitHubFetcherRepository(
            gitHubRepoFetcher: GitHubRepoFetcher,
            gitHubFetcherDataSource: GitHubFetcherDataSource
        ) = GitHubFetcherRepositoryImpl(
            gitHubRepoFetcher = gitHubRepoFetcher,
            gitHubFetcherDataSource = gitHubFetcherDataSource
        )
    }
}