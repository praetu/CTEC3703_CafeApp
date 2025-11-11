package com.example.ctec3703_cafeapp.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ctec3703_cafeapp.data.repository.CafeRepository

class MenuViewModelFactory(
    private val repository: CafeRepository,
    private val userId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MenuViewModel(repository, userId) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}