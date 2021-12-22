package com.petprijects.algorithm.controller

import com.petprijects.algorithm.model.Consumer
import com.petprijects.algorithm.model.Resource
import com.petprijects.algorithm.model.ResourceDistributor
import com.petprijects.algorithm.repository.ConsumerRepository
import com.petprijects.algorithm.repository.ResourceRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class Controller(
    private val resourceRepo: ResourceRepository,
    private val consumerRepo: ConsumerRepository,
    private val resourceDist: ResourceDistributor
) {
    @GetMapping("/resources")
    fun getAllResources() = resourceRepo.findAll()

    @GetMapping("/consumers")
    fun getAllConsumers() = consumerRepo.findAll()

    @GetMapping("/distribution")
    fun getMaxResourceDistribution() = resourceDist.doDistribution()

    @PostMapping("/resources")
    fun createResource(@Valid @RequestBody resource: Resource) = resourceRepo.save(resource)

    @PostMapping("/consumers")
    fun createConsumer(
        @RequestParam preferences: String,
        @Valid @RequestBody consumer: Consumer
    ): Consumer {
        val listOfPreferences =
            preferences.split(',').map { it.toLong() }.mapNotNull { resourceRepo.findResourceById(it) }
        return consumerRepo.save(consumer.copy(resources = listOfPreferences))
    }

    @PutMapping("/consumers/{id}")
    fun updateConsumer(
        @PathVariable id: Long,
        @RequestParam preferences: String,
        @Valid @RequestBody consumer: Consumer
    ): ResponseEntity<Consumer> {
        val consumerById = consumerRepo.findConsumerById(id)
        val responseEntity: ResponseEntity<Consumer> = consumerById?.let {
            val listOfPreferences =
                preferences.split(',').map { it.toLong() }.mapNotNull { resourceRepo.findResourceById(it) }
            val responseBody = consumerRepo.save(consumer.copy(id = id, resources = listOfPreferences))
            ResponseEntity
                .status(200)
                .body(responseBody)
        } ?: ResponseEntity
            .status(404)
            .body(null)
        return responseEntity
    }

    @PatchMapping("/resources/{id}")
    fun updateResourceCapacity(
        @PathVariable id: Long,
        @Valid @RequestBody resource: Resource
    ): ResponseEntity<Resource> {
        val resourceById = resourceRepo.findResourceById(id)
        val responseEntity: ResponseEntity<Resource> = resourceById?.let {
            val responseBody = resourceRepo.save(resourceById.copy(capacity = resource.capacity))
            ResponseEntity
                .status(200)
                .body(responseBody)
        } ?: ResponseEntity
            .status(404)
            .body(null)
        return responseEntity
    }

    @DeleteMapping("/resources/{id}")
    fun removeResource(@PathVariable id: Long) {
        val resourceById = resourceRepo.findResourceById(id)
        resourceById?.let { resourceRepo.deleteById(id) }
    }

    @DeleteMapping("/consumers/{id}")
    fun removeConsumer(@PathVariable id: Long) {
        val consumerById = consumerRepo.findConsumerById(id)
        consumerById?.let { consumerRepo.deleteById(id) }
    }
}


