package com.example.passwords

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.passwords.presentation.screens.GeneratePasswordScreen
import com.example.passwords.presentation.screens.PasswordListScreen
import com.example.passwords.presentation.screens.SplashScreen

@Composable
fun PasswordsNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = modifier
    ) {
        composable("splash") {
            SplashScreen {
                navController.navigate("generate") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }

        composable("generate") {
            GeneratePasswordScreen {
                navController.navigate("passwords")
            }
        }

        composable("passwords") {
            PasswordListScreen {
                navController.popBackStack()
            }
        }
    }
}