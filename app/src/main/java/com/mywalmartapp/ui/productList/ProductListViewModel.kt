package com.mywalmartapp.ui.productList

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mywalmartapp.data.repository.ProductRepository
import com.mywalmartapp.data.repository.CategoryRepository
import com.mywalmartapp.ui.cart.entities.CartItem
import com.mywalmartapp.ui.productList.entities.ProductItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _topProduct = MutableStateFlow<ProductItem?>(null)
    private val _products = MutableStateFlow<List<ProductItem>>(emptyList())
    private val _categories = MutableStateFlow(listOf("Todos"))
    private val _selectedCategory = MutableStateFlow("Todos")
    private val _cartItems = mutableStateListOf<CartItem>()
    private val _cartItemsState = MutableStateFlow<List<CartItem>>(emptyList())

    val topProduct: StateFlow<ProductItem?> get() = _topProduct
    val products: StateFlow<List<ProductItem>> get() = _products
    val categories: StateFlow<List<String>> get() = _categories
    val selectedCategory: StateFlow<String?> get() = _selectedCategory
    val cartItemsState: StateFlow<List<CartItem>> get() = _cartItemsState

    init {
        loadProducts()
        loadCategories()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            val productList = productRepository.getProducts()
            generateProductList(productList)
        }
    }

    private fun generateProductList(productList: List<ProductItem>) {
        val topProduct = calculateTopProduct(productList)
        _topProduct.value = topProduct
        val productListWithoutTop = productList.filter { it != topProduct }
        _products.value = productListWithoutTop
    }

    private fun calculateTopProduct(products: List<ProductItem>): ProductItem? {
        return products.maxByOrNull { it.rating.rate * it.rating.count }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val categoryList = categoryRepository.getCategories().map { category ->
                category.replaceFirstChar { it.titlecase() }
            }
            _categories.value += categoryList
        }
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        if (category == "Todos") {
            loadProducts()
        }
        else {
            viewModelScope.launch {
                val productList = productRepository.getProductsByCategory(category)
                generateProductList(productList)
            }
        }
    }


    fun addToCart(product: ProductItem) {
        val existingItem = _cartItems.find { it.product.id == product.id }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            _cartItems.add(CartItem(product, 1))
        }
        _cartItemsState.value = _cartItems.toList()
    }

    fun decreaseToCart(product: ProductItem) {
        val existingItem = _cartItems.find { it.product.id == product.id }
        existingItem?.let {
            if (it.quantity > 1) {
                it.quantity--
            } else {
                _cartItems.remove(it)
            }
            _cartItemsState.value = _cartItems.toList()
        }
    }

    fun removeToCart(product: ProductItem) {
        val existingItem = _cartItems.find { it.product.id == product.id }
        println("hola"+existingItem)
        existingItem?.let {
            _cartItems.remove(it)
            _cartItemsState.value = _cartItems.toList()
        }
    }
}