package com.mywalmartapp.ui.productList

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
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    private val _cartTotal = MutableStateFlow(0f)

    val topProduct: StateFlow<ProductItem?> get() = _topProduct
    val products: StateFlow<List<ProductItem>> get() = _products
    val categories: StateFlow<List<String>> get() = _categories
    val selectedCategory: StateFlow<String?> get() = _selectedCategory
    val cartItems: StateFlow<List<CartItem>> get() = _cartItems
    val cartTotal: StateFlow<Float> get() = _cartTotal

    init {
        loadProducts()
        loadCategories()
        viewModelScope.launch {
            _cartItems.collect { updateCartTotal() }
        }
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
        val existingItem = _cartItems.value.find { it.product.id == product.id }
        val updatedItems = if (existingItem != null) {
            _cartItems.value.map {
                if (it.product.id == existingItem.product.id) {
                    it.copy(quantity = it.quantity + 1)
                } else {
                    it
                }
            }
        } else {
            _cartItems.value + CartItem(product, 1)
        }
        _cartItems.value = updatedItems
    }

    fun decreaseFromCart(product: ProductItem) {
        val existingItem = _cartItems.value.find { it.product.id == product.id }
        if (existingItem != null && existingItem.quantity > 1) {
            val updatedItems = _cartItems.value.map {
                if (it.product.id == existingItem.product.id) {
                    it.copy(quantity = it.quantity - 1)
                } else {
                    it
                }
            }
            _cartItems.value = updatedItems
        } else {
            removeFromCart(product)
        }
    }

    fun removeFromCart(product: ProductItem) {
        val updatedItems = _cartItems.value.filterNot { it.product.id == product.id }
        _cartItems.value = updatedItems
    }

    private fun updateCartTotal() {
        val total = _cartItems.value.sumOf { it.product.price * it.quantity }.toFloat()
        _cartTotal.value = total
    }
}