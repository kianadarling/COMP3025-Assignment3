package com.example.comp3025_assignment3

import android.content.Intent
import com.example.comp3025_assignment3.Movie
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class MovieList : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var addButton: Button
    private var movieList: MutableList<Movie> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance()

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up the movie adapter
        movieAdapter = MovieAdapter(movieList, this)
        recyclerView.adapter = movieAdapter

        // Set up the add button
        addButton = findViewById(R.id.add_button)
        addButton.setOnClickListener {
            val intent = Intent(this, AddEditActivity::class.java)
            intent.putExtra("mode", "add")
            startActivity(intent)
        }

        // Get the movie list from Firebase Firestore
        db.collection("movies").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val movie = Movie(
                        document.id,
                        document.getString("title")!!,
                        document.getString("studio")!!,
                        document.getLong("thumbnail")!!.toInt(),
                        document.getDouble("criticsRating")!!
                    )
                    movieList.add(movie)
                }
                movieAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error getting movie list: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onResume() {
        super.onResume()
        // Get the movie list from Firebase Firestore
        db.collection("movies").get()
            .addOnSuccessListener { documents ->
                movieList.clear()
                for (document in documents) {
                    val movie = Movie(
                        document.id,
                        document.getString("title")!!,
                        document.getString("studio")!!,
                        document.getLong("thumbnail")!!.toInt(),
                        document.getDouble("criticsRating")!!
                    )
                    movieList.add(movie)
                }
                movieAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error getting movie list: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
