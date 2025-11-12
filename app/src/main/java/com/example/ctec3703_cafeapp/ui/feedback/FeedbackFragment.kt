package com.example.ctec3703_cafeapp.ui.feedback

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ctec3703_cafeapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FeedbackFragment : Fragment(R.layout.fragment_feedback) {

    private lateinit var ratingBar: RatingBar
    private lateinit var feedbackEditText: EditText
    private lateinit var submitButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        ratingBar = view.findViewById(R.id.ratingBar)
        feedbackEditText = view.findViewById(R.id.feedbackEditText)
        submitButton = view.findViewById(R.id.submitFeedbackButton)

        submitButton.setOnClickListener {

            val rating = ratingBar.rating.toInt() // 1–5 stars
            val comment = feedbackEditText.text.toString().trim()

            if (rating == 0) {

                Toast.makeText(requireContext(), "Please select a rating", Toast.LENGTH_SHORT).show()

                return@setOnClickListener
            }

            saveFeedback(rating, comment)
        }
    }

    private fun saveFeedback(rating: Int, comment: String) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        val feedbackData = hashMapOf(
            "userId" to uid,
            "rating" to rating,
            "comment" to comment,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("feedback").add(feedbackData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Thank you for your feedback!", Toast.LENGTH_SHORT).show()
                feedbackEditText.text.clear()
                ratingBar.rating = 0f
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to submit feedback", Toast.LENGTH_SHORT).show()
            }
    }
}