package com.example.comp3025_assignment3

import android.content.Context
import android.content.Intent
import com.example.comp3025_assignment3.Movie
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore



class MovieAdapter(
    private val movieList: MutableList<Movie>,
    private val context: Context
) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    private lateinit var db: FirebaseFirestore

    // onCreateViewHolder method
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movieList[position]
        holder.titleTextView.text = movie.title
        holder.studioTextView.text = movie.studio
        holder.thumbnailImageView.setImageResource(movie.thumbnail)
        holder.criticsRatingTextView.text = movie.criticsRating.toString()

        // Set up the edit button
        holder.editButton.setOnClickListener {
            val intent = Intent(context, AddEditActivity::class.java)
            intent.putExtra("mode", "edit")
            intent.putExtra("movie", movie)
            context.startActivity(intent)
        }

        // Set up the delete button
        holder.deleteButton.setOnClickListener {

            // Remove the movie from the list
            movieList.removeAt(position)
            // Update the RecyclerView
            notifyItemRemoved(position)

            // Delete the movie from Firebase Firestore
            db = FirebaseFirestore.getInstance()
            db.collection("movies").document(movie.id).delete()
                .addOnSuccessListener {
                    Toast.makeText(context, "Movie deleted successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error deleting movie: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title_text_view)
        val studioTextView: TextView = itemView.findViewById(R.id.studio_text_view)
        val thumbnailImageView: ImageView = itemView.findViewById(R.id.thumbnail_image_view)
        val criticsRatingTextView: TextView = itemView.findViewById(R.id.critics_rating_text_view)
        val editButton: Button = itemView.findViewById(R.id.edit_button)
        val deleteButton: Button = itemView.findViewById(R.id.delete_button)
    }
}
