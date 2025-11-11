package com.example.ctec3703_cafeapp.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ctec3703_cafeapp.data.model.Cart
import com.example.ctec3703_cafeapp.data.model.CartItem
import com.example.ctec3703_cafeapp.data.model.MenuItem
import com.example.ctec3703_cafeapp.data.repository.CafeRepository
import com.google.firebase.firestore.ListenerRegistration

class MenuViewModel(
    private val repository: CafeRepository,
    private val userId: String
) : ViewModel() {

    // Menu
    private val _menuItems = MutableLiveData<List<MenuItem>>(emptyList())
    val menuItems: LiveData<List<MenuItem>> = _menuItems

    private var menuListener: ListenerRegistration? = null

    // Cart
    private val _cart = MutableLiveData<Cart>()
    val cart: LiveData<Cart> = _cart

    private val _cartUpdated = MutableLiveData<Boolean>()
    val cartUpdated: LiveData<Boolean> = _cartUpdated

    private var cartListener: ListenerRegistration? = null

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    // Fetch menu items
    fun fetchMenuItems() {

        menuListener = repository.getMenuItems()
            .addSnapshotListener { snapshot, exception ->

                if (exception != null) {
                    _error.value = exception.message
                    return@addSnapshotListener
                }

                _menuItems.value = snapshot?.documents?.mapNotNull { it.toObject(MenuItem::class.java) } ?: emptyList()
            }
    }

    // Fetch cart for current user
    fun fetchCart() {

        cartListener = repository.getCart(userId)
            .addSnapshotListener { snapshot, exception ->

                if (exception != null) {
                    _error.value = exception.message
                    return@addSnapshotListener
                }

                val currentCart = snapshot?.toObject(Cart::class.java) ?: Cart(cartId = userId, userId = userId)
                _cart.value = currentCart
            }
    }

    // Add item to cart
    fun addItemToCart(item: MenuItem) {

        val currentCart = _cart.value ?: Cart(cartId = userId, userId = userId)
        val updatedItems = currentCart.items.toMutableList()

        val existing = updatedItems.indexOfFirst { it.itemId == item.itemId }

        if (existing >= 0) {
            val updatedItem = updatedItems[existing].copy(quantity = updatedItems[existing].quantity + 1)
            updatedItems[existing] = updatedItem
        } else {
            updatedItems.add(CartItem(itemId = item.itemId, name = item.name, price = item.price, quantity = 1))
        }

        val updatedCart = currentCart.copy(items = updatedItems)

        repository.updateCart(updatedCart)
        _cart.value = updatedCart
        _cartUpdated.value = true
    }

    override fun onCleared() {
        super.onCleared()
        menuListener?.remove()
        cartListener?.remove()
    }
}