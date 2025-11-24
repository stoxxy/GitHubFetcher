package com.example.githubfetcher.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.githubfetcher.R
import com.example.githubfetcher.presentation.GitHubFetchResult
import com.example.githubfetcher.presentation.GitHubFetcherIntent
import com.example.githubfetcher.presentation.model.RepositoryUiModel
import com.example.githubfetcher.presentation.util.ContentWrapper
import com.example.githubfetcher.presentation.util.ErrorMessage
import com.example.githubfetcher.presentation.util.ItemWrapper

@Composable
fun RepoSearchScreen(
    username: String,
    fetchResult: GitHubFetchResult,
    onIntent: (GitHubFetcherIntent) -> Unit
) {
    ContentWrapper {
        OutlinedTextField(
            value = username,
            singleLine = true,
            onValueChange = {
                onIntent(GitHubFetcherIntent.SearchInput(it))
            },
            placeholder = {
                if (username.isEmpty()) Text(stringResource(R.string.input_username_placeholder))
            }
        )

        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            when (fetchResult) {
                GitHubFetchResult.None -> Text(stringResource(R.string.use_search_tool))
                GitHubFetchResult.InProgress -> CircularProgressIndicator()
                is GitHubFetchResult.Success -> ReposColumn(
                    repos = fetchResult.content as List<RepositoryUiModel>,
                    cached = fetchResult.cached,
                    onRepoSelect = { onIntent(GitHubFetcherIntent.SelectRepo(it)) }
                )
                is GitHubFetchResult.NoContent -> Text(stringResource(R.string.no_repos))
                is GitHubFetchResult.Error -> ErrorMessage(R.string.could_not_fetch_repos)
            }
        }
    }
}

@Composable
private fun ReposColumn(
    repos: List<RepositoryUiModel>,
    cached: Boolean,
    onRepoSelect: (String) -> Unit
) {
    ContentWrapper {
        if (cached) {
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icons.Filled.Refresh.let { Icon(it, it.name) }
                Spacer(Modifier.width(6.dp))
                Text(stringResource(R.string.recent_repos),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium))
            }
        }
        LazyColumn(
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp,
                Alignment.Top)
        ) {
            items(repos, key = { it.id }) {
                RepoItem(repository = it,
                    onRepoSelect = { onRepoSelect(it.name) })
            }
        }
   }
}


@Composable
private fun RepoItem(repository: RepositoryUiModel,
                     onRepoSelect: () -> Unit) {
    ItemWrapper(
        modifier = Modifier.clickable {
            onRepoSelect()
        }
    ) {
        Text(repository.name,
            style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(6.dp))
        repository.description?.let { Text(it) }
    }
}