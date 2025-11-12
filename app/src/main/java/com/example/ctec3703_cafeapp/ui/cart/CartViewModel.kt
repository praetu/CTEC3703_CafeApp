package com.example.ctec3703_cafeapp.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ctec3703_cafeapp.data.model.Cart
import com.example.ctec3703_cafeapp.data.model.CartItem
import com.example.ctec3703_cafeapp.data.model.Order
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

    private val _orderSuccess = MutableLiveData<Boolean>()
    val orderSuccess: LiveData<Boolean> = _orderSuccess

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

    fun checkout() {

        val currentCart = _cart.value ?: return

        if (currentCart.items.isEmpty()) {
            _error.value = "Your cart is empty."
            return
        }

        // Mark cart as ordered
        val orderedCart = currentCart.copy(orderStatus = true)
        repository.updateCart(orderedCart)

        // Create new order
        val newOrderId = repository.generateOrderId()
        val order = Order(
            orderId = newOrderId,
            userId = currentCart.userId,
            cartId = currentCart.cartId,
            items = currentCart.items,
            totalPrice = currentCart.items.sumOf { it.price * it.quantity },
            status = "Completed",
            paymentStatus = "Paid"
        )

        repository.createOrder(order)

        // Create a fresh empty cart for user
        val freshCart = Cart(
            cartId = userId,
            userId = userId,
            items = emptyList(),
            orderStatus = false
        )

        repository.updateCart(freshCart)
        _cart.value = freshCart

        // Notify UI
        _orderSuccess.value = true
    }
}