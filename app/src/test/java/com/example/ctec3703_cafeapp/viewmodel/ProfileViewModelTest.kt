package com.example.ctec3703_cafeapp.ui.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.ctec3703_cafeapp.data.model.Order
import com.example.ctec3703_cafeapp.data.repository.CafeRepository
import com.example.ctec3703_cafeapp.model.TestData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class ProfileViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // Makes LiveData updates synchronous

    private lateinit var mockRepository: CafeRepository
    private lateinit var viewModel: ProfileViewModel

    private lateinit var ordersObserver: Observer<List<Order>>
    private lateinit var errorObserver: Observer<String?>

    private val testUserId = "U001"

    @Before
    fun setup() {

        MockitoAnnotations.openMocks(this)

        mockRepository = mock(CafeRepository::class.java)
        viewModel = ProfileViewModel(mockRepository, testUserId)

        ordersObserver = mock(Observer::class.java) as Observer<List<Order>>
        errorObserver = mock(Observer::class.java) as Observer<String?>

        viewModel.orders.observeForever(ordersObserver)
        viewModel.error.observeForever(errorObserver)

    }

    @Test
    fun `fetchUserOrders sets orders LiveData when snapshot has data`() {

        // Mock Firestore chain

        val mockCollection = mock(CollectionReference::class.java)
        val mockQuery = mock(Query::class.java)
        val mockListenerRegistration = mock(ListenerRegistration::class.java)

        val mockSnapshot = mock(QuerySnapshot::class.java)
        val mockDoc1 = mock(DocumentSnapshot::class.java)
        val mockDoc2 = mock(DocumentSnapshot::class.java)

        val order1 = TestData.testOrder.copy(orderId = "O001", createdAt = 1000L)
        val order2 = TestData.testOrder.copy(orderId = "O002", createdAt = 2000L)

        `when`(mockRepository.getOrders()).thenReturn(mockCollection)
        `when`(mockCollection.whereEqualTo("userId", testUserId)).thenReturn(mockQuery)
        `when`(mockQuery.addSnapshotListener(any())).thenAnswer { invocation ->

            val listener = invocation.arguments[0] as com.google.firebase.firestore.EventListener<QuerySnapshot>

            `when`(mockSnapshot.documents).thenReturn(listOf(mockDoc1, mockDoc2))
            `when`(mockDoc1.toObject(Order::class.java)).thenReturn(order1)
            `when`(mockDoc2.toObject(Order::class.java)).thenReturn(order2)

            listener.onEvent(mockSnapshot, null)
            mockListenerRegistration

        }

        viewModel.fetchUserOrders()

        // Orders should be sorted by createdAt descending

        verify(ordersObserver).onChanged(listOf(order2, order1))

    }

    @Test
    fun `fetchUserOrders sets error LiveData when exception occurs`() {

        val mockCollection = mock(CollectionReference::class.java)
        val mockQuery = mock(Query::class.java)
        val mockListenerRegistration = mock(ListenerRegistration::class.java)

        // Correct exception type

        val exception = FirebaseFirestoreException(
            "Firestore failed",
            FirebaseFirestoreException.Code.ABORTED
        )

        `when`(mockRepository.getOrders()).thenReturn(mockCollection)
        `when`(mockCollection.whereEqualTo("userId", testUserId)).thenReturn(mockQuery)
        `when`(mockQuery.addSnapshotListener(any())).thenAnswer { invocation ->

            val listener = invocation.arguments[0] as com.google.firebase.firestore.EventListener<QuerySnapshot>

            listener.onEvent(null, exception)
            mockListenerRegistration

        }

        viewModel.fetchUserOrders()

        verify(errorObserver).onChanged("Firestore failed")

    }

}