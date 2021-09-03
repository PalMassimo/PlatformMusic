package it.units.musicplatform.fragments

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.firebase.auth.FirebaseAuth
import it.units.musicplatform.databinding.FragmentEditpostDialogBinding
import it.units.musicplatform.entities.Post
import it.units.musicplatform.retrievers.StorageReferenceRetriever
import it.units.musicplatform.utilities.GlideApp

class EditPostDialogFragment : DialogFragment() {

    private lateinit var post: Post
    private var elementPosition: Int? = null
    private var localImageUri: Uri? = null
    private var _binding: FragmentEditpostDialogBinding? = null
    private val binding get() = _binding!!
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid
    private val uriLauncherActivity = registerForActivityResult(ActivityResultContracts.GetContent()) {
        binding.coverImageView.setImageURI(it)
        localImageUri = it
    }

    companion object {
        @JvmStatic
        fun newInstance(post: Post, elementPosition: Int) = EditPostDialogFragment().apply {
            this.post = post
            this.elementPosition = elementPosition
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentEditpostDialogBinding.inflate(LayoutInflater.from(context))

        binding.coverImageView.setOnClickListener { uriLauncherActivity.launch("image/*") }
        binding.songNameEditText.setText(post.songName)
        binding.artistNameEditText.setText(post.artistName)
        GlideApp.with(requireContext()).load(StorageReferenceRetriever.coverReference(userId, post.id)).into(binding.coverImageView)

        val builder = AlertDialog.Builder(requireContext()).apply {
            setView(binding.root)
            setTitle("Edit your post")
            setNegativeButton("Cancel") { _, _ -> }
            setPositiveButton("Confirm") { _, _ ->
                editPost(binding.songNameEditText.text.toString(), binding.artistNameEditText.text.toString())
            }
        }

        return builder.create()

    }


    private fun editPost(songName: String, artistName: String) {
        post.songName = songName
        post.artistName = artistName

        Bundle().run {
            putString("songName", post.songName)
            putString("artistName", post.artistName)
            putString("localUriCover", localImageUri?.toString())
            putInt("element_position", elementPosition!!)
            setFragmentResult("updated_post", this)
            dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}