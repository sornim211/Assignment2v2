package ca.georgiancollege.assignment2v2.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.assignment2v2.adapter.MovieAdapter
import ca.georgiancollege.assignment2v2.databinding.ActivityMovieListBinding
import ca.georgiancollege.assignment2v2.model.Movie
import ca.georgiancollege.assignment2v2.utils.FirebaseUtil.getFirestore

class MovieListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieListBinding
    private val db = getFirestore()
    private lateinit var adapter: MovieAdapter
    private val allMovies: MutableList<Movie> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MovieAdapter(allMovies) { movie ->
            // Handle item click here if needed
            Toast.makeText(this, "Clicked: ${movie.title}", Toast.LENGTH_SHORT).show()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        fetchMoviesFromFirestore()

        binding.buttonSearch.setOnClickListener {
            val query = binding.editTextSearch.text.toString().trim()
            searchMovies(query)
        }
    }

    private fun fetchMoviesFromFirestore() {
        db.collection("Movies")  // Use your exact Firestore collection name
            .get()
            .addOnSuccessListener { result ->
                allMovies.clear()
                for (document in result) {
                    val movie = document.toObject(Movie::class.java)
                    allMovies.add(movie)
                }
                adapter.updateMovies(allMovies)
            }
            .addOnFailureListener { e ->
                Log.w("MovieList", "Error getting movies", e)
                Toast.makeText(this, "Failed to load movies", Toast.LENGTH_SHORT).show()
            }
    }

    private fun searchMovies(query: String) {
        val filtered = allMovies.filter {
            it.title.contains(query, ignoreCase = true)
        }
        adapter.updateMovies(filtered)
    }
}
