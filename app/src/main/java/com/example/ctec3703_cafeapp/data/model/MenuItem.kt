package com.example.ctec3703_cafeapp.data.model

data class MenuItem(
    val itemId: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val category: String = "",   // e.g. "Drink", "Cake"
    val quantity: Int = 0,       // available stock
    val imageUrl: String = ""    // optional, for displaying product image
)