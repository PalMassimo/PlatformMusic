package it.units.musicplatform.activities

import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import it.units.musicplatform.databinding.ActivityAddPostBinding
import it.units.musicplatform.entities.Post
import it.units.musicplatform.retrievers.DatabaseReferenceRetriever
import it.units.musicplatform.retrievers.StorageReferenceRetriever


class AddPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPostBinding

    private var userId: String? = null
    private var songName: String? = null
    private var artistName: String? = null
    private var localUriSong: Uri? = null
    private var localUriCover: Uri? = null
    private var milliseconds: Long? = null
    private var fileExtension: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = FirebaseAuth.getInstance().currentUser!!.uid

        val coverLauncher = registerCoverLauncher()

        binding.songPictureImageView.setOnClickListener { coverLauncher.launch("image/*") }
        binding.shareButton.setOnClickListener { addPost() }

        songInfoLauncher().launch("audio/*")


    }

    private fun addPost() {
        val post = Post().apply {
            id = DatabaseReferenceRetriever.postsReference().push().key!!
            uploaderId = userId!!
            artistName = binding.artistNameEditText.text.toString()
            songName = binding.songNameEditText.text.toString()
            numberOfSeconds = (milliseconds!! / 1000).toInt()
            numberOfDownloads = 0
            songExtension = fileExtension!!
        }

        val songReference = StorageReferenceRetriever.songReference(userId!!, post.id)
        val coverReference = StorageReferenceRetriever.coverReference(userId!!, post.id)

        val songUploadTask = songReference.putFile(localUriSong!!).continueWithTask { songReference.downloadUrl }
        val coverUploadTask = coverReference.putFile(localUriCover!!).continueWithTask { coverReference.downloadUrl }
        Tasks.whenAllSuccess<Any>(songUploadTask, coverUploadTask).addOnSuccessListener {
            post.songFileDownloadString = it[0].toString()
            post.songPictureDownloadString = it[1].toString()

            val intent = Intent().apply { putExtra("post", post) }
            setResult(RESULT_OK, intent)

            this.finish()
        }

    }


    private fun songInfoLauncher() = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        localUriSong = uri
        val mediaDataRetriever = MediaMetadataRetriever().apply { setDataSource(this@AddPostActivity, uri) }
        milliseconds = mediaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
        artistName = mediaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        songName = mediaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        fileExtension = uri.lastPathSegment!!.substring(uri.lastPathSegment!!.lastIndexOf(".") + 1)

        binding.artistNameEditText.setText(artistName)
        binding.songNameEditText.setText(songName)

    }

    private fun registerCoverLauncher() = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        localUriCover = uri
        binding.songPictureImageView.setImageURI(uri)
    }

}