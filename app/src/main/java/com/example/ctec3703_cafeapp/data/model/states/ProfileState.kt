package com.example.ctec3703_cafeapp.data.model.states

import com.example.ctec3703_cafeapp.data.model.User

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    data class Success(val user: User) : ProfileState()
    data class Error(val message: String) : ProfileState()
}