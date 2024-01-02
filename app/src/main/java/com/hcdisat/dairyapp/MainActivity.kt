package com.hcdisat.dairyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.hcdisat.dairyapp.abstraction.networking.SessionState
import com.hcdisat.dairyapp.navigation.Screen
import com.hcdisat.dairyapp.navigation.SetupNavGraph
import com.hcdisat.dairyapp.ui.theme.DairyAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        mainViewModel.syncRemoteImages()

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DairyAppTheme {
                val navController = rememberNavController()
                SetupNavGraph(
                    startDestination = getStartDestination(),
                    navHostController = navController
                )
            }
        }
    }

    private fun getStartDestination(): Screen = when (mainViewModel.getSession().state) {
        SessionState.LOGGED_IN -> Screen.Home
        SessionState.LOGGED_OUT -> Screen.Authentication
    }
}