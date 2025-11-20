package com.example.githubfetcher

import com.example.githubfetcher.data.model.RepositoryEntity
import com.example.githubfetcher.data.model.toDomain
import com.example.githubfetcher.domain.model.Repository
import com.example.githubfetcher.presentation.model.toUi
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MapperTest {

    @Test
    fun entityToDomain_fieldsAreCorrect() {
        val domain = entity.toDomain()

        assertEquals(ID, domain.id)
        assertEquals(NAME, domain.name)
        assertEquals(DESCRIPTION, domain.description)
    }

    @Test
    fun entityListToDomain_fieldsAreCorrect() {
        val entities = List(10) { entity.copy(id = it.toLong()) }
        val domains = entities.toDomain()

        for (i in entities.indices) {
            assertEquals(entities[i].id, domains[i].id)
            assertEquals(entities[i].name, domains[i].name)
            assertEquals(entities[i].description, domains[i].description)
        }
    }

    @Test
    fun domainToUi_fieldsAreCorrect() {
        val ui = domain.toUi()

        assertEquals(ui.id, domain.id)
        assertEquals(ui.name, domain.name)
        assertEquals(ui.description, domain.description)
    }

    @Test
    fun domainListToUi_fieldsAreCorrect() {
        val domains = List(10) { domain.copy(id = it.toLong()) }
        val uis = domains.toUi()

        for (i in domains.indices) {
            assertEquals(uis[i].id, domains[i].id)
            assertEquals(uis[i].name, domains[i].name)
            assertEquals(uis[i].description, domains[i].description)
        }
    }

    private companion object {
        const val ID = 0L
        const val NAME = "testName"
        const val DESCRIPTION = "testDescription"

        val entity = RepositoryEntity(
            id = ID,
            name = NAME,
            description = DESCRIPTION
        )
        val domain = Repository(
            id = ID,
            name = NAME,
            description = DESCRIPTION
        )
    }
}