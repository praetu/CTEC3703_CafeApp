package com.example.ctec3703_cafeapp.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ctec3703_cafeapp.data.model.Cart
import com.example.ctec3703_cafeapp.data.model.CartItem
import com.example.ctec3703_cafeapp.data.repository.CafeRepository
import com.google.firebase.firestore.ListenerRegistration

class CartViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    private val _cart = MutableLiveData<Cart>()
    val cart: LiveData<Cart> = _cart

    private val _totalPrice = MutableLiveData<Double>(0.0)
    val totalPrice: LiveData<Double> = _totalPrice

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var cartListener: ListenerRegistration? = null


    // Observe cart changes in Firestore

    fun observeCart(cartId: String) {
        cartListener = repository.getCart(cartId)
            .addSnapshotListener { snapshot, exception ->

                if (exception != null) {
                    _error.value = exception.message
                    return@addSnapshotListener
                }

                val currentCart = snapshot?.toObject(Cart::class.java)

                if (currentCart != null) {
                    _cart.value = currentCart
                    calculateTotal(currentCart.items)
                }
            }
    }


    // Add item to cart

    fun addItem(cartId: String, item: CartItem) {

        val currentCart = _cart.value ?: Cart(cartId = cartId, userId = "")
        val existingItemIndex = currentCart.items.indexOfFirst { it.itemId == item.itemId }

        if (existingItemIndex >= 0) {
            // Increase quantity if item exists
            val updatedItem = currentCart.items[existingItemIndex].copy(
                quantity = currentCart.items[existingItemIndex].quantity + item.quantity
            )

            currentCart.items = currentCart.items.toMutableList().apply {
                set(existingItemIndex, updatedItem)
            }

        } else {
            // Add new item
            currentCart.items = currentCart.items + item
        }

        repository.updateCart(currentCart)
    }


    // Remove item from cart

    fun removeItem(cartId: String, itemId: String) {
        val currentCart = _cart.value ?: return
        currentCart.items = currentCart.items.filter { it.itemId != itemId }
        repository.updateCart(currentCart)
    }

    // Calculate total

    private fun calculateTotal(items: List<CartItem>) {
        val total = items.sumOf { it.price * it.quantity }
        _totalPrice.value = total
    }

    // Clean up listener when ViewModel destroyed

    override fun onCleared() {
        super.onCleared()
        cartListener?.remove()
    }
}