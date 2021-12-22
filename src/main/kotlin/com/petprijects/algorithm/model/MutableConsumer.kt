package com.petprijects.algorithm.model

class MutableConsumer(
    consumer: Consumer,
    val id: Long? = consumer.id,
    var capacity: Int = consumer.capacity,
    val distribution: MutableMap<Long?, Int> = consumer.resources.associateBy({ it.id }, { 0 })
        .toSortedMap(compareBy { it }).toMutableMap()
)