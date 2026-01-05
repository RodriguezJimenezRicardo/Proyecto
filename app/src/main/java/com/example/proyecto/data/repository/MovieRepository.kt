package com.example.proyecto.data.repository

import com.example.proyecto.data.local.MovieDao
import com.example.proyecto.data.model.Movie
import com.example.proyecto.data.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val movieDao: MovieDao) {

    private val apiService = RetrofitClient.apiService

    // Obtener películas populares desde la API
    suspend fun getPopularMovies(): Result<List<Movie>> {
        return try {
            val response = apiService.getPopularMovies()
            if (response.isSuccessful) {
                val movies = response.body()?.results ?: emptyList()
                Result.success(movies)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener películas mejor valoradas
    suspend fun getTopRatedMovies(): Result<List<Movie>> {
        return try {
            val response = apiService.getTopRatedMovies()
            if (response.isSuccessful) {
                val movies = response.body()?.results ?: emptyList()
                Result.success(movies)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener películas en cartelera
    suspend fun getNowPlayingMovies(): Result<List<Movie>> {
        return try {
            val response = apiService.getNowPlayingMovies()
            if (response.isSuccessful) {
                val movies = response.body()?.results ?: emptyList()
                Result.success(movies)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener películas por género
    suspend fun getMoviesByGenre(genreId: Int): Result<List<Movie>> {
        return try {
            val response = apiService.getMoviesByGenre(genreId = genreId)
            if (response.isSuccessful) {
                val movies = response.body()?.results ?: emptyList()
                Result.success(movies)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Buscar películas
    suspend fun searchMovies(query: String): Result<List<Movie>> {
        return try {
            val response = apiService.searchMovies(query = query)
            if (response.isSuccessful) {
                val movies = response.body()?.results ?: emptyList()
                Result.success(movies)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener detalles de una película
    suspend fun getMovieDetails(movieId: Int): Result<Movie> {
        return try {
            val response = apiService.getMovieDetails(movieId)
            if (response.isSuccessful) {
                val movie = response.body()
                if (movie != null) {
                    Result.success(movie)
                } else {
                    Result.failure(Exception("Película no encontrada"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Funciones de base de datos local para favoritos
    fun getFavoriteMovies(): Flow<List<Movie>> = movieDao.getFavoriteMovies()

    suspend fun toggleFavorite(movie: Movie) {
        val existingMovie = movieDao.getMovieById(movie.id)
        if (existingMovie != null) {
            movieDao.updateFavoriteStatus(movie.id, !existingMovie.isFavorite)
        } else {
            movieDao.insertMovie(movie.copy(isFavorite = true))
        }
    }

    suspend fun addToFavorites(movie: Movie) {
        movieDao.insertMovie(movie.copy(isFavorite = true))
    }

    suspend fun removeFromFavorites(movie: Movie) {
        movieDao.updateFavoriteStatus(movie.id, false)
    }

    suspend fun isMovieFavorite(movieId: Int): Boolean {
        return movieDao.getMovieById(movieId)?.isFavorite ?: false
    }
}

