package com.example.jotpackcomposelnstagram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jotpackcomposelnstagram.login.ui.LoginScreen
import com.example.jotpackcomposelnstagram.login.ui.LoginViewModel
import com.example.jotpackcomposelnstagram.ui.theme.JotpackComposelnstagramTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JotpackComposelnstagramTheme {
                // 1. Crear NavController
                val navController = rememberNavController()

                // 2. Definir el grafo de navegación
                NavHost(
                    navController    = navController,
                    startDestination = "login"
                ) {
                    // Pantalla de login
                    composable("login") {
                        LoginScreen(
                            loginViewModel = loginViewModel,
                            onLoginResult = { success ->
                                if (success) {
                                    // Navegar a "home" y limpiar el back stack de login
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            }
                        )
                    }
                    // Pantalla de inicio tras login exitoso
                    composable("home") {
                        HomeScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    // Aquí defines tu UI de bienvenida o de contenido principal
}

