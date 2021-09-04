package it.units.musicplatform.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import it.units.musicplatform.R
import it.units.musicplatform.databinding.ActivityAddUserBinding
import it.units.musicplatform.entities.User
import it.units.musicplatform.retrievers.DatabaseReferenceRetriever

class AddUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpButton.setOnClickListener {
            when {
                binding.usernameEditText.text.isBlank() -> showWrongField(binding.usernameEditText, "please insert a username")
                binding.emailEditText.text.isBlank() -> showWrongField(binding.emailEditText, "please insert an email address")
                !Patterns.EMAIL_ADDRESS.matcher(binding.emailEditText.text.toString()).matches() -> showWrongField(binding.emailEditText, "please provide a valid email address")
                binding.passwordEditText.text.isBlank() -> showWrongField(binding.passwordEditText, "please insert a password")
                binding.passwordEditText.text.toString().length < 6 -> showWrongField(binding.passwordEditText, "a password must have at least six characters")
                else -> registerUser(binding.usernameEditText.text.toString(), binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
            }
        }
    }

    private fun showWrongField(editText: EditText, errorMessage: String) {
        editText.requestFocus()
        editText.error = errorMessage
    }

    private fun registerUser(username: String, email: String, password: String) {

        binding.progressBar.visibility = View.VISIBLE
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnSuccessListener { task ->

            val user = User(id = task.user!!.uid, email = email, fullName = username)

            DatabaseReferenceRetriever.user(user.id).setValue(user).addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                val intent = Intent(this, MainActivity::class.java).apply { putExtras(Bundle().apply { putString(getString(R.string.user_id), user.id) }) }
                startActivity(intent)
            }
        }
    }
}