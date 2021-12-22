package com.petprijects.algorithm.model

class MutableResource(
    resource: Resource,
    val id: Long? = resource.id,
    var capacity: Double = resource.capacity,
    val consumersId: List<Long?> = resource.consumers.map { it.id }
)