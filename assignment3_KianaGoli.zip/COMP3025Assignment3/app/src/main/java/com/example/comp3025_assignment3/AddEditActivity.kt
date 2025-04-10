package com.example.comp3025_assignment3

import android.content.Intent
import com.example.comp3025_assignment3.Movie
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class AddEditActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var titleEditText: EditText
    private lateinit var studioEditText: EditText
    private lateinit var thumbnailEditText: EditText
    private lateinit var criticsRatingEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var cancelButton: Button
    private var mode: String = ""
    private var movie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance()

        // Get the mode and movie from the intent
        mode = intent.getStringExtra("mode")!!
        movie = intent.getParcelableExtra("movie")

        // Set up the UI elements
        titleEditText = findViewById(R.id.title_edit_text)
        studioEditText = findViewById(R.id.studio_edit_text)
        thumbnailEditText = findViewById(R.id.thumbnail_edit_text)
        criticsRatingEditText = findViewById(R.id.critics_rating_edit_text)
        submitButton = findViewById(R.id.submit_button)
        cancelButton = findViewById(R.id.cancel_button)

        // Set up the UI elements based on the mode
        if (mode == "edit") {
            titleEditText.setText(movie!!.title)
            studioEditText.setText(movie!!.studio)
            thumbnailEditText.setText(movie!!.thumbnail.toString())
            criticsRatingEditText.setText(movie!!.criticsRating.toString())
            submitButton.text = "Edit"
        } else {
            submitButton.text = "Add"
        }

        // Set up the submit button
        submitButton.setOnClickListener {
            if (mode == "edit") {
                // Edit the movie
                val title = titleEditText.text.toString()
                val studio = studioEditText.text.toString()
                val thumbnail = thumbnailEditText.text.toString().toInt()
                val criticsRating = criticsRatingEditText.text.toString().toDouble()

                db.collection("movies").document(movie!!.id).update(
                    mapOf(
                        "title" to title,
                        "studio" to studio,
                        "thumbnail" to thumbnail,
                        "criticsRating" to criticsRating
                    )
                )
                    .addOnSuccessListener {
                        Toast.makeText(this, "Movie edited successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error editing movie: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // Add a new movie
                val title = titleEditText.text.toString()
                val studio = studioEditText.text.toString()
                val thumbnail = thumbnailEditText.text.toString().toInt()
                val criticsRating = criticsRatingEditText.text.toString().toDouble()

                val movie = Movie(
                    "",
                    title,
                    studio,
                    thumbnail,
                    criticsRating
                )

                db.collection("movies").add(movie)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(this, "Movie added successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error adding movie: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        // Set up the cancel button
        cancelButton.setOnClickListener {
            finish()
        }
    }
}
