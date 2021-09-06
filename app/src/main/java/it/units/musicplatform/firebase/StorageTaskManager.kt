package it.units.musicplatform.firebase

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.StorageReference
import it.units.musicplatform.entities.Post
import it.units.musicplatform.retrievers.StorageReferenceRetriever
import kotlinx.coroutines.tasks.await

class StorageTaskManager {

    companion object {

        @JvmStatic
        fun replaceCoverTask(userId: String, post: Post, localUri: Uri): Task<Unit> {
            return StorageReferenceRetriever.cover(userId, post.id).putFile(localUri)
                .continueWithTask { it.result!!.storage.downloadUrl }
                .continueWith { uriTask -> post.coverDownloadString = uriTask.result.toString() }
        }

        @JvmStatic
        fun addSongTask(userId: String, post: Post, localUri: Uri): Task<Unit> {
            return StorageReferenceRetriever.song(userId, post.id).putFile(localUri)
                .continueWithTask { it.result!!.storage.downloadUrl }
                .continueWith { uriTask -> post.songDownloadString = uriTask.result.toString() }
        }

    }

}