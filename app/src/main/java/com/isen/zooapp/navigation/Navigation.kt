package com.isen.zooapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.isen.zooapp.R
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.isen.zooapp.ui.screens.AnimalScreen
import com.isen.zooapp.ui.screens.EnclosureScreen
import com.isen.zooapp.ui.screens.HomeScreen
import com.isen.zooapp.ui.screens.ProfileScreen
import com.isen.zooapp.ui.screens.ServicesScreen
import com.isen.zooapp.ui.screens.SignInScreen
import com.isen.zooapp.ui.screens.SignUpScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    Scaffold(bottomBar = {
        if (currentRoute in listOf(Routes.HOME, Routes.SERVICES, Routes.PROFILE)) {
            NavigationBar {
                NavigationBarItem(selected = currentRoute == Routes.HOME,
                    onClick = { navController.navigate(Routes.HOME) },
                    icon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = stringResource(R.string.home_screen)
                        )
                    },
                    label = { Text(stringResource(R.string.home_screen)) })
                NavigationBarItem(selected = currentRoute == Routes.SERVICES,
                    onClick = { navController.navigate(Routes.SERVICES) },
                    icon = {
                        Icon(
                            Icons.Default.Build,
                            contentDescription = stringResource(R.string.services_screen)
                        )
                    },
                    label = { Text(stringResource(R.string.services_screen)) })
                NavigationBarItem(
                    selected = currentRoute == Routes.PROFILE,
                    onClick = { navController.navigate(Routes.PROFILE) },
                    icon = {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = stringResource(R.string.profile_screen)
                        )
                    },
                    label = { Text(stringResource(R.string.profile_screen)) }
                )

            }
        }
    }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.SIGN_IN,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.SIGN_IN) {
                SignInScreen(navController)
            }

            composable(Routes.SIGN_UP) {
                SignUpScreen(navController)
            }

            composable(Routes.HOME) {
                HomeScreen(navController)
            }

            composable(Routes.SERVICES) {
                ServicesScreen(navController)
            }

            composable(Routes.ENCLOSURES) { backStackEntry ->
                val biomeId = backStackEntry.arguments?.getString("biomeId") ?: ""
                EnclosureScreen(navController, biomeId)
            }

            composable(Routes.ANIMALS) { backStackEntry ->
                val enclosureId = backStackEntry.arguments?.getString("enclosureId") ?: ""
                AnimalScreen(navController, enclosureId)
            }

            composable(Routes.PROFILE) {
                ProfileScreen(navController)
            }
        }
    }
}