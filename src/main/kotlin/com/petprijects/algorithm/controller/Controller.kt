package com.petprijects.algorithm.controller

import com.petprijects.algorithm.model.Consumer
import com.petprijects.algorithm.model.Resource
import com.petprijects.algorithm.repository.ConsumerRepository
import com.petprijects.algorithm.repository.ResourceRepository
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class Controller(
    private val resourceRepo: ResourceRepository,
    private val consumerRepo: ConsumerRepository
) {
    @GetMapping("/resources")
    fun getAllResources() = resourceRepo.findAll()

    @GetMapping("/consumers")
    fun getAllConsumers() = consumerRepo.findAll()

    @PostMapping("/resources")
    fun createResource(@Valid @RequestBody resource: Resource) = resourceRepo.save(resource)

    @PostMapping("/consumer")
    fun createConsumer(@Valid @RequestBody consumer: Consumer) = consumerRepo.save(consumer)

    @DeleteMapping("/resources/{id}")
    fun removeResource(@PathVariable id: Long) = resourceRepo.deleteById(id)

    @DeleteMapping("/consumer/{id}")
    fun removeConsumer(@PathVariable id: Long) = consumerRepo.deleteById(id)
}


