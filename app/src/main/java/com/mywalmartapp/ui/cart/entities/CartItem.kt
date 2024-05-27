package com.mywalmartapp.ui.cart.entities

import com.mywalmartapp.ui.productList.entities.ProductItem

data class CartItem(
    val product: ProductItem,
    var quantity: Int
)