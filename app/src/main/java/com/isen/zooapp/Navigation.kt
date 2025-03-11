package com.isen.zooapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SIGN_IN,
        modifier = modifier
    ) {
        composable(Routes.SIGN_IN) {
            SignInScreen(navController)
        }

        composable(Routes.SIGN_UP) {
            SignUpScreen(navController)
        }
    }
}