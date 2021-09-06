package it.units.musicplatform.activities

import android.os.Bundle
import android.util.Patterns
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

        binding.instructionTextView.text = getString(R.string.reset_password_instruction)

        binding.sendEmailButton.setOnClickListener {
            when {
                binding.resetEmailEditText.text.isBlank() -> showErrorMessage("please insert your email address")
                isEmailAddressValid() -> showErrorMessage("please insert a valid email address")
                else -> sendResetPasswordEmail()
            }
        }

    }

    private fun sendResetPasswordEmail() {
        FirebaseAuth.getInstance().sendPasswordResetEmail(binding.resetEmailEditText.text.toString()).addOnCompleteListener {
            val resultMessage = if(it.isSuccessful) "The email was sent correctly, please check your mailbox" else "Something went wrong. Please try again"
            Toast.makeText(this, resultMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun showErrorMessage(errorMessage: String) {
        binding.resetEmailEditText.requestFocus()
        binding.resetEmailEditText.error = errorMessage
    }

    private fun isEmailAddressValid() = !Patterns.EMAIL_ADDRESS.matcher(binding.resetEmailEditText.text.toString()).matches()
}