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

    //    private lateinit var userViewModel: UserViewModel
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

//        userViewModel = ViewModelProviders.of(parent).get(UserViewModel::class.java)

        userId = FirebaseAuth.getInstance().currentUser!!.uid

        val songInfoLauncher = getSongInfoLauncher()
        val coverLauncher = getCoverLauncher()

        binding.songPictureImageView.setOnClickListener { coverLauncher.launch("image/*") }
        binding.shareButton.setOnClickListener { addPost() }

        songInfoLauncher.launch("audio/*")


    }

    private fun addPost() {
        val post = Post()
        post.id = DatabaseReferenceRetriever.postsReference().push().key!!
        post.uploaderId = userId!!
        post.artistName = binding.artistNameEditText.text.toString()
        post.songName = binding.songNameEditText.text.toString()
        post.numberOfSeconds = (milliseconds!! / 1000).toInt()
        post.numberOfDownloads = 0
        post.songExtension = fileExtension!!

        val songReference = StorageReferenceRetriever.songReference(userId!!, post.id)
        val coverReference = StorageReferenceRetriever.coverReference(userId!!, post.id)

        val songUploadTask = songReference.putFile(localUriSong!!).continueWithTask { songReference.downloadUrl }
        val coverUploadTask = coverReference.putFile(localUriCover!!).continueWithTask { coverReference.downloadUrl }
        Tasks.whenAllSuccess<Any>(songUploadTask, coverUploadTask).addOnSuccessListener {
            post.songFileDownloadString = it[0].toString()
            post.songPictureDownloadString = it[1].toString()

            val intent = Intent()
            intent.putExtra("post", post)
//        intent.putExtra("localUriSong", localUriSong)
//        intent.putExtra("localUriCover", localUriCover)
            setResult(RESULT_OK, intent)

            this.finish()
        }
//            DatabaseReferenceRetriever.postReference(post.id).setValue(post)
//            DatabaseReferenceRetriever.userPostReference(userId!!, post.id).setValue(true)

    }


    private fun getSongInfoLauncher() = registerForActivityResult(ActivityResultContracts.GetContent()) {
        localUriSong = it
        val mediaDataRetriever = MediaMetadataRetriever()
        mediaDataRetriever.setDataSource(this, it)
        milliseconds = mediaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
        artistName = mediaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        songName = mediaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        fileExtension = it.lastPathSegment!!.substring(it.lastPathSegment!!.lastIndexOf(".") + 1)

        binding.artistNameEditText.setText(artistName)
        binding.songNameEditText.setText(songName)

    }

    private fun getCoverLauncher() = registerForActivityResult(ActivityResultContracts.GetContent()) {
        localUriCover = it
        binding.songPictureImageView.setImageURI(it)
    }

}