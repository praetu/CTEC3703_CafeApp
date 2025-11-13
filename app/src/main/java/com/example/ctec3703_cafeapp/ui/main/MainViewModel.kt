package com.example.ctec3703_cafeapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ctec3703_cafeapp.data.model.User
import com.example.ctec3703_cafeapp.data.model.states.MainState

class MainViewModel : ViewModel() {

    private val _currentUser = MutableLiveData<User?>()

    private val _mainState = MutableLiveData<MainState>(MainState.Idle)

    fun setCurrentUser(user: User) {
        _currentUser.value = user
        _mainState.value = MainState.UserLoaded(user)
    }
}