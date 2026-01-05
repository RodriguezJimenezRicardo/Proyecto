package com.example.proyecto.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("backdrop_path")
    val backdropPath: String?,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("release_date")
    val releaseDate: String,

    @SerializedName("genre_ids")
    val genreIds: List<Int>? = null,

    @SerializedName("popularity")
    val popularity: Double,

    @SerializedName("original_language")
    val originalLanguage: String,

    @SerializedName("adult")
    val adult: Boolean = false,

    // Campo local para favoritos
    var isFavorite: Boolean = false
) {
    fun getPosterUrl(): String {
        return "https://image.tmdb.org/t/p/w500${posterPath}"
    }

    fun getBackdropUrl(): String {
        return "https://image.tmdb.org/t/p/w780${backdropPath}"
    }

    fun getRatingOutOfFive(): Float {
        return (voteAverage / 2).toFloat()
    }
}

