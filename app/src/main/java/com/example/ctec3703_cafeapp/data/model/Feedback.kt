package com.example.ctec3703_cafeapp.data.model

data class Feedback(
    val feedbackId: String = "",
    val userId: String = "",
    val rating: Int = 0,           // e.g. 1–5 stars
    val review: String = "",
    val createdAt: Long = System.currentTimeMillis()
)