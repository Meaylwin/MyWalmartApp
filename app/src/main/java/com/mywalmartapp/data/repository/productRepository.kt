package com.mywalmartapp.data.repository


import com.mywalmartapp.data.datasource.ProductApiService
import com.mywalmartapp.ui.productList.entities.ProductItem
import javax.inject.Inject

class ProductRepository @Inject constructor(private val productApiService: ProductApiService) {

    suspend fun getProducts(): List<ProductItem> {
        return productApiService.getProducts().map { productResponseItem ->
            ProductItem(
                id = productResponseItem.id,
                title = productResponseItem.title,
                description = productResponseItem.description,
                category = productResponseItem.category,
                image = productResponseItem.image,
                price = productResponseItem.price,
                rating = productResponseItem.rating
            )
        }
    }

    suspend fun getProductsByCategory(category: String): List<ProductItem> {
        return productApiService.getProductsByCategory(category.lowercase()).map { productResponseItem ->
            ProductItem(
                id = productResponseItem.id,
                title = productResponseItem.title,
                description = productResponseItem.description,
                category = productResponseItem.category,
                image = productResponseItem.image,
                price = productResponseItem.price,
                rating = productResponseItem.rating
            )
        }
    }
}