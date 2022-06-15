package com.bulsing.routes

import com.bulsing.services.CustomerService
import io.ktor.server.application.*
import io.ktor.server.routing.*

class CustomerRoutes {
    private val customerService = CustomerService()

    fun Route.customerRouting() {
        route("/customer") {
            get { customerService.getAllCustomers(call) }
            get("{id?}") { customerService.getCustomer(call) }
            post { customerService.createCustomer(call) }
            delete("{id?}") { customerService.deleteCustomer(call) }
        }
    }
}