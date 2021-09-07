package it.units.musicplatform.activities

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import it.units.musicplatform.R
import it.units.musicplatform.databinding.ActivityResetPasswordBinding

class ResetPasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendEmailButton.setOnClickListener {
            when {
                isEmailAddressBlank() -> showErrorMessage("please insert your email address")
                isEmailAddressValid() -> showErrorMessage("please insert a valid email address")
                else -> sendResetPasswordEmail()
            }
        }

    }

    private fun sendResetPasswordEmail() {
        binding.sendEmailProgressBar.visibility = View.VISIBLE
        FirebaseAuth.getInstance().sendPasswordResetEmail(binding.resetEmailEditText.text.toString()).addOnCompleteListener {
            binding.sendEmailProgressBar.visibility = View.GONE
            val resultMessage = if(it.isSuccessful) "The email was sent correctly, please check your mailbox" else "Something went wrong. Please try again"
            Toast.makeText(this, resultMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun showErrorMessage(errorMessage: String) {
        binding.resetEmailEditText.requestFocus()
        binding.resetEmailEditText.error = errorMessage
    }

    private fun isEmailAddressValid() = !Patterns.EMAIL_ADDRESS.matcher(binding.resetEmailEditText.text.toString()).matches()
    private fun isEmailAddressBlank() = binding.resetEmailEditText.text.isBlank()
}