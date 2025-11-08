package com.example.ctec3703_cafeapp.data.model

data class CartItem(
    val itemId: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val quantity: Int = 1
)