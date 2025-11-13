package com.example.ctec3703_cafeapp.model

import com.example.ctec3703_cafeapp.data.model.Cart
import com.example.ctec3703_cafeapp.data.model.CartItem
import com.example.ctec3703_cafeapp.data.model.Feedback
import com.example.ctec3703_cafeapp.data.model.MenuItem
import com.example.ctec3703_cafeapp.data.model.Order
import com.example.ctec3703_cafeapp.data.model.User

object TestData {

    val testUser = User(
        userId = "U001",
        name = "Alice",
        email = "alice@test.com",
        role = "customer",
        createdAt = System.currentTimeMillis()
    )

    val testMenuItem = MenuItem(
        itemId = "M001",
        name = "Coffee",
        price = 2.0,
        category = "Drinks",
        imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fmedia.istockphoto.com%2Fphotos%2Fcoffee-espresso-picture-id136625069%3Fk%3D6%26m%3D136625069%26s%3D612x612%26w%3D0%26h%3DkLTJbSoNwsH1ZAXkSqO7z8XKBziB1a8lR07Lktwzuyk%3D&f=1&nofb=1&ipt=ee7f45c5cc4589bb5051619a72d04eebe08a85959ec86d60869ffff93e8eb013"
    )

    val testCartItem = CartItem(
        itemId = "M001",
        name = "Coffee",
        price = 2.0,
        quantity = 1
    )

    val testCart = Cart(
        cartId = "C001",
        userId = "U001",
        items = listOf(testCartItem),
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),
        orderStatus = false
    )

    val testOrder = Order(
        orderId = "O001",
        userId = "U001",
        cartId = "C001",
        items = listOf(testCartItem),
        totalPrice = 2.0,
        status = "Completed",
        paymentStatus = "Paid",
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )

    val testFeedback = Feedback(
        feedbackId = "F001",
        userId = "U001",
        rating = 5,
        review = "Good Stuff!",
        createdAt = System.currentTimeMillis()
    )
}