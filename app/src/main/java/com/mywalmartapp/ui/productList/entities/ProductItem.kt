package com.mywalmartapp.ui.productList.entities

import com.mywalmartapp.data.model.product.Rating

data class ProductItem (
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val image: String,
    val price: Double,
    val rating: Rating
)
