package com.example.githubfetcher.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Repository(
    val id: Long,
    val name: String,
    val description: String?
): Parcelable
