package com.example.ctec3703_cafeapp

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class App : Application() {

    lateinit var db: FirebaseFirestore
        private set

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase

        FirebaseApp.initializeApp(this)

        // Initialize Firestore

        db = FirebaseFirestore.getInstance()

        // Enable local persistence (optional but recommended)

        db.firestoreSettings = com.google.firebase.firestore.FirebaseFirestoreSettings.Builder()
            .build()
    }
}