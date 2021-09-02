package it.units.musicplatform.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import it.units.musicplatform.adapters.UserPostsAdapter
import it.units.musicplatform.databinding.FragmentProfileBinding
import it.units.musicplatform.retrievers.StorageReferenceRetriever
import it.units.musicplatform.utilities.GlideApp
import it.units.musicplatform.viewmodels.UserViewModel

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
        userViewModel = ViewModelProviders.of(requireActivity()).get(UserViewModel::class.java)
        binding.userviewmodel = userViewModel

        GlideApp.with(requireContext()).load(StorageReferenceRetriever.userImageReference(userId)).into(binding.profileImageView)

        setUpRecyclerView()

    }

    private fun setFragmentResultListeners() {
        setFragmentResultListener("post_operation") { _, bundle ->

            val elementPosition = bundle.getInt("position")

            when (bundle.get("operation")) {
                "edit" -> EditPostDialogFragment.newInstance(adapter.userPosts[elementPosition], elementPosition).run { show(this@ProfileFragment.parentFragmentManager, tag) }
                "delete" -> deletePost(elementPosition)
                else -> Toast.makeText(context, "Asked for unknown operation", Toast.LENGTH_LONG).show()
            }

        }

        setFragmentResultListener("updated_post") { _, bundle ->

            val elementPosition = bundle.getInt("element_position")

            val post = adapter.userPosts[elementPosition].apply {
                bundle.getString("songName")?.let { songName = it }
                bundle.getString("artistName")?.let { artistName = it }
                bundle.getString("coverDownloadString")?.let { songPictureDownloadString = it }
            }

            adapter.notifyItemChanged(elementPosition)
            userViewModel.updatePost(post)

        }
    }


    private fun setUpRecyclerView() {
        adapter = if (userViewModel.posts.value != null) UserPostsAdapter(this, userViewModel.posts.value!!) else UserPostsAdapter(this, ArrayList())
        binding.userPostsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.userPostsRecyclerView.adapter = adapter
        userViewModel.posts.observe(viewLifecycleOwner, { adapter.userPosts = userViewModel.posts.value!! })
    }

    private fun deletePost(elementPosition: Int) {
        adapter.removeElementAtPosition(elementPosition)
        userViewModel.deletePost(adapter.userPosts[elementPosition].id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}