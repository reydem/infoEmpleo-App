// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/login/di/AuthContext.kt
package com.example.infoempleo.login.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.infoempleo.login.di.AuthState
import com.example.infoempleo.login.di.SessionManager

/**
 * CompositionLocal que expone el estado de autenticación en Compose.
 */
val LocalAuthState = staticCompositionLocalOf<AuthState> {
    error("No se ha provisto AuthState en el árbol de composición")
}

/**
 * Proveedor de composición para inyectar AuthState usando SessionManager y StateFlow.
 */
@Composable
fun AuthProvider(
    sessionManager: SessionManager,
    content: @Composable () -> Unit
) {
    // Observa de forma reactiva el StateFlow de SessionManager
    val authState by sessionManager.authState.collectAsState()

    CompositionLocalProvider(
        LocalAuthState provides authState
    ) {
        content()
    }
}

