package com.example.proyecto.data.remote

import com.example.proyecto.data.model.GenreResponse
import com.example.proyecto.data.model.Movie
import com.example.proyecto.data.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDbApiService {

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        // API Key de TMDb - Necesitarás crear una cuenta gratuita en https://www.themoviedb.org/settings/api
        const val API_KEY = "4ff31bf80aea334ad8d5becd2b29cb0e" // Reemplazar con tu API key
    }

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "es-MX",
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "es-MX",
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "es-MX",
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "es-MX"
    ): Response<Movie>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "es-MX",
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "es-MX",
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "es-MX"
    ): Response<GenreResponse>
}

