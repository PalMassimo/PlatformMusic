package it.units.musicplatform.fragments

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.bumptech.glide.Glide
import it.units.musicplatform.R
import it.units.musicplatform.databinding.FragmentEditpostDialogBinding
import it.units.musicplatform.entities.Post
import it.units.musicplatform.utilities.PictureLoader

class EditPostDialogFragment : DialogFragment() {

    private lateinit var post: Post
    private var elementPosition: Int? = null
    private var localImageUri: Uri? = null
    private var _binding: FragmentEditpostDialogBinding? = null
    private val binding get() = _binding!!

    private var _userId: String? = null
    private val userId get() = _userId!!

    private val uriLauncherActivity = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            Glide.with(requireContext()).load(uri).into(binding.coverImageView)
            localImageUri = uri
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(post: Post, elementPosition: Int, userId: String) = EditPostDialogFragment().apply {
            this.post = post
            this.elementPosition = elementPosition
            this._userId = userId
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentEditpostDialogBinding.inflate(LayoutInflater.from(context))

        binding.coverImageView.setOnClickListener { uriLauncherActivity.launch("image/*") }
        binding.songNameEditText.setText(post.songName)
        binding.artistNameEditText.setText(post.artistName)
        PictureLoader.loadCover(requireContext(), binding.coverImageView, userId, post.id)

        val builder = AlertDialog.Builder(requireContext()).apply {
            setView(binding.root)
            setIcon(R.mipmap.ic_launcher_round)
            setTitle("Edit your post")
            setNeutralButton("Cancel") { _, _ -> }
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