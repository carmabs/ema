package com.carmabs.ema.presentation.ui.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class ProfileActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavHost(
                navController = rememberNavController(),
                startDestination = "Login"
            ) {
                composable("Login"){
                    ProfileComposableScreen( )
                }
            }
        }
    }
}
