package com.example.ctec3703_cafeapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ctec3703_cafeapp.data.repository.CafeRepository
import com.example.ctec3703_cafeapp.ui.main.MainViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModelFactory(
    private val repository: CafeRepository,
    private val mainViewModel: MainViewModel,
    private val auth: FirebaseAuth
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository, mainViewModel, auth) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}