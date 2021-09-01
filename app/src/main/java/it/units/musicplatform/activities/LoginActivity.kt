package it.units.musicplatform.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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

        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString()

        binding.loginButton.setOnClickListener {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Intent(this, MainActivity::class.java)
                        .apply { putExtras(Bundle().apply { putString(getString(R.string.user_id), FirebaseAuth.getInstance().currentUser!!.uid) }) }
                        .run { startActivity(this) }
                } else {
                    Toast.makeText(this, "Login failed, check credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }


}