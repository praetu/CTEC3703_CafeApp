package com.example.ctec3703_cafeapp.data.model

data class Cart(
    val cartId: String = "",
    val userId: String = "",
    val items: List<CartItem> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val orderStatus: Boolean = false  // false = active, true = order placed
)