package it.units.musicplatform.firebase

import android.net.Uri
import com.google.android.gms.tasks.Task
import it.units.musicplatform.entities.Post
import it.units.musicplatform.firebase.retrievers.StorageReferenceRetriever

class StorageTasks {

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

        @JvmStatic
        fun deleteSong(userId: String, postId: String){
            StorageReferenceRetriever.song(userId, postId).delete()
            StorageReferenceRetriever.cover(userId, postId).delete()
        }

        @JvmStatic
        fun updateProfilePicture(userId: String, uri: Uri) = StorageReferenceRetriever.userImageReference(userId).putFile(uri)

    }

}