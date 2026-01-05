package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto.data.local.AppDatabase
import com.example.proyecto.data.model.Movie
import com.example.proyecto.data.repository.MovieRepository
import com.example.proyecto.databinding.ActivityHomeBinding
import com.example.proyecto.ui.adapter.MovieAdapter
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var repository: MovieRepository

    private val popularAdapter = MovieAdapter { movie -> onMovieClick(movie) }
    private val topRatedAdapter = MovieAdapter { movie -> onMovieClick(movie) }
    private val nowPlayingAdapter = MovieAdapter { movie -> onMovieClick(movie) }

    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar repositorio
        val database = AppDatabase.getDatabase(this)
        repository = MovieRepository(database.movieDao())

        setupRecyclerViews()
        setupGenreChips()
        setupSearch()
        loadMovies()
    }

    private fun setupRecyclerViews() {
        // Configurar RecyclerViews horizontales
        binding.rvPopularMovies.apply {
            layoutManager = LinearLayoutManager(this@Home, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularAdapter
        }

        binding.rvTopRatedMovies.apply {
            layoutManager = LinearLayoutManager(this@Home, LinearLayoutManager.HORIZONTAL, false)
            adapter = topRatedAdapter
        }

        binding.rvNowPlayingMovies.apply {
            layoutManager = LinearLayoutManager(this@Home, LinearLayoutManager.HORIZONTAL, false)
            adapter = nowPlayingAdapter
        }
    }

    private fun setupGenreChips() {
        // IDs de géneros de TMDb
        binding.chipSciFi.setOnClickListener { loadMoviesByGenre(878, "Ciencia Ficción") }
        binding.chipAction.setOnClickListener { loadMoviesByGenre(28, "Acción") }
        binding.chipComedy.setOnClickListener { loadMoviesByGenre(35, "Comedia") }
        binding.chipDrama.setOnClickListener { loadMoviesByGenre(18, "Drama") }
        binding.chipHorror.setOnClickListener { loadMoviesByGenre(27, "Terror") }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener { text ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500) // Debounce de 500ms
                val query = text.toString().trim()
                if (query.isNotEmpty()) {
                    searchMovies(query)
                } else {
                    loadMovies()
                }
            }
        }
    }

    private fun loadMovies() {
        showLoading(true)

        lifecycleScope.launch {
            // Cargar películas populares
            repository.getPopularMovies().onSuccess { movies ->
                popularAdapter.submitList(movies)
            }.onFailure { error ->
                showError("Error al cargar películas populares: ${error.message}")
            }

            // Cargar películas mejor valoradas
            repository.getTopRatedMovies().onSuccess { movies ->
                topRatedAdapter.submitList(movies)
            }.onFailure { error ->
                showError("Error al cargar películas recomendadas: ${error.message}")
            }

            // Cargar películas en cartelera
            repository.getNowPlayingMovies().onSuccess { movies ->
                nowPlayingAdapter.submitList(movies)
            }.onFailure { error ->
                showError("Error al cargar películas en cartelera: ${error.message}")
            }

            showLoading(false)
        }
    }

    private fun loadMoviesByGenre(genreId: Int, genreName: String) {
        showLoading(true)

        lifecycleScope.launch {
            repository.getMoviesByGenre(genreId).onSuccess { movies ->
                popularAdapter.submitList(movies)
                topRatedAdapter.submitList(emptyList())
                nowPlayingAdapter.submitList(emptyList())
                binding.tvPopular.text = "Películas de $genreName"
                showLoading(false)
            }.onFailure { error ->
                showError("Error al cargar películas de $genreName: ${error.message}")
                showLoading(false)
            }
        }
    }

    private fun searchMovies(query: String) {
        showLoading(true)

        lifecycleScope.launch {
            repository.searchMovies(query).onSuccess { movies ->
                if (movies.isEmpty()) {
                    showError("No se encontraron películas")
                    popularAdapter.submitList(emptyList())
                    topRatedAdapter.submitList(emptyList())
                    nowPlayingAdapter.submitList(emptyList())
                } else {
                    popularAdapter.submitList(movies)
                    topRatedAdapter.submitList(emptyList())
                    nowPlayingAdapter.submitList(emptyList())
                    binding.tvPopular.text = "Resultados de búsqueda"
                }
                showLoading(false)
            }.onFailure { error ->
                showError("Error en la búsqueda: ${error.message}")
                showLoading(false)
            }
        }
    }

    private fun onMovieClick(movie: Movie) {
        val intent = Intent(this, DetallePelicula::class.java).apply {
            putExtra("MOVIE_ID", movie.id)
            putExtra("MOVIE_TITLE", movie.title)
            putExtra("MOVIE_OVERVIEW", movie.overview)
            putExtra("MOVIE_POSTER", movie.posterPath)
            putExtra("MOVIE_BACKDROP", movie.backdropPath)
            putExtra("MOVIE_RATING", movie.voteAverage)
            putExtra("MOVIE_RELEASE_DATE", movie.releaseDate)
        }
        startActivity(intent)
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}