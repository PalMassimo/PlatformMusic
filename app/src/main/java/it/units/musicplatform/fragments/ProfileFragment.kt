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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import it.units.musicplatform.R
import it.units.musicplatform.adapters.UserPostsAdapter
import it.units.musicplatform.databinding.FragmentProfileBinding
import it.units.musicplatform.retrievers.StorageReferenceRetriever
import it.units.musicplatform.utilities.GlideApp
import it.units.musicplatform.viewmodels.UserViewModel
import it.units.musicplatform.viewmodels.factories.UserViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var userId = FirebaseAuth.getInstance().currentUser!!.uid
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
        userViewModel = ViewModelProvider(requireActivity(), UserViewModelFactory(userId)).get(UserViewModel::class.java)
        binding.userviewmodel = userViewModel

        val newProfileImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                binding.profileImageView.setImageURI(uri)
                userViewModel.updateProfilePicture(it)
                requireActivity().lifecycleScope.launch(Dispatchers.Default) { GlideApp.get(requireContext()).clearDiskCache() }
            }
        }

        binding.profileImageView.setOnClickListener { newProfileImageLauncher.launch("image/*") }

        GlideApp.with(requireContext())
            .load(StorageReferenceRetriever.userImageReference(userId))
            .skipMemoryCache(true)
            .error(R.drawable.ic_profile)
            .into(binding.profileImageView)

        setUpRecyclerView()

    }

    private fun setFragmentResultListeners() {
        setFragmentResultListener("post_operation") { _, bundle ->

            val position = bundle.getInt("position")

            when (bundle.get("operation")) {
                "edit" -> EditPostDialogFragment.newInstance(userViewModel.posts.value!![position], position).run { show(this@ProfileFragment.parentFragmentManager, tag) }
                "delete" -> deletePost(position)
                else -> Toast.makeText(context, "Asked for unknown operation", Toast.LENGTH_LONG).show()
            }

        }

        setFragmentResultListener("updated_post") { _, bundle ->
            val postPosition = bundle.getInt("element_position")
            userViewModel.updatePost(postPosition, bundle.getString("songName"), bundle.getString("artistName"), bundle.getString("localUriCover"))
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