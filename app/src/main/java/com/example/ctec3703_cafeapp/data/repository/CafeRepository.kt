package com.example.ctec3703_cafeapp.data.repository

import com.example.ctec3703_cafeapp.data.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.CollectionReference

class CafeRepository(private val db: FirebaseFirestore) {

    // Collections
    private val usersCollection: CollectionReference = db.collection("users")
    private val menuCollection: CollectionReference = db.collection("menuItems")
    private val cartsCollection: CollectionReference = db.collection("carts")
    private val ordersCollection: CollectionReference = db.collection("orders")
    private val feedbackCollection: CollectionReference = db.collection("feedback")

    // User Functions

    fun getUser(userId: String) = usersCollection.document(userId)

    fun addUser(user: User) {
        usersCollection.document(user.userId).set(user)
    }

    // Menu Functions

    fun getMenuItems() = menuCollection

    fun getMenuItem(itemId: String) = menuCollection.document(itemId)

    // Cart Functions

    fun getCart(cartId: String) = cartsCollection.document(cartId)

    fun updateCart(cart: Cart) {
        cartsCollection.document(cart.cartId).set(cart)
    }

    // Add an item to a cart
    fun addItemToCart(cartId: String, newItem: CartItem) {

        val cartRef = cartsCollection.document(cartId)

        db.runTransaction { transaction ->

            val snapshot = transaction.get(cartRef)
            val cart = if (snapshot.exists()) {
                snapshot.toObject(Cart::class.java) ?: Cart(cartId = cartId)
            } else {
                Cart(cartId = cartId)
            }

            // Make items mutable
            val updatedItems = cart.items.toMutableList()

            // Check if item already exists in cart
            val existingIndex = updatedItems.indexOfFirst { it.itemId == newItem.itemId }

            if (existingIndex != -1) {
                val existingItem = updatedItems[existingIndex]
                updatedItems[existingIndex] = existingItem.copy(quantity = existingItem.quantity + newItem.quantity)
            } else {
                updatedItems.add(newItem)
            }

            // Update cart
            val updatedCart = cart.copy(
                items = updatedItems,
                updatedAt = System.currentTimeMillis()
            )

            transaction.set(cartRef, updatedCart)
        }
    }

    // Optional: observe cart changes
    fun observeCart(cartId: String, listener: (Cart?) -> Unit) {

        cartsCollection.document(cartId)
            .addSnapshotListener { snapshot, _ ->
                val cart = snapshot?.toObject(Cart::class.java)
                listener(cart)
            }
    }

    // Order Functions

    fun createOrder(order: Order) {
        ordersCollection.document(order.orderId).set(order)
    }

    fun getOrder(orderId: String) = ordersCollection.document(orderId)

    fun getOrders() = ordersCollection

    // Feedback Functions

    fun submitFeedback(feedback: Feedback) {
        feedbackCollection.document(feedback.feedbackId).set(feedback)
    }
}