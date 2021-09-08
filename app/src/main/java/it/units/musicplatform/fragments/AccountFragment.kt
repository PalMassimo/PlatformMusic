package it.units.musicplatform.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import it.units.musicplatform.databinding.FragmentAccountBinding
import it.units.musicplatform.utilities.GlideApp
import it.units.musicplatform.utilities.PictureLoader
import it.units.musicplatform.viewmodels.UserViewModel

class AccountFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private var _binding : FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userViewModel = ViewModelProviders.of(requireActivity()).get(UserViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAccountBinding.inflate(layoutInflater)
        binding.userViewModel = userViewModel
        PictureLoader.loadProfilePicture(requireContext(), binding.profileImageView, userViewModel.userId)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}