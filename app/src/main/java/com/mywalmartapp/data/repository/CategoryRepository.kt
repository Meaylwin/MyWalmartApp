package com.mywalmartapp.data.repository


import com.mywalmartapp.data.datasource.CategoryApiService
import com.mywalmartapp.ui.productList.entities.ProductItem
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val categoryApiService: CategoryApiService) {

    suspend fun getCategories(): List<String> {
        return categoryApiService.getCategories()
    }
}