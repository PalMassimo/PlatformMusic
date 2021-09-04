package it.units.musicplatform.retrievers

import com.google.firebase.storage.FirebaseStorage

private const val PROFILE_PICTURE_CHILD = "profile_picture"
private const val DEFAULT_PROFILE_PICTURE_CHILD = "profile_picture_placeholder.png"
private const val SONG_PICTURES_CHILD = "song_pictures"
private val STORAGE_REFERENCE = FirebaseStorage.getInstance().reference

class StorageReferenceRetriever {

    companion object{

        fun profilePicturePlaceholder() = STORAGE_REFERENCE.child(DEFAULT_PROFILE_PICTURE_CHILD)
        fun userImageReference(userId: String) = userDirectoryReference(userId).child(PROFILE_PICTURE_CHILD)
        fun coverReference(userId: String, postId: String) = userCoversFolderReference(userId).child(postId)
        fun songReference(userId: String, postId: String) = userSongsFolderReference(userId).child(postId)

        private fun userDirectoryReference(userId: String) = STORAGE_REFERENCE.child(userId)
        private fun userCoversFolderReference(userId: String) = userDirectoryReference(userId).child(SONG_PICTURES_CHILD)
        private fun userSongsFolderReference(userId: String) = userDirectoryReference(userId).child("songs")
    }

}