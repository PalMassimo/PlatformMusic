package it.units.musicplatform.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import it.units.musicplatform.R
import it.units.musicplatform.adapters.UserPostsAdapter
import it.units.musicplatform.databinding.FragmentEditpostDialogBinding
import it.units.musicplatform.databinding.FragmentProfileBinding
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

            val elementPosition = bundle.get("position") as Int

            when (bundle.get("operation")) {
                "edit" -> showEditPostDialog(elementPosition)
                "delete" -> deletePost(elementPosition)
                else -> Toast.makeText(context, "Asked for unknown operation", Toast.LENGTH_LONG).show()
            }

        }

        setFragmentResultListener("updated_post") { _, bundle ->
            val songName = bundle.get("songName") as String
            val artistName = bundle.get("artistName") as String
            val coverDownloadUrl = bundle.get("coverDownloadString") as String

            val elementPosition = bundle.get("element_position") as Int

            val post = adapter.userPosts[elementPosition]
            post.songName = songName
            post.artistName = artistName
            post.songPictureDownloadString = coverDownloadUrl

            adapter.notifyItemChanged(elementPosition)
            userViewModel.updatePost(post)

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

    private fun deletePost(elementPosition: Int) {
        adapter.removeElementAtPosition(elementPosition)
        userViewModel.deletePost(adapter.userPosts[elementPosition].id)
    }

    private fun showEditPostDialog(elementPosition: Int) {
        val editPostDialogFragment = EditPostDialogFragment.newInstance(adapter.userPosts[elementPosition], elementPosition)
        editPostDialogFragment.show(parentFragmentManager, editPostDialogFragment.tag)
//        val builder = AlertDialog.Builder(requireContext())
//        builder.setTitle("Edit Post Dialog")
//            .setMessage("A message")
//            .setView(R.layout.fragment_editpost_dialog)
//            .setNegativeButton("Cancel") { _, _ -> }
//            .setPositiveButton("Confirm") { _, _ ->  editPost()}
////            .setPositiveButton("Confirm") { _, _ -> Toast.makeText(context, "Positive button clicked", Toast.LENGTH_SHORT).show() }
//            .create()
//        builder.show()

    }

//    private fun editPost(){
////        val binding = FragmentEditpostDialogBinding.bind(R.layout.fragment_editpost_dialog.)
//
//        requireActivity().findViewById<View>(R.id.coverImageView) as ImageView
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}