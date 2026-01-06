package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.proyecto.data.auth.PasswordHasher
import com.example.proyecto.data.local.AppDatabase
import com.example.proyecto.data.local.SessionManager
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEt: EditText = findViewById(R.id.et_login_email)
        val passEt: EditText = findViewById(R.id.et_login_password)
        val btnIngresar: Button = findViewById(R.id.btn_ingresar)

        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()
        val session = SessionManager(this)

        btnIngresar.setOnClickListener {
            val email = emailEt.text.toString().trim().lowercase()
            val pass = passEt.text.toString()

            // Validación de campos vacíos
            if (email.isBlank() || pass.isBlank()) {
                Toast.makeText(this, "Completa correo y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validación de usuario y contraseña
            lifecycleScope.launch {
                val user = userDao.getByEmail(email)
                if (user == null || !PasswordHasher.verify(pass, user.passwordHash)) {
                    Toast.makeText(this@Login, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                // Guardar el userId en la sesión (DataStore)
                session.setUserId(user.id)

                // Redirigir al Home
                startActivity(Intent(this@Login, Home::class.java))
                finish()
            }
        }
    }
}
