package com.example.proyecto

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.proyecto.data.local.AppDatabase
import com.example.proyecto.data.model.Movie
import com.example.proyecto.data.repository.MovieRepository
import kotlinx.coroutines.launch

class DetallePelicula : AppCompatActivity() {

    private lateinit var repository: MovieRepository
    private var currentMovie: Movie? = null
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_pelicula)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar repositorio
        val database = AppDatabase.getDatabase(this)
        repository = MovieRepository(database.movieDao())

        // Obtener datos del Intent
        val movieId = intent.getIntExtra("MOVIE_ID", -1)
        val movieTitle = intent.getStringExtra("MOVIE_TITLE") ?: ""
        val movieOverview = intent.getStringExtra("MOVIE_OVERVIEW") ?: ""
        val moviePoster = intent.getStringExtra("MOVIE_POSTER")
        val movieBackdrop = intent.getStringExtra("MOVIE_BACKDROP")
        val movieRating = intent.getDoubleExtra("MOVIE_RATING", 0.0)
        val movieReleaseDate = intent.getStringExtra("MOVIE_RELEASE_DATE") ?: ""

        // Crear objeto Movie temporal
        currentMovie = Movie(
            id = movieId,
            title = movieTitle,
            overview = movieOverview,
            posterPath = moviePoster,
            backdropPath = movieBackdrop,
            voteAverage = movieRating,
            releaseDate = movieReleaseDate,
            genreIds = null,
            popularity = 0.0,
            originalLanguage = "",
            adult = false,
            isFavorite = false
        )

        // Mostrar datos
        displayMovieDetails()

        // Verificar si es favorito
        checkFavoriteStatus(movieId)

        // Configurar botón de favoritos
        setupFavoriteButton()
    }

    private fun displayMovieDetails() {
        currentMovie?.let { movie ->
            findViewById<TextView>(R.id.textView3).text = movie.title
            findViewById<TextView>(R.id.textView4).text = movie.overview
            findViewById<RatingBar>(R.id.ratingBar2).rating = movie.getRatingOutOfFive()

            // Cargar imagen de backdrop
            findViewById<ImageView>(R.id.imageView2).load(movie.getBackdropUrl()) {
                crossfade(true)
                placeholder(R.drawable.movie1)
                error(R.drawable.movie1)
            }
        }
    }

    private fun checkFavoriteStatus(movieId: Int) {
        lifecycleScope.launch {
            isFavorite = repository.isMovieFavorite(movieId)
            updateFavoriteButton()
        }
    }

    private fun setupFavoriteButton() {
        findViewById<Button>(R.id.button3).setOnClickListener {
            currentMovie?.let { movie ->
                lifecycleScope.launch {
                    if (isFavorite) {
                        repository.removeFromFavorites(movie)
                        isFavorite = false
                        Toast.makeText(this@DetallePelicula, "Eliminado de favoritos", Toast.LENGTH_SHORT).show()
                    } else {
                        repository.addToFavorites(movie)
                        isFavorite = true
                        Toast.makeText(this@DetallePelicula, "Agregado a favoritos", Toast.LENGTH_SHORT).show()
                    }
                    updateFavoriteButton()
                }
            }
        }
    }

    private fun updateFavoriteButton() {
        val button = findViewById<Button>(R.id.button3)
        button.text = if (isFavorite) "- Quitar de favoritos" else "+ Agregar a favoritos"
    }
}