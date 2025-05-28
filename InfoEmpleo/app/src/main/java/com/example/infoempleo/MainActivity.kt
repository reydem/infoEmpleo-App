// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/MainActivity.kt
package com.example.infoempleo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.infoempleo.addtasks.ui.TasksScreen
import com.example.infoempleo.addtasks.ui.TasksViewModel
import com.example.infoempleo.login.di.AuthProvider
import com.example.infoempleo.login.di.SessionManager
import com.example.infoempleo.login.ui.LoginScreen
import com.example.infoempleo.login.ui.LoginViewModel
import com.example.infoempleo.usuarios.ui.CandidatosScreen
import com.example.infoempleo.ui.theme.JotpackComposelnstagramTheme
import com.example.infoempleo.di.TokenPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var sessionManager: SessionManager
    @Inject lateinit var tokenPrefs: TokenPreferences

    private val tasksViewModel: TasksViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            JotpackComposelnstagramTheme {
                AuthProvider(sessionManager = sessionManager) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "login"
                    ) {
                        composable("login") {
                            LoginScreen(
                                loginViewModel = loginViewModel,
                                onLoginResult = { success ->
                                    if (success) {
                                        navController.navigate("home") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                }
                            )
                        }

                        composable("home") {
                            // Observa el estado actual de autenticación
                            val authState by sessionManager.authState.collectAsState()

                            // Callback que borra el token y regresa al login
                            val onLogout = {
                                tokenPrefs.saveJwtToken("")
                                tokenPrefs.saveEsReclutador(false)
                                tokenPrefs.saveCorreo("")
                                sessionManager.updateAuthState()
                                navController.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }

                            when {
                                authState.auth && authState.esReclutador -> {
                                    CandidatosScreen(
                                        viewModel = hiltViewModel(),
                                        onLogout = onLogout
                                    )
                                }
                                authState.auth && !authState.esReclutador -> {
                                    TasksScreen(
                                        tasksViewModel = tasksViewModel,
                                        onLogout = onLogout
                                    )
                                }
                                else -> {
                                    // No autenticado, fuerza volver a login
                                    LaunchedEffect(Unit) {
                                        navController.navigate("login") {
                                            popUpTo("home") { inclusive = true }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
