package com.example.ctec3703_cafeapp.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ctec3703_cafeapp.R
import com.example.ctec3703_cafeapp.data.repository.CafeRepository
import com.example.ctec3703_cafeapp.data.model.states.AuthState
import com.example.ctec3703_cafeapp.ui.main.MainViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var authViewModel: AuthViewModel
    private val repository = CafeRepository(FirebaseFirestore.getInstance())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val emailInput = view.findViewById<TextInputEditText>(R.id.emailInput)
        val passwordInput = view.findViewById<TextInputEditText>(R.id.passwordInput)
        val loginButton = view.findViewById<MaterialButton>(R.id.loginButton)

        // Shared MainViewModel

        val mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        // AuthViewModel with Factory

        val factory = AuthViewModelFactory(
            repository,
            mainViewModel,
            FirebaseAuth.getInstance()
        )

        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            authViewModel.login(email, password)
        }

        // Observe auth state

        authViewModel.authState.observe(viewLifecycleOwner) { state ->

            when (state) {
                is AuthState.Success -> findNavController().navigate(R.id.menuFragment)
                is AuthState.Error -> {/* TODO: Show error message */}
                else -> {}
            }

        }
    }
}