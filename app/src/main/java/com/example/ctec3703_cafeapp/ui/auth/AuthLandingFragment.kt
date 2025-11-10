package com.example.ctec3703_cafeapp.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ctec3703_cafeapp.R
import com.google.android.material.button.MaterialButton

class AuthLandingFragment : Fragment(R.layout.fragment_auth_landing) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginButton = view.findViewById<MaterialButton>(R.id.loginButton)
        val registerButton = view.findViewById<MaterialButton>(R.id.registerButton)

        loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_authLanding_to_login)
        }

        registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_authLanding_to_register)
        }
    }
}