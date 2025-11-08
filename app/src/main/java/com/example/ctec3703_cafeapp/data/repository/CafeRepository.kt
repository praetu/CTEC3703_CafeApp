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

    fun createCart(cart: Cart) {
        cartsCollection.document(cart.cartId).set(cart)
    }

    fun updateCart(cart: Cart) {
        cartsCollection.document(cart.cartId).set(cart)
    }

    // Order Functions

    fun createOrder(order: Order) {
        ordersCollection.document(order.orderId).set(order)
    }

    fun getOrder(orderId: String) = ordersCollection.document(orderId)

    // Feedback Functions

    fun submitFeedback(feedback: Feedback) {
        feedbackCollection.document(feedback.feedbackId).set(feedback)
    }

    fun getFeedback(feedbackId: String) = feedbackCollection.document(feedbackId)
}