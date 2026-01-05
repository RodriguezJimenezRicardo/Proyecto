package com.example.proyecto

import android.content.Intent
import android.os.Bundle
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
import com.example.proyecto.databinding.ActivityDetallePeliculaBinding
import kotlinx.coroutines.launch

class DetallePelicula : AppCompatActivity() {

    private lateinit var binding: ActivityDetallePeliculaBinding
    private lateinit var repository: MovieRepository
    private lateinit var database: AppDatabase
    private var currentMovie: Movie? = null
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallePeliculaBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar repositorio
        database = AppDatabase.getDatabase(this)
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

        // Configurar botones
        setupFavoriteButton()
        setupReviewButton()
    }

    private fun displayMovieDetails() {
        currentMovie?.let { movie ->
            binding.textView3.text = movie.title
            binding.textView4.text = movie.overview

            // Cargar imagen de backdrop
            binding.imageView2.load(movie.getBackdropUrl()) {
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
        binding.button3.setOnClickListener {
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
        binding.button3.text = if (isFavorite) "- Quitar de favoritos" else "+ Agregar a favoritos"
    }

    private fun setupReviewButton() {
        binding.btnHacerResena.setOnClickListener {
            currentMovie?.let { movie ->
                lifecycleScope.launch {
                    // Verificar si ya existe una reseña
                    val existingReview = database.reviewDao().getReviewByMovieId(movie.id)

                    val intent = Intent(this@DetallePelicula, EditarResena::class.java).apply {
                        putExtra("MOVIE_ID", movie.id)
                        putExtra("MOVIE_TITLE", movie.title)
                        putExtra("MOVIE_POSTER", movie.posterPath)

                        if (existingReview != null) {
                            putExtra("REVIEW_ID", existingReview.id)
                            putExtra("RATING", existingReview.rating)
                            putExtra("COMMENT", existingReview.comment)
                        }
                    }
                    startActivity(intent)
                }
            }
        }
    }
}