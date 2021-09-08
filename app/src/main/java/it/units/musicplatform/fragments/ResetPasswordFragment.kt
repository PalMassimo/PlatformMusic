package it.units.musicplatform.fragments

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import it.units.musicplatform.R
import it.units.musicplatform.databinding.FragmentHomeBinding
import it.units.musicplatform.databinding.FragmentResetPasswordBinding

class ResetPasswordFragment : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentResetPasswordBinding.inflate(layoutInflater)

        binding.sendEmailButton.setOnClickListener {
            when {
                isEmailAddressBlank() -> showErrorMessage("please insert your email address")
                isEmailAddressValid() -> showErrorMessage("please insert a valid email address")
                else -> sendResetPasswordEmail()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun sendResetPasswordEmail() {
        binding.sendEmailProgressBar.visibility = View.VISIBLE
        FirebaseAuth.getInstance().sendPasswordResetEmail(binding.resetEmailEditText.text.toString()).addOnCompleteListener {
            binding.sendEmailProgressBar.visibility = View.GONE
            val resultMessage = if (it.isSuccessful) "The email was sent correctly, please check your mailbox" else "Something went wrong. Please try again"
            Toast.makeText(requireContext(), resultMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun showErrorMessage(errorMessage: String) {
        binding.resetEmailEditText.requestFocus()
        binding.resetEmailEditText.error = errorMessage
    }

    private fun isEmailAddressValid() = !Patterns.EMAIL_ADDRESS.matcher(binding.resetEmailEditText.text.toString()).matches()
    private fun isEmailAddressBlank() = binding.resetEmailEditText.text.isBlank()
}