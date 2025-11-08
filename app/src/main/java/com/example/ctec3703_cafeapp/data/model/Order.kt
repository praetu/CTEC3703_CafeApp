package com.example.ctec3703_cafeapp.data.model

data class Order(
    val orderId: String = "",
    val userId: String = "",
    val cartId: String = "",
    val items: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val status: String = "Pending",      // e.g. Pending, Preparing, Ready, Completed
    val paymentStatus: String = "Unpaid", // e.g. Unpaid, Paid, Refunded
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)