package com.example.githubfetcher.presentation.util

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource

@Composable
fun ErrorMessage(
    @StringRes id: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icons.Filled.Warning.let { Icon(it, contentDescription = it.name) }
        Text(stringResource(id))
    }
}