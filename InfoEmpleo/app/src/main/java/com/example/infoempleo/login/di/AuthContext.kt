// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/login/di/AuthContext.kt
package com.example.infoempleo.login.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.infoempleo.login.di.AuthState
import com.example.infoempleo.login.di.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * CompositionLocal que expone el estado de autenticación en Compose.
 */
val LocalAuthState = staticCompositionLocalOf<AuthState> {
    error("No se ha provisto AuthState en el árbol de composición")
}

/**
 * Proveedor de composición para inyectar AuthState usando SessionManager.
 */
@Composable
fun AuthProvider(
    sessionManager: SessionManager,
    content: @Composable () -> Unit
) {
    // Obtiene el estado actual de la sesión
    val authState = sessionManager.getAuthState()

    CompositionLocalProvider(
        LocalAuthState provides authState
    ) {
        content()
    }
}
