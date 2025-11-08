package com.example.ctec3703_cafeapp.data.model

data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "customer",
    val createdAt: Long = System.currentTimeMillis()
)