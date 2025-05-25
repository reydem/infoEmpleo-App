// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/MainActivity.kt
package com.example.infoempleo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.infoempleo.addtasks.ui.TasksScreen
import com.example.infoempleo.addtasks.ui.TasksViewModel
import com.example.infoempleo.login.di.AuthProvider
import com.example.infoempleo.login.di.SessionManager
import com.example.infoempleo.login.ui.LoginScreen
import com.example.infoempleo.login.ui.LoginViewModel
import com.example.infoempleo.ui.theme.JotpackComposelnstagramTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // 1) Inyectamos SessionManager directamente
    @Inject lateinit var sessionManager: SessionManager

    private val tasksViewModel: TasksViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // 2) Aplicamos el tema
            JotpackComposelnstagramTheme {
                // 3) Envolvemos TODO en AuthProvider para que LocalAuthState esté disponible
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
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 50.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                TasksScreen(tasksViewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
