package com.example.ctec3703_cafeapp.ui.menu

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ctec3703_cafeapp.R
import com.example.ctec3703_cafeapp.data.repository.CafeRepository
import com.example.ctec3703_cafeapp.data.model.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MenuFragment : Fragment(R.layout.fragment_menu) {

    private lateinit var menuViewModel: MenuViewModel
    private lateinit var menuAdapter: MenuAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.menuRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        menuAdapter = MenuAdapter { item -> onMenuItemClicked(item) }
        recyclerView.adapter = menuAdapter

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val repository = CafeRepository(FirebaseFirestore.getInstance())

        menuViewModel = ViewModelProvider(
            this,
            MenuViewModelFactory(repository, userId)
        )[MenuViewModel::class.java]

        // Observe menu items
        menuViewModel.menuItems.observe(viewLifecycleOwner) { items ->
            menuAdapter.submitList(items)
        }

        // Observe cart updates
        menuViewModel.cartUpdated.observe(viewLifecycleOwner) { added ->
            if (added) Toast.makeText(requireContext(), "Item added to cart", Toast.LENGTH_SHORT).show()
        }

        // Observe errors
        menuViewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        menuViewModel.fetchMenuItems()
    }

    private fun onMenuItemClicked(item: MenuItem) {
        menuViewModel.addItemToCart(item)
    }
}