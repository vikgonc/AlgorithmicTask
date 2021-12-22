package com.petprijects.algorithm.repository

import com.petprijects.algorithm.model.Resource
import org.springframework.data.repository.CrudRepository

interface ResourceRepository : CrudRepository<Resource, Long> {
    override fun findAll(): List<Resource>

    fun findResourceById(id: Long): Resource?
}