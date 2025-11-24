package com.example.githubfetcher.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.githubfetcher.R
import com.example.githubfetcher.presentation.GitHubFetchResult
import com.example.githubfetcher.presentation.model.CommitContentUiModel
import com.example.githubfetcher.presentation.model.CommitUiModel
import com.example.githubfetcher.presentation.util.ContentWrapper
import com.example.githubfetcher.presentation.util.ErrorMessage
import com.example.githubfetcher.presentation.util.ItemWrapper

@Composable
fun CommitListScreen(
    name: String,
    fetchResult: GitHubFetchResult,
    onBack: () -> Unit
) {
    ContentWrapper {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dpx)
        ) {
            IconButton(onClick = onBack,
                modifier = Modifier.weight(0.2f)) { Icons.AutoMirrored.Filled.ArrowBack.let { Icon(it, it.name) } }
            Text(stringResource(R.string.commits_screen_title, name),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(0.8f))
        }
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            when (fetchResult) {
                GitHubFetchResult.None -> {}
                GitHubFetchResult.InProgress -> CircularProgressIndicator()
                is GitHubFetchResult.Success -> CommitListContent(commits = fetchResult.content as List<CommitUiModel>)
                GitHubFetchResult.NoContent -> Text(stringResource(R.string.no_commits))
                is GitHubFetchResult.Error -> ErrorMessage(R.string.could_not_fetch_commits)
            }
        }
    }
}

@Composable
fun CommitListContent(
    commits: List<CommitUiModel>
) {
    LazyColumn(
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp,
            Alignment.Top)
    ) {
        items(commits, key = { it.commit.url }) {
            CommitItem(it.commit)
        }
    }
}

@Composable
fun CommitItem(commit: CommitContentUiModel) {
    ItemWrapper {
        Text(commit.message,
            style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(6.dp))
        Text(commit.author.name)
    }
}