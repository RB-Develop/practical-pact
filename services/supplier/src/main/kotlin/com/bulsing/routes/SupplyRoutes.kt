package com.bulsing.routes

import com.bulsing.services.SupplyService
import io.ktor.server.application.*
import io.ktor.server.routing.*

class SupplyRoutes {
    private val supplyService = SupplyService()

    fun Route.customerRouting() {
        route("/supply") {
            get { supplyService.getAllSupply(call) }
            get("{id?}") { supplyService.getSupply(call) }
        }
    }
}