package com.carmabs.ema.presentation.ui.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.carmabs.ema.android.compose.ui.EmaComposableScreen
import com.carmabs.ema.android.di.injectDirect


class ProfileActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "Login"
            ) {
                composable("Login"){
                    val vm:ProfileViewModel = injectDirect()
                    EmaComposableScreen(
                        defaultState = ProfileState(),
                        navigator = ProfileNavigator(this@ProfileActivity,navController,it),
                        androidViewModel = ProfileAndroidViewModel(vm),
                        screenView = ProfileScreen(),
                        screenActions = vm
                    )
                }
            }
        }
    }
}
