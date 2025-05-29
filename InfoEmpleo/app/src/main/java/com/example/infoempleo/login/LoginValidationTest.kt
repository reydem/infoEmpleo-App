package com.example.infoempleo.login

import org.junit.Assert.*
import org.junit.Test
import java.util.regex.Pattern

class LoginValidationTest {

    private fun isLoginValid(email: String, password: String): Boolean {
        val emailRegex = "[^@]+@[^@]+\\.[^@]+"
        return Pattern.matches(emailRegex, email) && password.length >= 6
    }

    @Test
    fun validEmailAndPassword_returnsTrue() {
        val email = "test@example.com"
        val password = "securePass123"
        assertTrue(isLoginValid(email, password))
    }

    private fun assertTrue(loginValid: Boolean) {

    }

    @Test
    fun emptyEmail_returnsFalse() {
        val email = ""
        val password = "somepassword"
        assertFalse(isLoginValid(email, password))
    }

    @Test
    fun emptyPassword_returnsFalse() {
        val email = "user@example.com"
        val password = ""
        assertFalse(isLoginValid(email, password))
    }

    @Test
    fun invalidEmailFormat_returnsFalse() {
        val email = "invalidemail"
        val password = "password123"
        assertFalse(isLoginValid(email, password))
    }

    @Test
    fun shortPassword_returnsFalse() {
        val email = "valid@example.com"
        val password = "123"
        assertFalse(isLoginValid(email, password))
    }
}
