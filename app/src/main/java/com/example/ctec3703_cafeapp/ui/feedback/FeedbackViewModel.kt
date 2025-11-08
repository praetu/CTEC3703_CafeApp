package com.example.ctec3703_cafeapp.ui.feedback

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ctec3703_cafeapp.data.model.Feedback
import com.example.ctec3703_cafeapp.data.model.FeedbackState
import com.example.ctec3703_cafeapp.data.repository.CafeRepository

class FeedbackViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    private val _feedbackState = MutableLiveData<FeedbackState>(FeedbackState.Idle)
    val feedbackState: LiveData<FeedbackState> = _feedbackState

    // Submit new feedback

    fun submitFeedback(feedback: Feedback) {

        _feedbackState.value = FeedbackState.Loading

        try {
            repository.submitFeedback(feedback)
            _feedbackState.value = FeedbackState.Success
        } catch (e: Exception) {
            _feedbackState.value = FeedbackState.Error(e.message ?: "Failed to submit feedback")
        }

    }
}