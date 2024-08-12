package com.sajeg.banksy

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sajeg.banksy.screens.ARScreen
import com.sajeg.banksy.screens.HomeScreen
import kotlinx.serialization.Serializable

// The NavGraph is responsible for the managing of the different screens that a App has
@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = HomeScreen) {
        composable<HomeScreen> {
            HomeScreen(navController = navController)
        }
        composable<ArScreen> {
            ARScreen(navController = navController)
        }
    }
}

// These are the objects or classes that are used as route
@Serializable
object HomeScreen

@Serializable
object ArScreen