package com.example.proyecto

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.proyecto.data.local.AppDatabase
import com.example.proyecto.data.local.SessionManager
import com.example.proyecto.databinding.ActivityPerfilBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class Perfil : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        session = SessionManager(this)

        loadUserProfile()
        setupButtons()
    }

    private fun loadUserProfile() {
        lifecycleScope.launch {
            // Obtener el userId desde DataStore
            val userId = session.userIdFlow.first()

            // Si hay un ID de usuario, buscar el usuario en la base de datos
            if (userId != null) {
                val user = AppDatabase.getDatabase(this@Perfil).userDao().getById(userId)
                // Mostrar el nombre del usuario en lugar del ID
                if (user != null) {
                    binding.textView6.text = user.name // Mostrar el nombre del usuario
                }
            } else {
                binding.textView6.text = "Usuario Invitado"
            }
        }
    }

    private fun setupButtons() {
        // Botón editar perfil
        binding.imageButton.setOnClickListener {
            showEditProfileDialog()
        }

        // Botón Mis reseñas
        binding.button4.setOnClickListener {
            val intent = Intent(this, Resenas::class.java)
            startActivity(intent)
        }

        // Botón Favoritos
        binding.button5.setOnClickListener {
            val intent = Intent(this, Favoritos::class.java)
            startActivity(intent)
        }

        // Botón Ajustes
        binding.button7.setOnClickListener {
            val intent = Intent(this, Ajustes::class.java)
            startActivity(intent)
        }

        // Botón Cerrar Sesión
        binding.button8.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showEditProfileDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar Perfil")

        val input = EditText(this)
        input.hint = "Nuevo nombre de usuario"
        builder.setView(input)

        builder.setPositiveButton("Guardar") { dialog, _ ->
            val newUsername = input.text.toString().trim()
            if (newUsername.isNotEmpty()) {
                binding.textView6.text = newUsername
                Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro de que deseas cerrar sesión?")
            .setPositiveButton("Sí") { _, _ ->
                logout()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun logout() {
        // Limpiar los datos de sesión usando SessionManager (DataStore)
        lifecycleScope.launch {
            session.clear()
            val intent = Intent(this@Perfil, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()

            Toast.makeText(this@Perfil, "Sesión cerrada", Toast.LENGTH_SHORT).show()
        }
    }
}
