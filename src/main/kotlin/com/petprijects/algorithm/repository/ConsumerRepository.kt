package com.petprijects.algorithm.repository

import com.petprijects.algorithm.model.Consumer
import org.springframework.data.repository.CrudRepository

interface ConsumerRepository : CrudRepository<Consumer, Long> {
    override fun findAll(): List<Consumer>

    fun findConsumerById(id: Long): Consumer?
}