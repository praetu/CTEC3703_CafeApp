package com.example.ctec3703_cafeapp.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ctec3703_cafeapp.data.model.Cart
import com.example.ctec3703_cafeapp.data.model.CartItem
import com.example.ctec3703_cafeapp.data.repository.CafeRepository

class CartViewModel(
    private val repository: CafeRepository,
    private val userId: String
) : ViewModel() {

    private val _cart = MutableLiveData<Cart>()
    val cart: LiveData<Cart> = _cart

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _total = MutableLiveData<Double>(0.0)
    val total: LiveData<Double> = _total

    fun fetchCart() {

        repository.getCart(userId).get()
            .addOnSuccessListener { doc ->

                val currentCart = doc.toObject(Cart::class.java)
                if (currentCart != null) _cart.value = currentCart
                else _cart.value = Cart(cartId = userId, userId = userId)

                calculateTotal()

            }
            .addOnFailureListener { e ->
                _error.value = e.message
            }
    }

    fun increaseQuantity(item: CartItem) {
        updateItemQuantity(item, item.quantity + 1)
    }

    fun decreaseQuantity(item: CartItem) {

        val newQty = item.quantity - 1

        if (newQty <= 0) removeItem(item)
        else updateItemQuantity(item, newQty)
    }

    private fun updateItemQuantity(item: CartItem, quantity: Int) {

        val currentCart = _cart.value ?: return
        val updatedItems = currentCart.items.map {
            if (it.itemId == item.itemId) it.copy(quantity = quantity) else it
        }
        val updatedCart = currentCart.copy(items = updatedItems)

        _cart.value = updatedCart
        repository.updateCart(updatedCart)
        calculateTotal()
    }

    private fun removeItem(item: CartItem) {

        val currentCart = _cart.value ?: return
        val updatedItems = currentCart.items.filter { it.itemId != item.itemId }
        val updatedCart = currentCart.copy(items = updatedItems)

        _cart.value = updatedCart
        repository.updateCart(updatedCart)
        calculateTotal()
    }

    private fun calculateTotal() {
        val currentCart = _cart.value
        val sum = currentCart?.items?.sumOf { it.price * it.quantity } ?: 0.0
        _total.value = sum
    }
}