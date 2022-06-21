package com.bulsing.models

import kotlinx.serialization.Serializable

@Serializable
data class ExternalSupply(val id: String, val quantity: Int, val unitPrice: Int)

@Serializable
data class ExternalSupplyListing(val supplies: List<ExternalSupply>)