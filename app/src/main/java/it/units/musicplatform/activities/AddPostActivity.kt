package it.units.musicplatform.activities

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.UploadTask
import it.units.musicplatform.databinding.ActivityAddPostBinding
import it.units.musicplatform.entities.Post
import it.units.musicplatform.retrievers.DatabaseReferenceRetriever
import it.units.musicplatform.retrievers.StorageReferenceRetriever
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class AddPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPostBinding
    private var userId: String? = null
    private var songName: String? = null
    private var artistName: String? = null
    private var localSongUri: Uri? = null
    private var localCoverUri: Uri? = null
    private var milliseconds: Long? = null
    private var fileExtension: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        val songUploadTask = songReference.putFile(localSongUri!!).continueWithTask { songReference.downloadUrl }
        val coverUploadTask = coverReference.putFile(localCoverUri!!).continueWithTask { coverReference.downloadUrl }

//        val songUriTask = songReference.putFile(localSongUri!!).continueWithTask { command: Task<UploadTask.TaskSnapshot?>? -> songReference.downloadUrl }
//        val coverUriTask = coverReference.putFile(localCoverUri!!).continueWithTask { command: Task<UploadTask.TaskSnapshot?>? -> coverReference.downloadUrl }

//        Tasks.whenAllSuccess<Any>(songUploadTask, coverUploadTask).addOnSuccessListener { uriList: List<Any> ->
//            post.songFileDownloadString = uriList[0].toString()
//            post.songPictureDownloadString = uriList[1].toString()
//            DatabaseReferenceRetriever.postReference(post.id).setValue(post)
//        }

        Tasks.whenAllSuccess<Any>(songUploadTask, coverUploadTask).addOnSuccessListener {
            post.songFileDownloadString = it[0].toString()
            post.songPictureDownloadString = it[1].toString()
            DatabaseReferenceRetriever.postReference(post.id).setValue(post)
            DatabaseReferenceRetriever.userPostReference(userId!!, post.id).setValue(true)
        }

//        GlobalScope.launch {
//
//            async { songUploadTask.getResult() }
//
//            Tasks.whenAll(songUploadTask, coverUploadTask).addOnCompleteListener {
//                post.songFileDownloadString = songUploadTask.result!!.result.toString()
//                post.songPictureDownloadString = coverUploadTask.result!!.toString()
//                DatabaseReferenceRetriever.postReference(post.id).setValue(post)
//                DatabaseReferenceRetriever.userPostReference(post.uploaderId, post.id).setValue(true)
//            }
//        }

        this.finish()

    }

    private fun getSongInfoLauncher() = registerForActivityResult(ActivityResultContracts.GetContent()) {
        localSongUri = it
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
        localCoverUri = it
        binding.songPictureImageView.setImageURI(it)
    }

}