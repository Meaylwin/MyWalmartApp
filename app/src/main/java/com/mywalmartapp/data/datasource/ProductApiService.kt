package com.mywalmartapp.data.datasource

import com.mywalmartapp.data.model.product.ProductResponseItem
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiService {

    @GET(GET_PRODUCTS)
    suspend fun getProducts(): List<ProductResponseItem>

    @GET(GET_PRODUCTS_BY_CATEGORY)
    suspend fun getProductsByCategory(@Path("category") category: String): List<ProductResponseItem>

    companion object {
        private const val GET_PRODUCTS = "/products"
        private const val GET_PRODUCTS_BY_CATEGORY = "category/{category}"
    }
}