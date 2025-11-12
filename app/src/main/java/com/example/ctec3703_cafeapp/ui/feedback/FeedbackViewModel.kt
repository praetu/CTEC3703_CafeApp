package com.example.ctec3703_cafeapp.ui.feedback

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ctec3703_cafeapp.data.model.Feedback
import com.example.ctec3703_cafeapp.data.repository.CafeRepository

class FeedbackViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    private val _feedbackResult = MutableLiveData<Boolean>()
    val feedbackResult: LiveData<Boolean> = _feedbackResult

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun saveFeedback(userId: String, rating: Int, comment: String) {

        // Generate an ID for the feedback

        val feedbackId = repository.generateFeedbackId()

        val feedback = Feedback(
            feedbackId = feedbackId,
            userId = userId,
            rating = rating,
            review = comment,
            createdAt = System.currentTimeMillis()
        )

        try {
            repository.submitFeedback(feedback)
            _feedbackResult.value = true
        } catch (e: Exception) {
            _error.value = e.message
        }
    }
}