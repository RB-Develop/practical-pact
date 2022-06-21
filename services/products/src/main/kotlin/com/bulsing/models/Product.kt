package com.bulsing.models

import kotlinx.serialization.Serializable

@Serializable
data class Product(val id: String, val name: String, val price: Int)

val productStorage = mutableListOf<Product>()