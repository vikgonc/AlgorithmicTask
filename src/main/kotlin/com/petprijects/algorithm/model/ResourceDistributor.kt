package com.petprijects.algorithm.model

import com.petprijects.algorithm.repository.ConsumerRepository
import com.petprijects.algorithm.repository.ResourceRepository
import org.springframework.stereotype.Component

@Component
class ResourceDistributor(
    private val resourceRepo: ResourceRepository,
    private val consumerRepo: ConsumerRepository
) {
    fun doDistribution(): List<MutableConsumer> {
        val resources = resourceRepo.findAll()
        val consumers = consumerRepo.findAll()
        val sortedMutableResources = resources.map { MutableResource(it) }.sortedBy { it.consumersId.size }
        val mutableConsumers = consumers.map { MutableConsumer(it) }
        for (resource in sortedMutableResources) {
            val currentSortedConsumers =
                mutableConsumers.filter { it.id in resource.consumersId }.sortedBy { it.capacity }.toMutableList()
            while (resource.capacity > 0 && currentSortedConsumers.size > 0) {
                val dividedResourceCapacity = resource.capacity / currentSortedConsumers.size
                val minCapacityFromConsumers = currentSortedConsumers.first().capacity
                if (minCapacityFromConsumers >= dividedResourceCapacity) {
                    currentSortedConsumers.forEach {
                        it.capacity -= dividedResourceCapacity
                        val currentMapValue = it.distribution[resource.id] ?: 0
                        it.distribution[resource.id] = currentMapValue + dividedResourceCapacity
                    }
                    resource.capacity %= currentSortedConsumers.size
                    if (resource.capacity > 0)
                        for (currentConsumer in currentSortedConsumers)
                            if (currentConsumer.capacity > 0) {
                                currentConsumer.capacity -= 1
                                val currentMapValue = currentConsumer.distribution[resource.id] ?: 0
                                currentConsumer.distribution[resource.id] = currentMapValue + 1
                                resource.capacity -= 1
                                if (resource.capacity == 0)
                                    break
                            } else
                                currentSortedConsumers.remove(currentConsumer)
                } else {
                    currentSortedConsumers.forEach {
                        it.capacity -= minCapacityFromConsumers
                        val currentMapValue = it.distribution[resource.id] ?: 0
                        it.distribution[resource.id] = currentMapValue + minCapacityFromConsumers
                    }
                    resource.capacity -= minCapacityFromConsumers * currentSortedConsumers.size
                    currentSortedConsumers.removeFirst()
                }
            }
        }
        return mutableConsumers
    }
}