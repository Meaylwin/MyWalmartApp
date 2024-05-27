package com.mywalmartapp.data.datasource

import com.mywalmartapp.data.model.product.ProductResponseItem
import retrofit2.http.GET

interface CategoryApiService {

    @GET(GET_CATEGORIES)
    suspend fun getCategories(): List<String>

    companion object {
        private const val GET_CATEGORIES = "/products/categories"
    }
}