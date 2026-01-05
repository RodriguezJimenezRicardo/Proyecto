package com.example.proyecto

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.proyecto.data.local.AppDatabase
import com.example.proyecto.data.model.Review
import com.example.proyecto.databinding.ActivityEditarResenaBinding
import kotlinx.coroutines.launch

class EditarResena : AppCompatActivity() {

    private lateinit var binding: ActivityEditarResenaBinding
    private lateinit var database: AppDatabase
    private var reviewId: Int = 0
    private var movieId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarResenaBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = AppDatabase.getDatabase(this)

        // Obtener datos del Intent
        reviewId = intent.getIntExtra("REVIEW_ID", 0)
        movieId = intent.getIntExtra("MOVIE_ID", -1)
        val movieTitle = intent.getStringExtra("MOVIE_TITLE") ?: ""
        val moviePoster = intent.getStringExtra("MOVIE_POSTER")
        val currentRating = intent.getFloatExtra("RATING", 0f)
        val currentComment = intent.getStringExtra("COMMENT") ?: ""

        // Mostrar datos
        binding.tvMovieTitle.text = movieTitle

        if (moviePoster != null) {
            val posterUrl = "https://image.tmdb.org/t/p/w200$moviePoster"
            binding.ivMoviePoster.load(posterUrl) {
                crossfade(true)
                placeholder(R.drawable.movie1)
                error(R.drawable.movie1)
            }
        }

        // Cargar datos existentes si es edición
        if (reviewId > 0) {
            binding.ratingBarUser.rating = currentRating
            binding.etReviewComment.setText(currentComment)
            binding.btnSaveReview.text = "Actualizar Reseña"
        }

        setupButtons()
    }

    private fun setupButtons() {
        // Botón cancelar
        binding.btnCancel.setOnClickListener {
            finish()
        }

        // Botón guardar
        binding.btnSaveReview.setOnClickListener {
            saveReview()
        }
    }

    private fun saveReview() {
        val rating = binding.ratingBarUser.rating
        val comment = binding.etReviewComment.text.toString().trim()

        if (rating == 0f) {
            Toast.makeText(this, "Por favor selecciona una calificación", Toast.LENGTH_SHORT).show()
            return
        }

        if (comment.isEmpty()) {
            Toast.makeText(this, "Por favor escribe tu opinión", Toast.LENGTH_SHORT).show()
            return
        }

        if (comment.length < 10) {
            Toast.makeText(this, "La opinión debe tener al menos 10 caracteres", Toast.LENGTH_SHORT).show()
            return
        }

        val movieTitle = intent.getStringExtra("MOVIE_TITLE") ?: ""
        val moviePoster = intent.getStringExtra("MOVIE_POSTER")

        lifecycleScope.launch {
            val review = Review(
                id = reviewId,
                movieId = movieId,
                movieTitle = movieTitle,
                moviePosterPath = moviePoster,
                rating = rating,
                comment = comment
            )

            database.reviewDao().insertReview(review)

            Toast.makeText(
                this@EditarResena,
                if (reviewId > 0) "Reseña actualizada correctamente" else "Reseña guardada correctamente",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
}

