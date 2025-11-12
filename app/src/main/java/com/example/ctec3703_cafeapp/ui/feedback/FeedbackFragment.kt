package com.example.ctec3703_cafeapp.ui.feedback

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ctec3703_cafeapp.R
import com.example.ctec3703_cafeapp.data.repository.CafeRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FeedbackFragment : Fragment(R.layout.fragment_feedback) {

    private lateinit var ratingBar: RatingBar
    private lateinit var feedbackEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var feedbackViewModel: FeedbackViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        ratingBar = view.findViewById(R.id.ratingBar)
        feedbackEditText = view.findViewById(R.id.feedbackEditText)
        submitButton = view.findViewById(R.id.submitFeedbackButton)

        // Initialize ViewModel with Factory

        val repository = CafeRepository(FirebaseFirestore.getInstance())
        val factory = FeedbackViewModelFactory(repository)
        feedbackViewModel = ViewModelProvider(this, factory)[FeedbackViewModel::class.java]

        // Observe submission result

        feedbackViewModel.feedbackResult.observe(viewLifecycleOwner) { success ->

            if (success) {
                Toast.makeText(requireContext(), "Thank you for your feedback!", Toast.LENGTH_SHORT).show()
                feedbackEditText.text.clear()
                ratingBar.rating = 0f

            }
        }

        // Observe errors

        feedbackViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "Failed to submit feedback: $it", Toast.LENGTH_SHORT).show()
            }
        }

        // Submit button
        submitButton.setOnClickListener {

            val rating = ratingBar.rating.toInt()
            val comment = feedbackEditText.text.toString().trim()

            if (rating == 0) {
                Toast.makeText(requireContext(), "Please select a rating", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener

            feedbackViewModel.saveFeedback(uid, rating, comment)
        }
    }
}