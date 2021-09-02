package it.units.musicplatform.fragments

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import it.units.musicplatform.databinding.FragmentEditpostDialogBinding
import it.units.musicplatform.entities.Post
import it.units.musicplatform.retrievers.StorageReferenceRetriever
import it.units.musicplatform.utilities.GlideApp
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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

        return AlertDialog.Builder(requireContext()).create().apply {
            setView(binding.root)
            setTitle("Edit Post Dialog")
            setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { _, _ -> }
            setButton(AlertDialog.BUTTON_POSITIVE, "Confirm") { _, _ ->
                lifecycleScope.launch { editPost(binding.songNameEditText.text.toString(), binding.artistNameEditText.text.toString()) }
            }
            show()
            getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                lifecycleScope.launch { editPost(binding.songNameEditText.text.toString(), binding.artistNameEditText.text.toString()) }
            }
        }

    }


    private suspend fun editPost(songName: String, artistName: String) {
        post.songName = songName
        post.artistName = artistName
        localImageUri?.let {
            StorageReferenceRetriever.coverReference(userId, post.id).putFile(it).continueWithTask {
                StorageReferenceRetriever.coverReference(userId, post.id).downloadUrl
            }.continueWith { uriTask ->
                post.songPictureDownloadString = uriTask.result.toString()
            }.await()
        }

        Bundle().run {
            putString("songName", post.songName)
            putString("artistName", post.artistName)
            putString("coverDownloadString", post.songPictureDownloadString)
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