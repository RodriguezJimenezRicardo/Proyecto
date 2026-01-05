package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.proyecto.data.local.AppDatabase
import com.example.proyecto.data.model.Movie
import com.example.proyecto.data.repository.MovieRepository
import com.example.proyecto.databinding.ActivityFavoritosBinding
import com.example.proyecto.ui.adapter.MovieAdapter
import kotlinx.coroutines.launch

class Favoritos : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritosBinding
    private lateinit var repository: MovieRepository
    private val favoritesAdapter = MovieAdapter { movie -> onMovieClick(movie) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritosBinding.inflate(layoutInflater)
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

        setupRecyclerView()
        setupBackButton()
        loadFavorites()
    }

    override fun onResume() {
        super.onResume()
        // Recargar favoritos cuando se regresa de otra actividad
        loadFavorites()
    }

    private fun setupRecyclerView() {
        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(this@Favoritos, 2)
            adapter = favoritesAdapter
        }
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadFavorites() {
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            repository.getFavoriteMovies().collect { movies ->
                if (movies.isEmpty()) {
                    showEmptyState(true)
                    favoritesAdapter.submitList(emptyList())
                } else {
                    showEmptyState(false)
                    favoritesAdapter.submitList(movies)
                }
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun showEmptyState(show: Boolean) {
        if (show) {
            binding.tvEmptyState.visibility = View.VISIBLE
            binding.rvFavorites.visibility = View.GONE
        } else {
            binding.tvEmptyState.visibility = View.GONE
            binding.rvFavorites.visibility = View.VISIBLE
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
}

