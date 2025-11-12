package com.example.ctec3703_cafeapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ctec3703_cafeapp.data.model.Order
import com.example.ctec3703_cafeapp.data.repository.CafeRepository
import com.google.firebase.firestore.ListenerRegistration

class ProfileViewModel(
    private val repository: CafeRepository,
    private val userId: String
) : ViewModel() {

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> = _orders

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var listener: ListenerRegistration? = null

    fun fetchUserOrders() {
        listener = repository.getOrders()
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, exception ->

                if (exception != null) {
                    _error.value = exception.message
                    return@addSnapshotListener
                }

                val orderList = snapshot?.documents?.mapNotNull { it.toObject(Order::class.java) } ?: emptyList()
                _orders.value = orderList.sortedByDescending { it.createdAt }
            }
    }

    override fun onCleared() {
        super.onCleared()
        listener?.remove()
    }
}