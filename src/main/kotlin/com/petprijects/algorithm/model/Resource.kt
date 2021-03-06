package com.petprijects.algorithm.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.Table
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Entity
@Table(name = "resources")
data class Resource(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:Min(450)
    @field:Max(800)
    val capacity: Int,

    @JsonIgnore
    @ManyToMany(mappedBy = "resources")
    val consumers: List<Consumer> = listOf()
)