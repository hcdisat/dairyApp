package com.hcdisat.dairyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.hcdisat.abstraction.networking.SessionState
import com.hcdisat.common.settings.NavigationConstants
import com.hcdisat.dairyapp.navigation.Router
import com.hcdisat.ui.theme.DairyAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        mainViewModel.syncRemoteImages()

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val sessionState = mainViewModel.getSession().state
        setContent {
            DairyAppTheme {
                val navController = rememberNavController()
                val startDestination = when (sessionState) {
                    SessionState.LOGGED_IN -> NavigationConstants.HOME_ROUTE
                    SessionState.LOGGED_OUT -> NavigationConstants.AUTHENTICATION_ROUTE
                }
                router.SetupNavGraph(
                    startDestination = startDestination,
                    navHostController = navController
                )
            }
        }
    }
}