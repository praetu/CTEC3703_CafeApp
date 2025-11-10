package com.example.ctec3703_cafeapp.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ctec3703_cafeapp.R
import com.example.ctec3703_cafeapp.data.model.states.AuthState
import com.example.ctec3703_cafeapp.data.repository.CafeRepository
import com.example.ctec3703_cafeapp.ui.main.MainViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var authViewModel: AuthViewModel
    private val repository = CafeRepository(FirebaseFirestore.getInstance())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val nameInput = view.findViewById<TextInputEditText>(R.id.nameInput)
        val emailInput = view.findViewById<TextInputEditText>(R.id.emailInput)
        val passwordInput = view.findViewById<TextInputEditText>(R.id.passwordInput)
        val registerButton = view.findViewById<MaterialButton>(R.id.registerButton)

        // Get shared MainViewModel from activity

        val mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        // Create factory with repository and mainViewModel

        val factory = AuthViewModelFactory(
            repository,
            mainViewModel,
            FirebaseAuth.getInstance()
        )

        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        registerButton.setOnClickListener {

            val name = nameInput.text.toString()
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            // Simple validation

            if (name.isBlank() || email.isBlank() || password.isBlank()) return@setOnClickListener

            authViewModel.register(email, password, name)
        }

        // Observe auth state

        authViewModel.authState.observe(viewLifecycleOwner) { state ->

            when (state) {
                is AuthState.Success -> findNavController().navigate(R.id.authLandingFragment)
                is AuthState.Error -> {/* show error */}
                else -> {}
            }

        }
    }
}