package com.hcdisat.dairyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.hcdisat.dairyapp.navigation.Screen
import com.hcdisat.dairyapp.navigation.SetupNavGraph
import com.hcdisat.dairyapp.ui.theme.DairyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            DairyAppTheme {
                val navController = rememberNavController()
                SetupNavGraph(
                    startDestination = Screen.Authentication,
                    navHostController = navController
                )
            }
        }
    }
}