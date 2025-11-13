package com.example.ctec3703_cafeapp.ui.main

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.ctec3703_cafeapp.R
import com.google.android.material.appbar.MaterialToolbar
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var profileIcon: ImageButton
    private lateinit var feedbackIcon: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Toolbar and icons

        toolbar = findViewById(R.id.toolbar)
        profileIcon = toolbar.findViewById(R.id.profileIcon)
        feedbackIcon = toolbar.findViewById(R.id.feedbackIcon)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // NavController

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        setupActionBarWithNavController(navController)

        // Observe fragment changes for toolbar

        navController.addOnDestinationChangedListener { _, destination, _ ->

            // Toolbar visibility

            toolbar.visibility = if (destination.id in listOf(
                    R.id.authLandingFragment,
                    R.id.loginFragment,
                    R.id.registerFragment
                )) View.GONE else View.VISIBLE

            if (toolbar.isVisible) {

                toolbar.navigationIcon =

                    if (destination.id in listOf(
                            R.id.cartFragment,
                            R.id.profileFragment,
                            R.id.feedbackFragment
                        )) ContextCompat.getDrawable(this, R.drawable.ic_back)
                    else null

                toolbar.setNavigationOnClickListener { navController.popBackStack() }

                // profile icon

                profileIcon.visibility =
                    if (destination.id in listOf(R.id.menuFragment, R.id.cartFragment)) View.VISIBLE else View.GONE

                // feedback icon

                feedbackIcon.visibility =
                    if (destination.id in listOf(R.id.menuFragment)) View.VISIBLE else View.GONE

                when (destination.id) {
                    R.id.menuFragment, R.id.profileFragment, R.id.feedbackFragment, R.id.cartFragment -> hideSystemStatusBar()
                    else -> showSystemStatusBar()
                }
            }
        }

        // Toolbar listeners

        profileIcon.setOnClickListener { navController.navigate(R.id.profileFragment) }
        feedbackIcon.setOnClickListener { navController.navigate(R.id.feedbackFragment) }

    }

    private fun hideSystemStatusBar() {

        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)

        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    private fun showSystemStatusBar() {

        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)

        windowInsetsController.show(WindowInsetsCompat.Type.statusBars())
    }

    // Handle system back button

    override fun onSupportNavigateUp(): Boolean {

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        return navHostFragment.navController.navigateUp() || super.onSupportNavigateUp()
    }
}