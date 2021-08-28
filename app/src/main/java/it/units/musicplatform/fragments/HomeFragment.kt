package it.units.musicplatform.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import it.units.musicplatform.R
import it.units.musicplatform.databinding.FragmentHomeBinding
import it.units.musicplatform.viewmodels.UserViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            userId = it.getString(getString(R.string.user_id))
        }

//        val userViewModel = UserViewModel(userId!!)
//        val userViewModel : UserViewModel by viewModels()
        val userViewModel = ViewModelProviders.of(requireActivity()).get(UserViewModel::class.java)
        userViewModel.getUser().observe(this, { user -> binding.fullName.text = user.fullName })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}