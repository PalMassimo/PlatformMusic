package it.units.musicplatform.activities

import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import it.units.musicplatform.databinding.ActivityAddPostBinding
import it.units.musicplatform.entities.Post
import it.units.musicplatform.retrievers.DatabaseReferenceRetriever


class AddPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPostBinding

    private var userId: String? = null
    private var songName: String? = null
    private var artistName: String? = null
    private var milliseconds: Long? = null
    private var localUriSong: Uri? = null
    private var localUriCover: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("user_id")!!

        val coverLauncher = registerCoverLauncher()

        binding.songPictureImageView.setOnClickListener { coverLauncher.launch("image/*") }
        binding.shareButton.setOnClickListener {
            when {
                binding.songNameEditText.text.isBlank() -> showMissingField(binding.songNameEditText, "Please insert the song name")
                binding.artistNameEditText.text.isBlank() -> showMissingField(binding.artistNameEditText, "Please insert the artist name")
                else -> addPost()
            }
        }

        songInfoLauncher().launch("audio/*")

    }

    private fun showMissingField(editText: EditText, errorMessage : String){
        editText.requestFocus()
        editText.error = errorMessage
    }

    private fun addPost() {
        val post = Post().apply {
            id = DatabaseReferenceRetriever.posts().push().key!!
            uploaderId = userId!!
            artistName = binding.artistNameEditText.text.toString()
            songName = binding.songNameEditText.text.toString()
            numberOfSeconds = (milliseconds!! / 1000).toInt()
        }

        val intent = Intent().apply {
            putExtra("post", post)
            putExtra("localUriCover", localUriCover.toString())
            putExtra("localUriSong", localUriSong.toString())
        }

        setResult(RESULT_OK, intent)

        this.finish()

    }


    private fun songInfoLauncher() = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

        uri?.let {
            localUriSong = uri
            val mediaDataRetriever = MediaMetadataRetriever().apply { setDataSource(this@AddPostActivity, uri) }
            milliseconds = mediaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!.toLong()
            artistName = mediaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            songName = mediaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)

            binding.artistNameEditText.setText(artistName)
            binding.songNameEditText.setText(songName)
        } ?: {
            val intent = Intent().apply { putExtra("message", "To add a post choose a song from your storage")  }
            setResult(RESULT_CANCELED, intent)
            finish()
        }()


    }

    private fun registerCoverLauncher() = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            localUriCover = uri
            binding.songPictureImageView.setImageURI(uri)
        }
    }

}