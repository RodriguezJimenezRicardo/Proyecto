package com.example.proyecto.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.proyecto.R
import com.example.proyecto.data.model.Review

class ReviewAdapter(
    private val onItemClick: (Review) -> Unit,
    private val onEditClick: (Review) -> Unit,
    private val onDeleteClick: (Review) -> Unit
) : ListAdapter<Review, ReviewAdapter.ReviewViewHolder>(ReviewDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view, onItemClick, onEditClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ReviewViewHolder(
        itemView: View,
        private val onItemClick: (Review) -> Unit,
        private val onEditClick: (Review) -> Unit,
        private val onDeleteClick: (Review) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val ivPoster: ImageView = itemView.findViewById(R.id.iv_movie_poster)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_movie_title)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.rating_bar_review)
        private val tvComment: TextView = itemView.findViewById(R.id.tv_review_comment)
        private val tvDate: TextView = itemView.findViewById(R.id.tv_review_date)
        private val btnEdit: ImageButton = itemView.findViewById(R.id.btn_edit_review)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btn_delete_review)

        fun bind(review: Review) {
            tvTitle.text = review.movieTitle
            ratingBar.rating = review.rating
            tvComment.text = review.comment
            tvDate.text = review.getFormattedDate()

            // Cargar poster
            val posterUrl = "https://image.tmdb.org/t/p/w200${review.moviePosterPath}"
            ivPoster.load(posterUrl) {
                crossfade(true)
                placeholder(R.drawable.movie1)
                error(R.drawable.movie1)
            }

            itemView.setOnClickListener {
                onItemClick(review)
            }

            btnEdit.setOnClickListener {
                onEditClick(review)
            }

            btnDelete.setOnClickListener {
                onDeleteClick(review)
            }
        }
    }

    class ReviewDiffCallback : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    }
}

