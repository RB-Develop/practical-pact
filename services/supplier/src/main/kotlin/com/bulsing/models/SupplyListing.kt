package com.bulsing.models

import kotlinx.serialization.Serializable

@Serializable
data class SupplyListing(val supplies: List<Supply>)