package com.example.ctec3703_cafeapp.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.ctec3703_cafeapp.data.model.states.AuthState
import com.example.ctec3703_cafeapp.data.repository.CafeRepository
import com.example.ctec3703_cafeapp.model.TestData
import com.example.ctec3703_cafeapp.ui.main.MainViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

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

        val mockTask = mock(Task::class.java) as Task<Void>

        `when`(mockAuth.createUserWithEmailAndPassword(email, password)).thenAnswer {
            val listener = it.arguments[2] as? com.google.android.gms.tasks.OnCompleteListener<Void>
            listener?.onComplete(mockTask)
            mockTask
        }

        `when`(mockTask.isSuccessful).thenReturn(true)

        viewModel.register(email, password, name)

        verify(mockRepository).addUser(TestData.testUser)
        verify(authStateObserver).onChanged(AuthState.Loading)
        verify(authStateObserver).onChanged(AuthState.Success(TestData.testUser))

    }

    @Test
    fun `login successful updates authState and sets current user`() {

        val email = TestData.testUser.email
        val password = "password"

        val mockSignInTask = mock(Task::class.java) as Task<Void>

        `when`(mockAuth.signInWithEmailAndPassword(email, password)).thenAnswer {
            val listener = it.arguments[2] as? com.google.android.gms.tasks.OnCompleteListener<Void>
            listener?.onComplete(mockSignInTask)
            mockSignInTask
        }

        `when`(mockSignInTask.isSuccessful).thenReturn(true)

        val mockDocRef = mock(com.google.firebase.firestore.DocumentReference::class.java)
        val mockDocSnapshot = mock(com.google.firebase.firestore.DocumentSnapshot::class.java)
        val mockGetTask = mock(Task::class.java) as Task<com.google.firebase.firestore.DocumentSnapshot>

        `when`(mockRepository.getUser(TestData.testUser.userId)).thenReturn(mockDocRef)
        `when`(mockDocRef.get()).thenReturn(mockGetTask)
        `when`(mockGetTask.isSuccessful).thenReturn(true)
        `when`(mockGetTask.result).thenReturn(mockDocSnapshot)
        `when`(mockDocSnapshot.toObject(com.example.ctec3703_cafeapp.data.model.User::class.java))
            .thenReturn(TestData.testUser)

        viewModel.login(email, password)

        verify(mockMainViewModel).setCurrentUser(TestData.testUser)
        verify(authStateObserver).onChanged(AuthState.Loading)
        verify(authStateObserver).onChanged(AuthState.Success(TestData.testUser))

    }

}