package com.example.ctec3703_cafeapp.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ctec3703_cafeapp.data.model.Cart
import com.example.ctec3703_cafeapp.data.model.CartItem
import com.example.ctec3703_cafeapp.data.model.MenuItem
import com.example.ctec3703_cafeapp.data.repository.CafeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MenuViewModel(
    private val repository: CafeRepository,
    private val userId: String
) : ViewModel() {

    private val _menuItems = MutableLiveData<List<MenuItem>>(emptyList())
    val menuItems: LiveData<List<MenuItem>> = _menuItems

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    // LiveData for cart updates
    private val _cartUpdated = MutableLiveData<Boolean>(false)
    val cartUpdated: LiveData<Boolean> = _cartUpdated

    // Fetch menu safely
    fun fetchMenuItems() {

        _loading.value = true

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val snapshot = repository.getMenuItems().get().await()
                val items = snapshot.toObjects(MenuItem::class.java)

                _menuItems.postValue(items)
            } catch (e: Exception) {
                _error.postValue(e.message ?: "Failed to load menu")
            } finally {
                _loading.postValue(false)
            }
        }
    }

    // Add item to cart
    fun addItemToCart(menuItem: MenuItem) {

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val cartRef = repository.getCart(userId)
                val cartSnapshot = cartRef.get().await()

                val currentCart = if (cartSnapshot.exists()) {
                    cartSnapshot.toObject(Cart::class.java)!!
                } else {
                    Cart(cartId = userId, userId = userId)
                }

                // Update cart items
                val updatedItems = currentCart.items.toMutableList()
                val existingItem = updatedItems.find { it.itemId == menuItem.itemId }

                if (existingItem != null) {
                    val index = updatedItems.indexOf(existingItem)
                    updatedItems[index] = existingItem.copy(quantity = existingItem.quantity + 1)
                } else {
                    updatedItems.add(CartItem(menuItem.itemId, menuItem.name, menuItem.price, 1))
                }

                val updatedCart = currentCart.copy(items = updatedItems)

                repository.updateCart(updatedCart)
                _cartUpdated.postValue(true)

            } catch (e: Exception) {
                _error.postValue(e.message ?: "Failed to update cart")
            }
        }
    }
}