package com.example.ctec3703_cafeapp.ui.feedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ctec3703_cafeapp.data.repository.CafeRepository

class FeedbackViewModelFactory(
    private val repository: CafeRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(FeedbackViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FeedbackViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}