package com.example.ctec3703_cafeapp.data.repository

import com.example.ctec3703_cafeapp.data.model.Cart
import com.example.ctec3703_cafeapp.data.model.Feedback
import com.example.ctec3703_cafeapp.data.model.MenuItem
import com.example.ctec3703_cafeapp.data.model.Order
import com.example.ctec3703_cafeapp.data.model.User
import com.example.ctec3703_cafeapp.model.TestData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.junit.Assert.assertEquals

class CafeRepositoryTest {

    private lateinit var mockFirestore: FirebaseFirestore
    private lateinit var mockCollection: CollectionReference
    private lateinit var mockDocument: DocumentReference
    private lateinit var repository: CafeRepository

    @Before
    fun setUp() {

        MockitoAnnotations.openMocks(this)

        mockFirestore = mock(FirebaseFirestore::class.java)
        mockCollection = mock(CollectionReference::class.java)
        mockDocument = mock(DocumentReference::class.java)

        // Mock all collections

        `when`(mockFirestore.collection("users")).thenReturn(mockCollection)
        `when`(mockFirestore.collection("menuItems")).thenReturn(mockCollection)
        `when`(mockFirestore.collection("carts")).thenReturn(mockCollection)
        `when`(mockFirestore.collection("orders")).thenReturn(mockCollection)
        `when`(mockFirestore.collection("feedback")).thenReturn(mockCollection)
        `when`(mockCollection.document(anyString())).thenReturn(mockDocument)
        `when`(mockCollection.document()).thenReturn(mockDocument) // for generateOrderId/FeedbackId

        `when`(mockDocument.id).thenReturn("generatedId")

        repository = CafeRepository(mockFirestore)

    }


    // User Tests

    @Test
    fun `addUser stores user in Firestore`() {

        repository.addUser(TestData.testUser)

        verify(mockCollection).document(TestData.testUser.userId)
        verify(mockDocument).set(TestData.testUser)

    }

    @Test
    fun `getUser returns correct document reference`() {

        repository.getUser("U001")

        verify(mockCollection).document("U001")

    }


    // Menu Tests

    @Test
    fun `getMenuItems returns collection reference`() {

        val result = repository.getMenuItems()

        assertEquals(mockCollection, result)

    }


    // Cart Tests

    @Test
    fun `getCart returns correct document reference`() {

        repository.getCart("C001")

        verify(mockCollection).document("C001")

    }

    @Test
    fun `updateCart sets cart document`() {

        repository.updateCart(TestData.testCart)

        verify(mockCollection).document(TestData.testCart.cartId)
        verify(mockDocument).set(TestData.testCart)

    }


    // Order Tests

    @Test
    fun `createOrder sets order document`() {

        repository.createOrder(TestData.testOrder)

        verify(mockCollection).document(TestData.testOrder.orderId)
        verify(mockDocument).set(TestData.testOrder)

    }

    @Test
    fun `getOrders returns collection reference`() {

        val result = repository.getOrders()

        assertEquals(mockCollection, result)

    }

    @Test
    fun `generateOrderId returns document id`() {

        val id = repository.generateOrderId()

        assertEquals("generatedId", id)

    }


    // Feedback Tests

    @Test
    fun `submitFeedback sets feedback document`() {

        repository.submitFeedback(TestData.testFeedback)

        verify(mockCollection).document(TestData.testFeedback.feedbackId)
        verify(mockDocument).set(TestData.testFeedback)

    }

    @Test
    fun `generateFeedbackId returns document id`() {

        val id = repository.generateFeedbackId()

        assertEquals("generatedId", id)

    }
}