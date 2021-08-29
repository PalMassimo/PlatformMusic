package it.units.musicplatform.retrievers

import com.google.firebase.storage.FirebaseStorage

private const val PROFILE_PICTURE = "profile_picture"
private val STORAGE_REFERENCE = FirebaseStorage.getInstance().reference

class StorageReferenceRetriever {

    companion object{

        fun userImageReference(userId: String) = userDirectoryReference(userId).child(PROFILE_PICTURE)
        fun coverReference(userId: String, postId: String) = userCoversFolderReference(userId).child(postId)
        fun songReference(userId: String, postId: String) = userSongsFolderReference(userId).child(postId)

        private fun userDirectoryReference(userId: String) = STORAGE_REFERENCE.child(userId)
        private fun userCoversFolderReference(userId: String) = userDirectoryReference(userId).child("song_pictures")
        private fun userSongsFolderReference(userId: String) = userDirectoryReference(userId).child("songs")
    }

}