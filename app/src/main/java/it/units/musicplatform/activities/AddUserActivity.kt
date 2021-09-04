package it.units.musicplatform.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
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
        setContentView(R.layout.activity_add_user)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signUpButton.setOnClickListener { registerUser() }
    }

    private fun registerUser() {
        val fullName: String = binding.fullNameEditText.text.toString().trim()
        val email: String = binding.emailEditText.text.toString().trim()
        val password: String = binding.passwordEditText.text.toString().trim()

        if (fullName.isEmpty()) {
            binding.fullNameEditText.error = "Provide your full name"
            binding.fullNameEditText.requestFocus()
            return
        }

        if (email.isEmpty()) {
            binding.emailEditText.error = "Provide an email"
            binding.emailEditText.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEditText.error = "Please provide valid email"
            binding.emailEditText.requestFocus()
            return
        }
        if (password.isEmpty()) {
            binding.passwordEditText.error = "Provide a password"
            binding.passwordEditText.requestFocus()
            return
        }
        if (password.length < 6) {
            binding.passwordEditText.error = "Password length must be at least 6 characters"
            binding.passwordEditText.requestFocus()
            return
        }
        binding.progressBar.visibility = View.VISIBLE
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult?> ->
            if (task.isSuccessful) {
                val user = User(id = task.result!!.user!!.uid, email = email, fullName = fullName)

                DatabaseReferenceRetriever.userReference(user.id).setValue(user).addOnCompleteListener { registerUserTask ->
                    if (registerUserTask.isSuccessful) {
                        Toast.makeText(this@AddUserActivity, "User has been registered successfully", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                        val intent = Intent(this, MainActivity::class.java).apply { putExtras(Bundle().apply { putString(getString(R.string.user_id), user.id) }) }
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@AddUserActivity, "Failed to register the user, please try again", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                    }
                }
            } else {
                Toast.makeText(this@AddUserActivity, "Failed to register, try again", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}