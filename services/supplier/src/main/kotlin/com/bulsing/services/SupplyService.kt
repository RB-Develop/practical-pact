package com.bulsing.services

import com.bulsing.models.Supply
import com.bulsing.models.SupplyListing
import com.bulsing.models.supplyStorage
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondText

class SupplyService {
    init {
        supplyStorage.apply {
            add(Supply("orange", 20, 2))
            add(Supply("chair", 10, 50))
        }
    }

    suspend fun getAllSupply(call: ApplicationCall) {
        if (supplyStorage.isNotEmpty()) {
            call.respond(SupplyListing(supplyStorage), )
        } else {
            call.respondText("No supply found", status = HttpStatusCode.OK)
        }
    }

    suspend fun getSupply(call: ApplicationCall) {
        val id = call.parameters["id"] ?: return call.respondText(
            "Missing id",
            status = HttpStatusCode.BadRequest
        )
        val supply =
            supplyStorage.find { it.id == id } ?: return call.respondText(
                "No customer with id $id",
                status = HttpStatusCode.NotFound
            )
        call.respond(supply)
    }
}