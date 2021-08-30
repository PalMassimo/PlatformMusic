package it.units.musicplatform.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import it.units.musicplatform.R
import it.units.musicplatform.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.loginButton.setOnClickListener {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.emailEditText.text.toString().trim(), binding.passwordEditText.text.toString().trim()).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)//.apply { put(getString(R.string.user_id), auth.currentUser?.uid) }
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Login failed, check credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }


}