package it.units.musicplatform.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import it.units.musicplatform.R
import it.units.musicplatform.activities.MainActivity
import it.units.musicplatform.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)

        binding.loginButton.setOnClickListener { login(binding.emailEditText.text.toString().trim(), binding.passwordEditText.text.toString()) }

        binding.registerUserTextView.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.welcomefragmentContainer, AddUserFragment()).addToBackStack("tag").commit()
        }

        binding.forgotPasswordTextView.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.welcomefragmentContainer, ResetPasswordFragment()).addToBackStack("tag").commit()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun login(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                startActivity(Intent(requireContext(), MainActivity::class.java))
            } else {
                Toast.makeText(requireContext(), "Login failed, check credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}