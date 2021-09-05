package it.units.musicplatform.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import it.units.musicplatform.adapters.UserPostsAdapter
import it.units.musicplatform.databinding.FragmentProfileBinding
import it.units.musicplatform.utilities.PictureLoader
import it.units.musicplatform.viewmodels.FollowersPostsViewModel
import it.units.musicplatform.viewmodels.UserViewModel

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var userId: String
    private lateinit var adapter: UserPostsAdapter
    private lateinit var userViewModel: UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListeners()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        userId = userViewModel.userId
        binding.userviewmodel = userViewModel

        val newProfileImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                userViewModel.updateProfilePicture(it)
                Glide.with(requireContext()).load(it).into(binding.profileImageView)
            }
        }

        binding.profileImageView.setOnClickListener { newProfileImageLauncher.launch("image/*") }
        PictureLoader.loadProfilePicture(requireContext(), binding.profileImageView, userId)

        setUpRecyclerView()

    }

    private fun setFragmentResultListeners() {
        setFragmentResultListener("post_operation") { _, bundle ->

            val position = bundle.getInt("position")

            when (bundle.get("operation")) {
                "edit" -> EditPostDialogFragment.newInstance(userViewModel.posts.value!![position], position, userId).run { show(this@ProfileFragment.parentFragmentManager, tag) }
                "delete" -> deletePost(position)
                else -> Toast.makeText(context, "Asked for unknown operation", Toast.LENGTH_LONG).show()
            }

        }

        setFragmentResultListener("updated_post") { _, bundle ->
            bundle.let {
                val postPosition = it.getInt("element_position")
                userViewModel.updatePost(postPosition, it.getString("songName"), it.getString("artistName"), it.getString("localUriCover"))
            }
        }
    }

    private fun setUpRecyclerView() {

        adapter = UserPostsAdapter(this, userViewModel.user.value!!.posts.keys.toList(), userViewModel.posts.value!!)
        binding.userPostsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.userPostsRecyclerView.adapter = adapter
        userViewModel.posts.observe(requireActivity(), {
            adapter.posts = userViewModel.posts.value!!
            adapter.postsIds = userViewModel.user.value!!.posts.keys.toList()
            adapter.notifyDataSetChanged()
        })

    }

    private fun deletePost(position: Int) {
        userViewModel.deletePost(adapter.posts[position].id)
        adapter.notifyItemRemoved(position)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}