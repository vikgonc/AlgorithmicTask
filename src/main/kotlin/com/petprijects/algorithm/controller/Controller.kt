package com.petprijects.algorithm.controller

import com.petprijects.algorithm.model.Consumer
import com.petprijects.algorithm.model.MutableConsumer
import com.petprijects.algorithm.model.MutableResource
import com.petprijects.algorithm.model.Resource
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

    @GetMapping("/distribution")
    fun getMaxResourceDistribution(): List<MutableConsumer> {
        val resources = resourceRepo.findAll()
        val consumers = consumerRepo.findAll()
        val sortedMutableResources = resources.map { MutableResource(it) }.sortedBy { it.consumersId.size }
        val mutableConsumers = consumers.map { MutableConsumer(it) }
        for (resource in sortedMutableResources) {
            val currentSortedConsumers =
                mutableConsumers.filter { it.id in resource.consumersId }.sortedBy { it.capacity }.toMutableList()
            while (resource.capacity > 0.0 && currentSortedConsumers.size > 0) {
                val dividedResourceCapacity = resource.capacity / currentSortedConsumers.size
                val minCapacityFromConsumers = currentSortedConsumers.first().capacity
                if (minCapacityFromConsumers >= dividedResourceCapacity) {
                    currentSortedConsumers.forEach {
                        it.capacity -= dividedResourceCapacity
                        val currentMapValue = it.distribution[resource.id] ?: 0.0
                        it.distribution[resource.id] = currentMapValue + dividedResourceCapacity
                    }
                    resource.capacity = 0.0
                } else {
                    currentSortedConsumers.forEach {
                        it.capacity -= minCapacityFromConsumers
                        val currentMapValue = it.distribution[resource.id] ?: 0.0
                        it.distribution[resource.id] = currentMapValue + dividedResourceCapacity
                    }
                    resource.capacity -= minCapacityFromConsumers * currentSortedConsumers.size
                    currentSortedConsumers.removeFirst()
                }
            }
        }
        return mutableConsumers
    }

    @PostMapping("/resources")
    fun createResource(@Valid @RequestBody resource: Resource) = resourceRepo.save(resource)

    @PostMapping("/consumers")
    fun createConsumer(@Valid @RequestBody consumer: Consumer): Consumer {
        consumer.resources.forEach {
            resourceRepo.save(it)
        }
        return consumerRepo.save(consumer)
    }

    @PutMapping("/consumers/{id}")
    fun updateConsumer(
        @PathVariable id: Long,
        @Valid @RequestBody consumer: Consumer
    ): ResponseEntity<Consumer> {
        val consumerById = consumerRepo.findConsumerById(id)
        val responseEntity: ResponseEntity<Consumer> = consumerById?.let {
            consumer.resources.forEach {
                resourceRepo.save(it)
            }
            val responseBody = consumerRepo.save(consumer.copy(id = id))
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


