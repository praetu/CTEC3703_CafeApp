package com.example.ctec3703_cafeapp.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ctec3703_cafeapp.data.model.MenuItem
import com.example.ctec3703_cafeapp.data.repository.CafeRepository
import com.google.firebase.firestore.ListenerRegistration

class MenuViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    // LiveData for the menu
    private val _menuItems = MutableLiveData<List<MenuItem>>(emptyList())
    val menuItems: LiveData<List<MenuItem>> = _menuItems

    // Optional: LiveData for loading/error state
    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    // Firestore listener registration (to remove listener when ViewModel clears)
    private var menuListener: ListenerRegistration? = null


    // Fetch menu items

    fun fetchMenuItems() {

        _loading.value = true

        menuListener = repository.getMenuItems()
            .addSnapshotListener { snapshot, exception ->

                _loading.value = false

                if (exception != null) {
                    _error.value = exception.message
                    return@addSnapshotListener
                }

                val items = snapshot?.documents?.mapNotNull { it.toObject(MenuItem::class.java) } ?: emptyList()
                _menuItems.value = items
            }
    }

    // Clean up listener when ViewModel destroyed

    override fun onCleared() {
        super.onCleared()
        menuListener?.remove()
    }
}