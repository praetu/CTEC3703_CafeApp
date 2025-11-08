package com.example.ctec3703_cafeapp.data.model

sealed class FeedbackState {
    object Idle : FeedbackState()
    object Loading : FeedbackState()
    object Success : FeedbackState()
    data class Error(val message: String) : FeedbackState()
}