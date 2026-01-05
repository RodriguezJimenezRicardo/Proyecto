package com.example.proyecto

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Login : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val PREF_NAME = "CineValorPrefs"
        private const val KEY_USERNAME = "username"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)

        // Verificar si ya hay sesión iniciada
        if (sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)) {
            navigateToHome()
            return
        }

        val etUsername: EditText = findViewById(R.id.et_login_email)
        val btnIngresar: Button = findViewById(R.id.btn_ingresar)

        btnIngresar.setOnClickListener {
            val username = etUsername.text.toString().trim()

            if (username.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa tu correo o nombre de usuario", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Guardar datos de sesión
            sharedPreferences.edit {
                putString(KEY_USERNAME, username)
                putBoolean(KEY_IS_LOGGED_IN, true)
            }

            Toast.makeText(this, "¡Bienvenido $username!", Toast.LENGTH_SHORT).show()
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }
}