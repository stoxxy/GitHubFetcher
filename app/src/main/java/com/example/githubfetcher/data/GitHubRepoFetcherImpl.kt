package com.example.githubfetcher.data

import GitHubFetcher.app.BuildConfig
import com.example.githubfetcher.data.model.RepositoryEntity
import com.example.githubfetcher.domain.GitHubRepoFetcher
import com.example.githubfetcher.domain.model.Commit
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class GitHubRepoFetcherImpl(
    engine: HttpClientEngine
): GitHubRepoFetcher {
    private val client = HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY
        }
        defaultRequest { url("https://api.github.com/") }
    }
    override suspend fun fetchRepos(username: String): List<RepositoryEntity> {
        return client.get("users/$username/repos") {
           headers {
               append(HttpHeaders.Authorization, "Bearer ${BuildConfig.GITHUB_ACCESS_TOKEN}")
           }
       }.body()
    }

    override suspend fun fetchCommits(
        username: String,
        repo: String
    ): List<Commit> {
        return client.get("repos/$username/$repo/commits") {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${BuildConfig.GITHUB_ACCESS_TOKEN}")
            }
        }.body()
    }
}