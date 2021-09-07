package it.units.musicplatform.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import it.units.musicplatform.R
import it.units.musicplatform.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setUpListeners()

    }

    private fun setUpListeners() {

        binding.registerUserTextView.setOnClickListener { startActivity(Intent(this, AddUserActivity::class.java)) }

        binding.loginButton.setOnClickListener { login(binding.emailEditText.text.toString().trim(), binding.passwordEditText.text.toString()) }

        binding.forgotPasswordTextView.setOnClickListener { startActivity(Intent(this, ResetPasswordActivity::class.java)) }

    }

    private fun login(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Toast.makeText(this, "Login failed, check credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }


}