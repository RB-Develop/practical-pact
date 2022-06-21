package com.bulsing.models

import kotlinx.serialization.Serializable

@Serializable
data class Supply(val id: String, val quantity: Int, val unitPrice: Int)

val supplyStorage = mutableListOf<Supply>()