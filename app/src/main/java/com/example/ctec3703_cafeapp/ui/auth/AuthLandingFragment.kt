package com.example.ctec3703_cafeapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ctec3703_cafeapp.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class AuthLandingFragment : Fragment(R.layout.fragment_auth_landing) {

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if user is already logged in
//        firebaseAuth.currentUser?.let {
//
//            findNavController().navigate(R.id.menuFragment)
//
//            return
//        }

        val loginButton = view.findViewById<MaterialButton>(R.id.loginButton)
        val registerButton = view.findViewById<MaterialButton>(R.id.registerButton)

        loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_auth_landing_to_login)
        }

        registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_auth_landing_to_register)
        }
    }
}