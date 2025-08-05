package ca.georgiancollege.assignment2v2.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ca.georgiancollege.assignment2v2.databinding.ActivityMovieListBinding

class MovieListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}

