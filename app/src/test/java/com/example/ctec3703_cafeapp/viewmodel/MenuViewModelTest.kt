package com.example.ctec3703_cafeapp.ui.menu

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.ctec3703_cafeapp.data.model.Cart
import com.example.ctec3703_cafeapp.data.model.CartItem
import com.example.ctec3703_cafeapp.data.model.MenuItem
import com.example.ctec3703_cafeapp.data.repository.CafeRepository
import com.example.ctec3703_cafeapp.model.TestData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.junit.Assert.assertEquals

class MenuViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // LiveData synchronous

    private lateinit var mockRepository: CafeRepository
    private lateinit var viewModel: MenuViewModel

    private lateinit var menuObserver: Observer<List<MenuItem>>
    private lateinit var cartObserver: Observer<Cart>
    private lateinit var cartUpdatedObserver: Observer<Boolean>
    private lateinit var errorObserver: Observer<String?>

    @Before
    fun setup() {

        MockitoAnnotations.openMocks(this)

        mockRepository = mock(CafeRepository::class.java)
        viewModel = MenuViewModel(mockRepository, TestData.testUser.userId)

        menuObserver = mock(Observer::class.java) as Observer<List<MenuItem>>
        cartObserver = mock(Observer::class.java) as Observer<Cart>
        cartUpdatedObserver = mock(Observer::class.java) as Observer<Boolean>
        errorObserver = mock(Observer::class.java) as Observer<String?>

        viewModel.menuItems.observeForever(menuObserver)
        viewModel.cart.observeForever(cartObserver)
        viewModel.cartUpdated.observeForever(cartUpdatedObserver)
        viewModel.error.observeForever(errorObserver)

    }

    @Test
    fun `addItemToCart adds new item if not exist`() {

        val initialCart = TestData.testCart.copy(items = emptyList())

        // Access private _cart LiveData using reflection

        val field = viewModel.javaClass.getDeclaredField("_cart")
        field.isAccessible = true

        val mutableCart = field.get(viewModel) as androidx.lifecycle.MutableLiveData<Cart>
        mutableCart.value = initialCart

        viewModel.addItemToCart(TestData.testMenuItem)

        val updatedCart = viewModel.cart.value!!

        assertEquals(1, updatedCart.items.size)
        assertEquals(TestData.testMenuItem.itemId, updatedCart.items[0].itemId)
        assertEquals(1, updatedCart.items[0].quantity)

        verify(mockRepository).updateCart(updatedCart)
        verify(cartUpdatedObserver).onChanged(true)

    }

    @Test
    fun `addItemToCart increases quantity if item exists`() {

        val existingItem = TestData.testCartItem
        val initialCart = TestData.testCart.copy(items = listOf(existingItem))

        val field = viewModel.javaClass.getDeclaredField("_cart")
        field.isAccessible = true

        val mutableCart = field.get(viewModel) as androidx.lifecycle.MutableLiveData<Cart>
        mutableCart.value = initialCart

        viewModel.addItemToCart(TestData.testMenuItem)

        val updatedCart = viewModel.cart.value!!

        assertEquals(1, updatedCart.items.size)
        assertEquals(2, updatedCart.items[0].quantity) // Quantity increased
        verify(mockRepository).updateCart(updatedCart)
        verify(cartUpdatedObserver).onChanged(true)

    }
}