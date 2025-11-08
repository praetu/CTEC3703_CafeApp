package com.example.ctec3703_cafeapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ctec3703_cafeapp.data.model.Order
import com.example.ctec3703_cafeapp.data.model.User
import com.example.ctec3703_cafeapp.data.model.states.ProfileState
import com.example.ctec3703_cafeapp.data.repository.CafeRepository
import com.google.firebase.firestore.ListenerRegistration

class ProfileViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    private val _profileState = MutableLiveData<ProfileState>(ProfileState.Idle)
    val profileState: LiveData<ProfileState> = _profileState

    private val _orders = MutableLiveData<List<Order>>(emptyList())
    val orders: LiveData<List<Order>> = _orders

    private var ordersListener: ListenerRegistration? = null

    // Fetch current user profile

    fun fetchUser(userId: String) {

        _profileState.value = ProfileState.Loading

        repository.getUser(userId).get()
            .addOnSuccessListener { doc ->

                val user = doc.toObject(User::class.java)

                if (user != null) {
                    _profileState.value = ProfileState.Success(user)
                } else {
                    _profileState.value = ProfileState.Error("User not found")
                }
            }
            .addOnFailureListener { e ->
                _profileState.value = ProfileState.Error(e.message ?: "Error fetching user")
            }
    }

    // Observe orders for this user

    fun observeOrders(userId: String) {
        ordersListener = repository.getOrders()
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, exception ->

                if (exception != null) return@addSnapshotListener

                val userOrders = snapshot?.documents?.mapNotNull { it.toObject(Order::class.java) } ?: emptyList()
                _orders.value = userOrders
            }
    }

    override fun onCleared() {
        super.onCleared()
        ordersListener?.remove()
    }
}