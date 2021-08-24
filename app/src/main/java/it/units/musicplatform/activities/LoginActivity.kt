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
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()

//        val emailEditText = findViewById<EditText>(R.id.emailEditText)
//        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
//        val button: Button = findViewById(R.id.loginButton)
//
//        val email: String = emailEditText.text.toString()
//        val password: String = passwordEditText.text.toString()

        binding.loginButton.setOnClickListener {
            auth.signInWithEmailAndPassword(binding.emailEditText.text.toString().trim(), binding.passwordEditText.text.toString().trim()).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java).apply { putExtra(getString(R.string.user_id), auth.currentUser?.uid) }
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Login failed, check credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }


}