package ca.georgiancollege.assignment2v2.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ca.georgiancollege.assignment2v2.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            val email = binding.editEmail.text.toString().trim()
            val password = binding.editPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                    updateUI(auth.currentUser)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Login Failed: ${it.message}", Toast.LENGTH_LONG).show()
                    updateUI(null)
                }
        }

        binding.registerNow.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        updateUI(FirebaseAuth.getInstance().currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MovieListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
