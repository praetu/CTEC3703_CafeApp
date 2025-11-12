package com.example.ctec3703_cafeapp.ui.cart

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ctec3703_cafeapp.R
import com.example.ctec3703_cafeapp.data.model.CartItem
import com.example.ctec3703_cafeapp.data.repository.CafeRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CartFragment : Fragment(R.layout.fragment_cart) {

    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.cartRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val totalTextView = view.findViewById<TextView>(R.id.totalPriceText)
        val checkoutButton = view.findViewById<Button>(R.id.checkoutButton)

        val repository = CafeRepository(FirebaseFirestore.getInstance())
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return


        // Initialize ViewModel
        cartViewModel = ViewModelProvider(
            this,
            CartViewModelFactory(repository, userId)
        )[CartViewModel::class.java]

        // Initialize Adapter with increase/decrease callbacks
        cartAdapter = CartAdapter(
            onIncrease = { item: CartItem -> cartViewModel.increaseQuantity(item) },
            onDecrease = { item: CartItem -> cartViewModel.decreaseQuantity(item) }
        )

        recyclerView.adapter = cartAdapter

        // Observe cart LiveData
        cartViewModel.cart.observe(viewLifecycleOwner) { cart ->
            cartAdapter.submitList(cart.items)
        }

        cartViewModel.total.observe(viewLifecycleOwner) { total ->
            totalTextView.text = "£%.2f".format(total)
        }

        checkoutButton.setOnClickListener {

            val currentCart = cartViewModel.cart.value

            if (currentCart != null && currentCart.items.isNotEmpty()) {
                cartViewModel.checkout()
                showCheckoutSuccess()
            }
        }

        // Fetch current cart
        cartViewModel.fetchCart()
    }

    private fun showCheckoutSuccess() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_checkout_success, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.show()

        dialogView.postDelayed({
            dialog.dismiss()
            findNavController().navigate(R.id.profileFragment)
        }, 1500)
    }
}