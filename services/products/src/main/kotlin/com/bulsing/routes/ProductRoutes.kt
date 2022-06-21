package com.bulsing.routes

import com.bulsing.services.ProductService
import io.ktor.server.application.*
import io.ktor.server.routing.*

class ProductRoutes {
    private val productService = ProductService()

    fun Route.customerRouting() {
        route("/products") {
            get { productService.getAllProducts(call) }
            get("{id?}") { productService.getProduct(call) }
        }
    }
}