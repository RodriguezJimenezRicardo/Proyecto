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
import com.example.proyecto.data.local.UserEntity
import kotlinx.coroutines.launch

class Registro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Obtener referencias de las vistas
        val nameEt: EditText = findViewById(R.id.et_nombre)
        val emailEt: EditText = findViewById(R.id.et_email)
        val passEt: EditText = findViewById(R.id.et_password)
        val btn: Button = findViewById(R.id.btn_confirmar_registro)

        // Instanciar base de datos y sesión
        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()
        val session = SessionManager(this)

        // Configurar el botón de registro
        btn.setOnClickListener {
            val name = nameEt.text.toString().trim()
            val email = emailEt.text.toString().trim().lowercase()
            val pass = passEt.text.toString()

            // Validaciones
            if (name.isBlank() || email.isBlank() || pass.length < 6) {
                Toast.makeText(this, "Completa todos los campos (contraseña mínimo 6 caracteres)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Registrar el nuevo usuario de manera asíncrona
            lifecycleScope.launch {
                // Verificar si el correo ya está registrado
                val exists = userDao.getByEmail(email) != null
                if (exists) {
                    Toast.makeText(this@Registro, "Ese correo ya está registrado", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                // Crear el nuevo usuario con contraseña hasheada
                val newUser = UserEntity(
                    name = name,
                    email = email,
                    passwordHash = PasswordHasher.hash(pass)
                )

                // Insertar el nuevo usuario en la base de datos
                val newId = userDao.insert(newUser)

                // Guardar el ID del usuario en la sesión
                session.setUserId(newId)

                // Redirigir al usuario a la pantalla de Home y limpiar el stack
                val intent = Intent(this@Registro, Home::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}
