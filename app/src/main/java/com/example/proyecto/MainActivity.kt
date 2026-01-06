package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.proyecto.data.local.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val session = SessionManager(this)

        // Verificar sesión ANTES de mostrar cualquier contenido
        lifecycleScope.launch {
            val userId = session.userIdFlow.first()
            if (userId != null) {
                // Si hay sesión activa, redirigir directamente a Home
                startActivity(Intent(this@MainActivity, Home::class.java))
                finish()
                return@launch
            }

            // Solo si no hay sesión, mostrar la pantalla de bienvenida
            runOnUiThread {
                enableEdgeToEdge()
                setContentView(R.layout.activity_main)

                // Manejo de insets
                ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                    insets
                }

                // Botón de login
                val buttonLogin: Button = findViewById(R.id.button_login)
                buttonLogin.setOnClickListener {
                    val intent = Intent(this@MainActivity, Login::class.java)
                    startActivity(intent)
                }

                // Botón de registro
                val buttonRegistro: Button = findViewById(R.id.button2)
                buttonRegistro.setOnClickListener {
                    val intent = Intent(this@MainActivity, Registro::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
