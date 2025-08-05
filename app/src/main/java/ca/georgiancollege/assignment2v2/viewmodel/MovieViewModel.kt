package ca.georgiancollege.assignment2v2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ca.georgiancollege.assignment2v2.model.Movie
import ca.georgiancollege.assignment2v2.utils.FirebaseUtil
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class MovieViewModel : ViewModel() {

    private val db: FirebaseFirestore = FirebaseUtil.getFirestore()

    private val _allMovies = MutableLiveData<List<Movie>>()
    val allMovies: LiveData<List<Movie>> get() = _allMovies

    private val _filteredMovies = MutableLiveData<List<Movie>>()
    val filteredMovies: LiveData<List<Movie>> get() = _filteredMovies

    fun loadMovies() {
        db.collection("movies")
            .get()
            .addOnSuccessListener { result ->
                val movieList = result.map { document ->
                    document.toObject<Movie>().copy(id = document.id)
                }
                _allMovies.value = movieList
                _filteredMovies.value = movieList
            }
            .addOnFailureListener {
                _allMovies.value = emptyList()
                _filteredMovies.value = emptyList()
            }
    }

    fun searchMovies(query: String) {
        val currentList = _allMovies.value ?: return
        if (query.isEmpty()) {
            _filteredMovies.value = currentList
        } else {
            val filtered = currentList.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.genre.contains(query, ignoreCase = true) ||
                        it.year.contains(query, ignoreCase = true)
            }
            _filteredMovies.value = filtered
        }
    }
}