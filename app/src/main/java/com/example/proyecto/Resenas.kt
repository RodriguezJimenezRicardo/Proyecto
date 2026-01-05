package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto.data.local.AppDatabase
import com.example.proyecto.databinding.ActivityResenasBinding
import com.example.proyecto.ui.adapter.ReviewAdapter
import kotlinx.coroutines.launch

class Resenas : AppCompatActivity() {

    private lateinit var binding: ActivityResenasBinding
    private lateinit var database: AppDatabase
    private val reviewAdapter = ReviewAdapter(
        onItemClick = { review -> onReviewClick(review) },
        onEditClick = { review -> editReview(review) },
        onDeleteClick = { review -> showDeleteConfirmation(review) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResenasBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = AppDatabase.getDatabase(this)

        setupRecyclerView()
        setupBackButton()
        setupFabButton()
        loadReviews()
    }

    override fun onResume() {
        super.onResume()
        loadReviews()
    }

    private fun setupRecyclerView() {
        binding.rvReviews.apply {
            layoutManager = LinearLayoutManager(this@Resenas)
            adapter = reviewAdapter
        }
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupFabButton() {
        binding.fabAddReview.setOnClickListener {
            // Aquí podrías navegar a una pantalla de búsqueda de películas
            // para agregar una nueva reseña
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }

    private fun loadReviews() {
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            database.reviewDao().getAllReviews().collect { reviews ->
                if (reviews.isEmpty()) {
                    showEmptyState(true)
                    reviewAdapter.submitList(emptyList())
                } else {
                    showEmptyState(false)
                    reviewAdapter.submitList(reviews)
                }
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun showEmptyState(show: Boolean) {
        if (show) {
            binding.tvEmptyState.visibility = View.VISIBLE
            binding.rvReviews.visibility = View.GONE
        } else {
            binding.tvEmptyState.visibility = View.GONE
            binding.rvReviews.visibility = View.VISIBLE
        }
    }

    private fun onReviewClick(review: com.example.proyecto.data.model.Review) {
        // Navegar al detalle de la película
        val intent = Intent(this, DetallePelicula::class.java).apply {
            putExtra("MOVIE_ID", review.movieId)
            putExtra("MOVIE_TITLE", review.movieTitle)
            putExtra("MOVIE_OVERVIEW", "")
            putExtra("MOVIE_POSTER", review.moviePosterPath)
            putExtra("MOVIE_BACKDROP", review.moviePosterPath)
            putExtra("MOVIE_RATING", 0.0)
            putExtra("MOVIE_RELEASE_DATE", "")
        }
        startActivity(intent)
    }

    private fun editReview(review: com.example.proyecto.data.model.Review) {
        val intent = Intent(this, EditarResena::class.java).apply {
            putExtra("REVIEW_ID", review.id)
            putExtra("MOVIE_ID", review.movieId)
            putExtra("MOVIE_TITLE", review.movieTitle)
            putExtra("MOVIE_POSTER", review.moviePosterPath)
            putExtra("RATING", review.rating)
            putExtra("COMMENT", review.comment)
        }
        startActivity(intent)
    }

    private fun showDeleteConfirmation(review: com.example.proyecto.data.model.Review) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Reseña")
            .setMessage("¿Estás seguro de que deseas eliminar esta reseña?")
            .setPositiveButton("Eliminar") { _, _ ->
                deleteReview(review)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteReview(review: com.example.proyecto.data.model.Review) {
        lifecycleScope.launch {
            database.reviewDao().deleteReview(review)
        }
    }
}

