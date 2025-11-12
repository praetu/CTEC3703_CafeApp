package com.example.ctec3703_cafeapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ctec3703_cafeapp.data.model.User
import com.example.ctec3703_cafeapp.data.model.states.AuthState
import com.example.ctec3703_cafeapp.data.repository.CafeRepository
import com.example.ctec3703_cafeapp.ui.main.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(
    private val repository: CafeRepository,
    private val mainViewModel: MainViewModel,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>(AuthState.Idle)
    val authState: LiveData<AuthState> = _authState

    fun register(email: String, password: String, name: String) {

        _authState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val firebaseUser: FirebaseUser? = auth.currentUser
                    val user = User(
                        userId = firebaseUser?.uid ?: "",
                        name = name,
                        email = firebaseUser?.email ?: ""
                    )

                    repository.addUser(user)
                    _authState.value = AuthState.Success(user)

                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Registration failed")
                }
            }
    }

    fun login(email: String, password: String) {

        _authState.value = AuthState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val firebaseUser: FirebaseUser? = auth.currentUser

                    repository.getUser(firebaseUser?.uid ?: "")
                        .get()
                        .addOnSuccessListener { doc ->

                            val user = doc.toObject(User::class.java)

                            if (user != null) {
                                _authState.value = AuthState.Success(user)
                                mainViewModel.setCurrentUser(user)
                            } else {
                                _authState.value = AuthState.Error("User not found in database")
                            }

                        }
                        .addOnFailureListener { e ->
                            _authState.value = AuthState.Error(e.message ?: "Error fetching user")
                        }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Login failed")
                }
            }
    }
}