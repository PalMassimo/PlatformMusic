package it.units.musicplatform.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import it.units.musicplatform.R
import it.units.musicplatform.activities.MainActivity
import it.units.musicplatform.databinding.FragmentAddUserBinding
import it.units.musicplatform.entities.User
import it.units.musicplatform.firebase.retrievers.DatabaseReferenceRetriever

class AddUserFragment : Fragment() {

    private var _binding: FragmentAddUserBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentAddUserBinding.inflate(layoutInflater)

        binding.signUpButton.setOnClickListener {
            when {
                binding.usernameEditText.text.isBlank() -> showError(binding.usernameEditText, "please insert a username")
                binding.emailEditText.text.isBlank() -> showError(binding.emailEditText, "please insert an email address")
                isEmailAddressValid() -> showError(binding.emailEditText, "please provide a valid email address")
                binding.passwordEditText.text.isBlank() -> showError(binding.passwordEditText, "please insert a password")
                isPasswordValid() -> showError(binding.passwordEditText, "a password must have at least six characters")
                else -> registerUser(binding.usernameEditText.text.toString(), binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showError(editText: EditText, errorMessage: String) {
        editText.requestFocus()
        editText.error = errorMessage
    }

    private fun isPasswordValid() = binding.passwordEditText.text.toString().length < 6

    private fun isEmailAddressValid() = !Patterns.EMAIL_ADDRESS.matcher(binding.emailEditText.text.toString()).matches()

    private fun registerUser(username: String, email: String, password: String) {

        binding.progressBar.visibility = View.VISIBLE
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnSuccessListener { task ->

            val user = User(id = task.user!!.uid, email = email, username = username)

            DatabaseReferenceRetriever.user(user.id).setValue(user).addOnSuccessListener {
                binding.progressBar.visibility = View.INVISIBLE
                val intent = Intent(requireContext(), MainActivity::class.java).apply { putExtras(Bundle().apply { putString(getString(R.string.user_id), user.id) }) }
                startActivity(intent)
            }
        }
    }

}