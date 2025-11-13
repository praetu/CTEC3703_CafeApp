package com.example.ctec3703_cafeapp.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.ctec3703_cafeapp.data.model.Cart
import com.example.ctec3703_cafeapp.data.model.CartItem
import com.example.ctec3703_cafeapp.data.model.Order
import com.example.ctec3703_cafeapp.data.repository.CafeRepository
import com.example.ctec3703_cafeapp.model.TestData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.junit.Assert.assertEquals
import org.mockito.kotlin.any

class CartViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var mockRepository: CafeRepository
    private lateinit var cartViewModel: CartViewModel

    private lateinit var cartObserver: Observer<Cart>
    private lateinit var totalObserver: Observer<Double>
    private lateinit var errorObserver: Observer<String?>
    private lateinit var orderSuccessObserver: Observer<Boolean>

    @Before
    fun setup() {

        MockitoAnnotations.openMocks(this)

        mockRepository = mock(CafeRepository::class.java)
        cartViewModel = CartViewModel(mockRepository, TestData.testUser.userId)

        cartObserver = mock(Observer::class.java) as Observer<Cart>
        totalObserver = mock(Observer::class.java) as Observer<Double>
        errorObserver = mock(Observer::class.java) as Observer<String?>
        orderSuccessObserver = mock(Observer::class.java) as Observer<Boolean>

        cartViewModel.cart.observeForever(cartObserver)
        cartViewModel.total.observeForever(totalObserver)
        cartViewModel.error.observeForever(errorObserver)
        cartViewModel.orderSuccess.observeForever(orderSuccessObserver)

    }

    // Helper to set private _cart LiveData
    private fun setCartForTesting(cart: Cart) {

        val field = cartViewModel.javaClass.getDeclaredField("_cart")
        field.isAccessible = true

        val mutableCart = field.get(cartViewModel) as androidx.lifecycle.MutableLiveData<Cart>
        mutableCart.value = cart

    }

    @Test
    fun `increaseQuantity updates item quantity`() {

        val cart = TestData.testCart.copy(items = listOf(TestData.testCartItem))
        setCartForTesting(cart)

        cartViewModel.increaseQuantity(TestData.testCartItem)

        val updatedCart = cartViewModel.cart.value!!

        assertEquals(2, updatedCart.items[0].quantity)
        verify(mockRepository).updateCart(updatedCart)
        assertEquals(4.0, cartViewModel.total.value, 0.001)

    }

    @Test
    fun `decreaseQuantity removes item if quantity becomes zero`() {

        val cart = TestData.testCart.copy(items = listOf(TestData.testCartItem))
        setCartForTesting(cart)

        cartViewModel.decreaseQuantity(TestData.testCartItem)

        val updatedCart = cartViewModel.cart.value!!

        assertEquals(0, updatedCart.items.size)
        verify(mockRepository).updateCart(updatedCart)
        assertEquals(0.0, cartViewModel.total.value, 0.001)

    }

    @Test
    fun `checkout creates order and resets cart`() {

        val cartItem = TestData.testCartItem.copy(quantity = 2)
        val cart = TestData.testCart.copy(items = listOf(cartItem))
        setCartForTesting(cart)

        `when`(mockRepository.generateOrderId()).thenReturn("O001")

        cartViewModel.checkout()

        val freshCart = cartViewModel.cart.value!!

        assertEquals(0, freshCart.items.size)
        assertEquals(false, freshCart.orderStatus)
        verify(mockRepository).createOrder(any<Order>())
        verify(orderSuccessObserver).onChanged(true)

    }

    @Test
    fun `checkout sets error if cart empty`() {

        val emptyCart = TestData.testCart.copy(items = emptyList())
        setCartForTesting(emptyCart)

        cartViewModel.checkout()

        verify(errorObserver).onChanged("Your cart is empty.")

    }

}