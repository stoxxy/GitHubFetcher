package com.example.githubfetcher.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.githubfetcher.domain.model.Repository
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "repository")
data class RepositoryEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String?
)

fun RepositoryEntity.toDomain() =
    Repository(
        id = this.id,
        name = this.name,
        description = this.description
    )

fun List<RepositoryEntity>.toDomain() = map { it.toDomain() }