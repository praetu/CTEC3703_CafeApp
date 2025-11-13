package com.example.ctec3703_cafeapp.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.ctec3703_cafeapp.data.model.states.AuthState
import com.example.ctec3703_cafeapp.data.repository.CafeRepository
import com.example.ctec3703_cafeapp.model.TestData
import com.example.ctec3703_cafeapp.ui.main.MainViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argThat


class AuthViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // Makes LiveData synchronous

    private lateinit var mockAuth: FirebaseAuth
    private lateinit var mockRepository: CafeRepository
    private lateinit var mockMainViewModel: MainViewModel
    private lateinit var mockUser: FirebaseUser
    private lateinit var viewModel: AuthViewModel
    private lateinit var authStateObserver: Observer<AuthState>

    @Before
    fun setup() {

        MockitoAnnotations.openMocks(this)

        mockAuth = mock(FirebaseAuth::class.java)
        mockRepository = mock(CafeRepository::class.java)
        mockMainViewModel = mock(MainViewModel::class.java)
        mockUser = mock(FirebaseUser::class.java)

        `when`(mockAuth.currentUser).thenReturn(mockUser)
        `when`(mockUser.uid).thenReturn(TestData.testUser.userId)
        `when`(mockUser.email).thenReturn(TestData.testUser.email)

        viewModel = AuthViewModel(mockRepository, mockMainViewModel, mockAuth)

        authStateObserver = mock(Observer::class.java) as Observer<AuthState>
        viewModel.authState.observeForever(authStateObserver)

    }

    @Test
    fun `register successful updates authState with Success`() {

        val email = TestData.testUser.email
        val password = "password"
        val name = TestData.testUser.name

        val mockTask = mock(Task::class.java) as Task<AuthResult>

        `when`(mockAuth.createUserWithEmailAndPassword(email, password)).thenReturn(mockTask)
        `when`(mockTask.isSuccessful).thenReturn(true)
        `when`(mockTask.addOnCompleteListener(any())).thenAnswer { invocation ->
            val listener = invocation.arguments[0] as OnCompleteListener<AuthResult>
            listener.onComplete(mockTask)
            mockTask
        }

        viewModel.register(email, password, name)

        // Verify addUser ignoring createdAt

        verify(mockRepository).addUser(argThat { user ->
            user.userId == TestData.testUser.userId &&
                    user.name == TestData.testUser.name &&
                    user.email == TestData.testUser.email &&
                    user.role == "customer"
        })

        // Verify LiveData updates

        verify(authStateObserver).onChanged(AuthState.Loading)
        verify(authStateObserver).onChanged(argThat { state ->
            state is AuthState.Success && state.user.userId == TestData.testUser.userId
        })
    }

}