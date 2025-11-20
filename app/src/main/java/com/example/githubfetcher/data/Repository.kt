package com.example.githubfetcher.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Entity
@Parcelize
data class Repository(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String?
): Parcelable
