package com.example.proyecto

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto.databinding.ActivityPerfilBinding

class Perfil : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val PREF_NAME = "CineValorPrefs"
        private const val KEY_USERNAME = "username"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
    }

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

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)

        loadUserProfile()
        setupButtons()
    }

    private fun loadUserProfile() {
        val username = sharedPreferences.getString(KEY_USERNAME, "Usuario Invitado") ?: "Usuario Invitado"
        binding.textView6.text = username
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
        val currentUsername = sharedPreferences.getString(KEY_USERNAME, "")
        input.setText(currentUsername)
        builder.setView(input)

        builder.setPositiveButton("Guardar") { dialog, _ ->
            val newUsername = input.text.toString().trim()
            if (newUsername.isNotEmpty()) {
                sharedPreferences.edit {
                    putString(KEY_USERNAME, newUsername)
                }
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
        // Limpiar datos de sesión
        sharedPreferences.edit {
            putBoolean(KEY_IS_LOGGED_IN, false)
        }

        // Redirigir a Login
        val intent = Intent(this, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()

        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
    }
}