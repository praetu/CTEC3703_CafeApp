package com.example.ctec3703_cafeapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ctec3703_cafeapp.data.model.User
import com.example.ctec3703_cafeapp.data.model.states.MainState

class MainViewModel : ViewModel() {


    // Current user

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    private val _mainState = MutableLiveData<MainState>(MainState.Idle)
    val mainState: LiveData<MainState> = _mainState

    // Cart item count for badges

    private val _cartItemCount = MutableLiveData<Int>(0)
    val cartItemCount: LiveData<Int> = _cartItemCount

    // Global notifications/messages

    private val _notification = MutableLiveData<String?>()
    val notification: LiveData<String?> = _notification

    // Functions to update global states

    fun setCurrentUser(user: User) {
        _currentUser.value = user
        _mainState.value = MainState.UserLoaded(user)
    }

    fun clearCurrentUser() {
        _currentUser.value = null
        _mainState.value = MainState.Idle
    }

    fun updateCartItemCount(count: Int) {
        _cartItemCount.value = count
    }

    fun showNotification(message: String) {
        _notification.value = message
    }

    fun clearNotification() {
        _notification.value = null
    }
}