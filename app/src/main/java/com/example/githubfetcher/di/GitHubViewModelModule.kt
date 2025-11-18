package com.example.githubfetcher.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(ViewModelComponent::class)
object GitHubViewModelModule {
    @Provides
    fun provideDispatcher() = Dispatchers.IO
}