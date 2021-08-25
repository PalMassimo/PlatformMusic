package it.units.musicplatform.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import it.units.musicplatform.R
import it.units.musicplatform.databinding.FragmentHomeBinding
import it.units.musicplatform.entities.User
import it.units.musicplatform.viewmodels.UserViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var userId: String? = null
//    private val userViewModel = ViewModelProviders.of
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            userId = it.getString(getString(R.string.user_id))
        }

        val userViewModel = UserViewModel(userId!!)
//        val userViewModel : UserViewModel by viewModels()
        userViewModel.getUser().observe(this, { user -> binding.fullName.setText(user.fullName) })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}