package com.petprijects.algorithm.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

@Entity
@Table(name = "consumers")
data class Consumer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:Min(300)
    @field:Max(600)
    val capacity: Int,

    @field:NotBlank
    @field:Pattern(regexp = "^([0-9]|,)*$")
    val preferences: String
)