package com.soul.core.service

import com.soul.core.domain.Food
import com.soul.core.repository.FoodRepository
import com.soul.core.utils.MenuUtils.Companion.generateSubIds
import com.soul.core.web.error.MenuNotFoundException
import org.springframework.stereotype.Service

@Service
class FoodService(
    private val repository: FoodRepository
) {

    fun getAll(): Food =
        repository.findAll().getOrElse(0) { throw MenuNotFoundException("Food not found") }

    fun add(food: Food): Food {
        generateSubIds(food)
        return repository.save(food)
    }

    fun deleteAll() = repository.deleteAll()

}