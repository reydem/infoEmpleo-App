package com.example.jotpackcomposelnstagram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jotpackcomposelnstagram.addtasks.ui.TasksScreen
import com.example.jotpackcomposelnstagram.addtasks.ui.TasksViewModel
import com.example.jotpackcomposelnstagram.login.ui.LoginScreen
import com.example.jotpackcomposelnstagram.login.ui.LoginViewModel
import com.example.jotpackcomposelnstagram.ui.theme.JotpackComposelnstagramTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val tasksViewModel: TasksViewModel by viewModels()
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
                    navController = navController,
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
                     composable("home") {
                             Column(
                                     modifier = Modifier
                                                 .fillMaxSize()
                                                 .padding(top = 50.dp),
                                     horizontalAlignment = Alignment.CenterHorizontally
                                         ) {
                                     Text(
                                             text = "¡Bienvenido de nuevo!",
                                             fontSize = 24.sp,
                                             fontWeight = FontWeight.Bold,
                                             modifier = Modifier.padding(bottom = 16.dp)
                                                 )
//                                     TasksScreen(tasksViewModel)
                                 TasksScreen()
                             }
                         }
                }
            }
        }
    }
}



