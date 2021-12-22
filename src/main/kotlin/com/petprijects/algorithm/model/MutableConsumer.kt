package com.petprijects.algorithm.model

class MutableConsumer(
    consumer: Consumer,
    val id: Long? = consumer.id,
    var capacity: Double = consumer.capacity,
    val distribution: MutableMap<Long?, Double> = consumer.resources.associateBy({ it.id }, { 0.0 })
        .toSortedMap(compareBy { it }).toMutableMap()
)