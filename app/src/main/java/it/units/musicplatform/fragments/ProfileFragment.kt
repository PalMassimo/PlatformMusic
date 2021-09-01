package it.units.musicplatform.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import it.units.musicplatform.adapters.UserPostsAdapter
import it.units.musicplatform.databinding.FragmentProfileBinding
import it.units.musicplatform.entities.Post
import it.units.musicplatform.utilities.PictureLoader
import it.units.musicplatform.viewmodels.UserViewModel

private const val ARG_USER_ID = "user_id"

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var userId: String? = null
    private lateinit var adapter: UserPostsAdapter
    private lateinit var userViewModel: UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(ARG_USER_ID)
        }

        setFragmentResultListener("post_operation") { _, bundle ->

            when (bundle.get("operation")) {
                "edit" -> Toast.makeText(context, "Edit operation not supported yet", Toast.LENGTH_LONG).show()
                "delete" -> deletePost(bundle.get("position") as Int)
                else -> null
            }

//            Toast.makeText(context, if (bundle.get(operation) == "edit") "Edit operation clicked" else "delete operation clicked", Toast.LENGTH_SHORT).show()
//            Toast.makeText(context, bundle.get("position").toString(), Toast.LENGTH_LONG).show()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProviders.of(requireActivity()).get(UserViewModel::class.java)
        binding.userviewmodel = userViewModel
        binding.lifecycleOwner = activity

        PictureLoader.setProfileImage(userId!!, binding.profileImageView)

        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        adapter = if (userViewModel.posts.value != null) UserPostsAdapter(this, userViewModel.posts.value!!) else UserPostsAdapter(this, ArrayList())
        binding.userPostsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.userPostsRecyclerView.adapter = adapter
        userViewModel.posts.observe(viewLifecycleOwner, { adapter.userPosts = userViewModel.posts.value!! })
    }

    private fun deletePost(elementPosition: Int){
        adapter.removeElementAtPosition(elementPosition)
        userViewModel.deletePost(adapter.userPosts[elementPosition].id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}