package com.example.ctec3703_cafeapp.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ctec3703_cafeapp.R
import com.example.ctec3703_cafeapp.data.repository.CafeRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var profileAdapter: ProfileAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val nameTextView = view.findViewById<TextView>(R.id.userNameText)
        val logoutButton = view.findViewById<TextView>(R.id.logoutButton)
        val ordersRecyclerView = view.findViewById<RecyclerView>(R.id.ordersRecyclerView)

        // Get current user

        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userId = currentUser?.uid ?: return

        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                val name = doc.getString("name") ?: "Your Profile"
                nameTextView.text = name
            }
            .addOnFailureListener {
                nameTextView.text = "Your Profile"
            }

        // Setup repository and ViewModel

        val repository = CafeRepository(FirebaseFirestore.getInstance())

        profileViewModel = ViewModelProvider(
            this,
            ProfileViewModelFactory(repository, userId)
        )[ProfileViewModel::class.java]

        // Setup RecyclerView

        profileAdapter = ProfileAdapter()
        ordersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        ordersRecyclerView.adapter = profileAdapter

        // Observe orders

        profileViewModel.orders.observe(viewLifecycleOwner) { orders ->
            profileAdapter.submitList(orders)
        }

        // Observe errors (optional)

        profileViewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                // Optional: show toast
            }
        }


        // Fetch orders
        profileViewModel.fetchUserOrders()

        // Logout button click

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_profile_to_auth) // Navigate to login or starting screen
        }
    }
}