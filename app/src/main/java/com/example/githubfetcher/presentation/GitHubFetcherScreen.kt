package com.example.githubfetcher.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.githubfetcher.R
import com.example.githubfetcher.data.Repository

@Composable
fun GitHubFetcherScreen(
    modifier: Modifier,
    viewModel: GitHubFetcherViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    GitHubFetcherScreenContent(
        modifier = modifier,
        uiState = uiState,
        onIntent = viewModel::handleIntent
    )
}

@Composable
private fun GitHubFetcherScreenContent(
    modifier: Modifier,
    uiState: UiState,
    onIntent: (GitHubFetcherIntent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = uiState.username,
            singleLine = true,
            onValueChange = {
                onIntent(GitHubFetcherIntent.SearchInput(it))
            },
            placeholder = {
                if (uiState.username.isEmpty()) Text(stringResource(R.string.input_username_placeholder))
            }
        )

        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            when (uiState.fetchResult) {
                GitHubFetchResult.None -> Text(stringResource(R.string.use_search_tool))
                GitHubFetchResult.InProgress -> CircularProgressIndicator()
                is GitHubFetchResult.Success -> {
                    if (uiState.fetchResult.repos.isEmpty()) Text(stringResource(R.string.no_repos))
                    else ReposColumn(repos = uiState.fetchResult.repos)
                }
                is GitHubFetchResult.Error -> Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icons.Filled.Warning.let { Icon(it, contentDescription = it.name) }
                    Text(stringResource(R.string.could_not_fetch_repos))
                }
            }
        }
    }
}

@Composable
private fun ReposColumn(
    repos: List<Repository>
) {
    LazyColumn(
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(repos, key = { it.id }) {
            RepoItem(it)
        }
    }
}


@Composable
private fun RepoItem(repository: Repository) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(20.dp),
        ) {
            Text(repository.name,
                style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(6.dp))
            repository.description?.let { Text(it) }
        }
    }
}