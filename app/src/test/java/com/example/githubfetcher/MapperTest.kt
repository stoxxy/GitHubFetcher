package com.example.githubfetcher

import com.example.githubfetcher.data.model.RepositoryEntity
import com.example.githubfetcher.data.model.toDomain
import com.example.githubfetcher.domain.model.Commit
import com.example.githubfetcher.domain.model.CommitAuthor
import com.example.githubfetcher.domain.model.CommitContent
import com.example.githubfetcher.domain.model.Repository
import com.example.githubfetcher.presentation.model.toUi
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MapperTest {

    @Test
    fun repositoryEntityToDomain_fieldsAreCorrect() {
        val domain = entityRepository.toDomain()

        assertEquals(REPOSITORY_ID, domain.id)
        assertEquals(REPOSITORY_NAME, domain.name)
        assertEquals(REPOSITORY_DESCRIPTION, domain.description)
    }

    @Test
    fun repositoryEntityListToDomain_fieldsAreCorrect() {
        val entities = List(10) { entityRepository.copy(id = it.toLong()) }
        val domains = entities.toDomain()

        for (i in entities.indices) {
            assertEquals(entities[i].id, domains[i].id)
            assertEquals(entities[i].name, domains[i].name)
            assertEquals(entities[i].description, domains[i].description)
        }
    }

    @Test
    fun repositoryDomainToUi_fieldsAreCorrect() {
        val ui = domainRepository.toUi()

        assertEquals(ui.id, domainRepository.id)
        assertEquals(ui.name, domainRepository.name)
        assertEquals(ui.description, domainRepository.description)
    }

    @Test
    fun repositoryDomainListToUi_fieldsAreCorrect() {
        val domains = List(10) { domainRepository.copy(id = it.toLong()) }
        val uis = domains.toUi()

        for (i in domains.indices) {
            assertEquals(uis[i].id, domains[i].id)
            assertEquals(uis[i].name, domains[i].name)
            assertEquals(uis[i].description, domains[i].description)
        }
    }

    @Test
    fun commitDomainToUi_fieldsAreCorrect() {
        val ui = domainCommit.toUi().commit

        assertEquals(ui.author.name, COMMIT_AUTHOR)
        assertEquals(ui.message, COMMIT_MESSAGE)
        assertEquals(ui.url, COMMIT_URL)
    }

    private companion object {
        const val REPOSITORY_ID = 0L
        const val REPOSITORY_NAME = "testName"
        const val REPOSITORY_DESCRIPTION = "testDescription"
        const val COMMIT_URL = "commitUrl"
        const val COMMIT_AUTHOR = "commitAuthor"
        const val COMMIT_MESSAGE = "commitMessage"

        val entityRepository = RepositoryEntity(
            id = REPOSITORY_ID,
            name = REPOSITORY_NAME,
            description = REPOSITORY_DESCRIPTION
        )
        val domainRepository = Repository(
            id = REPOSITORY_ID,
            name = REPOSITORY_NAME,
            description = REPOSITORY_DESCRIPTION
        )
        val domainCommit = Commit(
            commit = CommitContent(
                url = COMMIT_URL,
                author = CommitAuthor(name = COMMIT_AUTHOR),
                message = COMMIT_MESSAGE
            )
        )
    }
}