package com.bulsing.services

import com.bulsing.models.Customer
import com.bulsing.models.customerStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class CustomerService {
    suspend fun getAllCustomers(call: ApplicationCall) {
        if (customerStorage.isNotEmpty()) {
            call.respond(customerStorage)
        } else {
            call.respondText("No customers found", status = HttpStatusCode.OK)
        }
    }

    suspend fun getCustomer(call: ApplicationCall) {
        val id = call.parameters["id"] ?: return call.respondText(
            "Missing id",
            status = HttpStatusCode.BadRequest
        )
        val customer =
            customerStorage.find { it.id == id } ?: return call.respondText(
                "No customer with id $id",
                status = HttpStatusCode.NotFound
            )
        call.respond(customer)
    }

    suspend fun createCustomer(call: ApplicationCall) {
        val customer = call.receive<Customer>()
        customerStorage.add(customer)
        call.respondText("Customer stored correctly", status = HttpStatusCode.Created)
    }

    suspend fun deleteCustomer(call: ApplicationCall) {
        val id = call.parameters["id"] ?: return call.respond(HttpStatusCode.BadRequest)
        if (customerStorage.removeIf { it.id == id }) {
            call.respondText("Customer removed correctly", status = HttpStatusCode.Accepted)
        } else {
            call.respondText("Not Found", status = HttpStatusCode.NotFound)
        }
    }
}