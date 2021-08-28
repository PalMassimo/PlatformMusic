package it.units.musicplatform.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import it.units.musicplatform.R
import it.units.musicplatform.databinding.FragmentProfileBinding
import it.units.musicplatform.viewmodels.UserViewModel

private const val ARG_USER_ID = "user_id"

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(ARG_USER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userViewModel = ViewModelProviders.of(requireActivity()).get(UserViewModel::class.java)
        binding.userviewmodel = userViewModel
        binding.lifecycleOwner = activity

//        Picasso.get().load(userViewModel.getUser().value.)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}