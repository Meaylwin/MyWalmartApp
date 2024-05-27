package com.mywalmartapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mywalmartapp.ui.cart.entities.CartItem

object SharedPreferenceRepository {
    private const val PREF_NAME = "CartPrefs"
    private const val KEY_CART_ITEMS = "cartItems"
    private const val KEY_CART_TOTAL = "cartTotal"

    private lateinit var prefs: SharedPreferences
    private val gson = Gson()

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveCartItems(cartItems: List<CartItem>) {
        val json = gson.toJson(cartItems)
        prefs.edit().putString(KEY_CART_ITEMS, json).apply()
    }

    fun getCartItems(): List<CartItem> {
        val json = prefs.getString(KEY_CART_ITEMS, "")
        val type = object : TypeToken<List<CartItem>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun saveCartTotal(cartTotal: Float) {
        prefs.edit().putFloat(KEY_CART_TOTAL, cartTotal).apply()
    }

    fun getCartTotal(): Float {
        return prefs.getFloat(KEY_CART_TOTAL, 0f)
    }
}
