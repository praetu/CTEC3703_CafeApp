package com.example.ctec3703_cafeapp.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ctec3703_cafeapp.data.repository.CafeRepository

class CartViewModelFactory(
    private val repository: CafeRepository,
    private val userId: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            return CartViewModel(repository, userId) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}