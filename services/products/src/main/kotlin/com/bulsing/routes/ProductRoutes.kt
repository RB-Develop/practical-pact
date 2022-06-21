package com.bulsing.routes

import com.bulsing.services.ProductService
import io.ktor.client.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

class ProductRoutes(httpClient: HttpClient) {
    private val productService = ProductService(httpClient)

    fun Route.customerRouting() {
        route("/products") {
            get { productService.getAllProducts(call) }
            get("{id?}") { productService.getProduct(call) }
        }
    }
}