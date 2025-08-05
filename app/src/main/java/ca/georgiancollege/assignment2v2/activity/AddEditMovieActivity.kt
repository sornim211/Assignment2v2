package ca.georgiancollege.assignment2v2.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ca.georgiancollege.assignment2v2.databinding.ActivityAddEditMovieBinding
import ca.georgiancollege.assignment2v2.model.Movie
import ca.georgiancollege.assignment2v2.utils.FirebaseUtil
import com.google.firebase.firestore.FirebaseFirestore

class AddEditMovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditMovieBinding
    private lateinit var db: FirebaseFirestore
    private var movieId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseUtil.getFirestore()
        movieId = intent.getStringExtra("MOVIE_ID")

        if (movieId != null) {
            title = "Edit Movie"
            loadMovieDetails(movieId!!)
            binding.btnDelete.visibility = View.VISIBLE
        } else {
            title = "Add Movie"
            binding.btnDelete.visibility = View.GONE
        }

        binding.btnSave.setOnClickListener {
            saveMovie()
        }

        binding.btnDelete.setOnClickListener {
            movieId?.let { id -> deleteMovie(id) }
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun loadMovieDetails(id: String) {
        db.collection("movies").document(id).get()
            .addOnSuccessListener { doc ->
                val movie = doc.toObject(Movie::class.java)
                movie?.let {
                    binding.editTitle.setText(it.title)
                    binding.editYear.setText(it.year)
                    binding.editGenre.setText(it.genre)
                    binding.editPosterUrl.setText(it.posterUrl)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load movie details", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveMovie() {
        val title = binding.editTitle.text.toString().trim()
        val year = binding.editYear.text.toString().trim()
        val genre = binding.editGenre.text.toString().trim()
        val posterUrl = binding.editPosterUrl.text.toString().trim()

        if (title.isEmpty() || year.isEmpty() || genre.isEmpty() || posterUrl.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val movie = Movie(
            id = movieId ?: "",
            title = title,
            year = year,
            genre = genre,
            posterUrl = posterUrl
        )

        if (movieId != null) {
            // Update existing movie
            db.collection("movies").document(movieId!!)
                .set(movie)
                .addOnSuccessListener {
                    Toast.makeText(this, "Movie updated", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Add new movie
            db.collection("movies")
                .add(movie)
                .addOnSuccessListener {
                    Toast.makeText(this, "Movie added", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Add failed", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun deleteMovie(id: String) {
        db.collection("movies").document(id).delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Movie deleted", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
            }
    }
}