package com.example.ctec3703_cafeapp.data.model.states

import com.example.ctec3703_cafeapp.data.model.User

sealed class MainState {
    object Idle : MainState()
    object Loading : MainState()
    data class UserLoaded(val user: User) : MainState()
    data class Error(val message: String) : MainState()
}