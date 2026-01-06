package com.example.proyecto

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto.databinding.ActivityAjustesBinding

class Ajustes : AppCompatActivity() {

    private lateinit var binding: ActivityAjustesBinding
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val PREF_NAME = "CineValorPrefs"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAjustesBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)

        loadSettings()
        setupButtons()
    }

    private fun loadSettings() {
        // Establecer versión de la app
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            binding.tvAppVersion.text = "Versión de la App: ${packageInfo.versionName}"
        } catch (e: Exception) {
            binding.tvAppVersion.text = "Versión de la App: 1.0.0"
        }
    }

    private fun setupButtons() {
        // Botón Volver
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Botón Cambiar Contraseña
        binding.btnChangePassword.setOnClickListener {
            showChangePasswordDialog()
        }

        // Botón Eliminar Cuenta
        binding.btnDeleteAccount.setOnClickListener {
            showDeleteAccountDialog()
        }

        // Botón Términos y Condiciones
        binding.btnTerms.setOnClickListener {
            showTermsDialog()
        }

        // Botón Política de Privacidad
        binding.btnPrivacy.setOnClickListener {
            showPrivacyDialog()
        }
    }

    private fun showChangePasswordDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cambiar Contraseña")

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 20, 50, 20)

        val currentPasswordInput = EditText(this)
        currentPasswordInput.hint = "Contraseña actual"
        currentPasswordInput.inputType = android.text.InputType.TYPE_CLASS_TEXT or
                                         android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        layout.addView(currentPasswordInput)

        val newPasswordInput = EditText(this)
        newPasswordInput.hint = "Nueva contraseña"
        newPasswordInput.inputType = android.text.InputType.TYPE_CLASS_TEXT or
                                      android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        layout.addView(newPasswordInput)

        val confirmPasswordInput = EditText(this)
        confirmPasswordInput.hint = "Confirmar nueva contraseña"
        confirmPasswordInput.inputType = android.text.InputType.TYPE_CLASS_TEXT or
                                         android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        layout.addView(confirmPasswordInput)

        builder.setView(layout)

        builder.setPositiveButton("Cambiar") { dialog, _ ->
            val currentPassword = currentPasswordInput.text.toString().trim()
            val newPassword = newPasswordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            when {
                currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty() -> {
                    Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                }
                newPassword.length < 6 -> {
                    Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                }
                newPassword != confirmPassword -> {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Aquí iría la lógica para cambiar la contraseña en el servidor
                    Toast.makeText(this, "Contraseña actualizada exitosamente", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun showDeleteAccountDialog() {
        AlertDialog.Builder(this)
            .setTitle("⚠️ Eliminar Cuenta")
            .setMessage("Esta acción es irreversible. Se eliminarán todos tus datos, reseñas y favoritos.\n\n¿Estás seguro de que deseas eliminar tu cuenta?")
            .setPositiveButton("Eliminar") { _, _ ->
                showConfirmDeleteDialog()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showConfirmDeleteDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmación Final")
        builder.setMessage("Escribe 'ELIMINAR' para confirmar:")

        val input = EditText(this)
        input.hint = "ELIMINAR"
        builder.setView(input)

        builder.setPositiveButton("Confirmar") { dialog, _ ->
            val confirmation = input.text.toString().trim()
            if (confirmation == "ELIMINAR") {
                // Aquí iría la lógica para eliminar la cuenta
                Toast.makeText(this, "Cuenta eliminada. Esperamos verte pronto.", Toast.LENGTH_LONG).show()

                // Limpiar preferencias y regresar al login
                sharedPreferences.edit {
                    clear()
                }

                // Redirigir al login
                val intent = android.content.Intent(this, Login::class.java)
                intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or
                              android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Texto incorrecto. Cuenta no eliminada.", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun showTermsDialog() {
        val termsText = """
            TÉRMINOS Y CONDICIONES DE USO
            
            1. ACEPTACIÓN DE TÉRMINOS
            Al utilizar CineValor, aceptas estos términos y condiciones.
            
            2. USO DEL SERVICIO
            - Debes tener al menos 13 años para usar la aplicación
            - Eres responsable de mantener la confidencialidad de tu cuenta
            - No puedes usar la aplicación para fines ilegales
            
            3. CONTENIDO DEL USUARIO
            - Eres responsable del contenido que publicas
            - No publiques contenido ofensivo, ilegal o que viole derechos de terceros
            - Nos reservamos el derecho de eliminar contenido inapropiado
            
            4. PROPIEDAD INTELECTUAL
            - Todos los derechos sobre la aplicación son de CineValor
            - La información de películas proviene de TMDb
            
            5. LIMITACIÓN DE RESPONSABILIDAD
            - Proporcionamos la aplicación "tal cual"
            - No nos hacemos responsables de daños derivados del uso
            
            Fecha de última actualización: Enero 2026
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("Términos y Condiciones")
            .setMessage(termsText)
            .setPositiveButton("Aceptar", null)
            .show()
    }

    private fun showPrivacyDialog() {
        val privacyText = """
            POLÍTICA DE PRIVACIDAD
            
            1. INFORMACIÓN QUE RECOPILAMOS
            - Nombre de usuario
            - Reseñas y calificaciones de películas
            - Películas favoritas
            - Preferencias de configuración
            
            2. USO DE LA INFORMACIÓN
            - Proporcionar y mejorar nuestros servicios
            - Personalizar tu experiencia
            - Comunicarnos contigo sobre actualizaciones
            
            3. COMPARTIR INFORMACIÓN
            - No vendemos tu información personal
            - Podemos compartir datos agregados y anónimos
            - Usamos TMDb para información de películas
            
            4. SEGURIDAD
            - Implementamos medidas de seguridad razonables
            - Almacenamos datos localmente en tu dispositivo
            
            5. TUS DERECHOS
            - Puedes acceder a tu información
            - Puedes solicitar la eliminación de tu cuenta
            - Puedes modificar tus datos en cualquier momento
            
            6. CAMBIOS A LA POLÍTICA
            - Nos reservamos el derecho de actualizar esta política
            - Te notificaremos sobre cambios significativos
            
            Fecha de última actualización: Enero 2026
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("Política de Privacidad")
            .setMessage(privacyText)
            .setPositiveButton("Entendido", null)
            .show()
    }
}
