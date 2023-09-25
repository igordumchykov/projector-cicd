package com.soul.core.service

import com.soul.core.domain.Drinks
import com.soul.core.repository.DrinksRepository
import com.soul.core.utils.MenuUtils.Companion.generateSubIds
import com.soul.core.web.error.MenuNotFoundException
import org.springframework.stereotype.Service

@Service
class DrinksService(
    private val repository: DrinksRepository
) {

    fun getAll(): Drinks =
        repository.findAll().getOrElse(0) { throw MenuNotFoundException("Drinks not found") }

    fun add(drinks: Drinks): Drinks {
        generateSubIds(drinks)
        return repository.save(drinks)
    }
    fun deleteAll() = repository.deleteAll()
}