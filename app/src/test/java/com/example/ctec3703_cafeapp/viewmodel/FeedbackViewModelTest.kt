package com.example.ctec3703_cafeapp.ui.feedback

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.ctec3703_cafeapp.data.model.Feedback
import com.example.ctec3703_cafeapp.data.repository.CafeRepository
import com.example.ctec3703_cafeapp.model.TestData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any

class FeedbackViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var mockRepository: CafeRepository
    private lateinit var viewModel: FeedbackViewModel

    private lateinit var feedbackObserver: Observer<Boolean>
    private lateinit var errorObserver: Observer<String?>

    @Before
    fun setup() {

        MockitoAnnotations.openMocks(this)

        mockRepository = mock(CafeRepository::class.java)
        viewModel = FeedbackViewModel(mockRepository)

        feedbackObserver = mock(Observer::class.java) as Observer<Boolean>
        errorObserver = mock(Observer::class.java) as Observer<String?>

        viewModel.feedbackResult.observeForever(feedbackObserver)
        viewModel.error.observeForever(errorObserver)

    }

    @Test
    fun `saveFeedback successful sets feedbackResult to true`() {

        `when`(mockRepository.generateFeedbackId()).thenReturn("F001")

        viewModel.saveFeedback(TestData.testUser.userId, rating = 5, comment = "Great service!")

        // Verify repository called with any Feedback

        verify(mockRepository).submitFeedback(any<Feedback>())

        // Verify LiveData updated

        verify(feedbackObserver).onChanged(true)

    }

    @Test
    fun `saveFeedback repository throws exception sets error`() {

        `when`(mockRepository.generateFeedbackId()).thenReturn("F002")
        doThrow(RuntimeException("Failed to save")).`when`(mockRepository).submitFeedback(any<Feedback>())

        viewModel.saveFeedback(TestData.testUser.userId, rating = 4, comment = "Good")

        verify(errorObserver).onChanged("Failed to save")

    }

}