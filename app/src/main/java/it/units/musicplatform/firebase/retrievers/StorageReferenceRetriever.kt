package it.units.musicplatform.firebase.retrievers

import com.google.firebase.storage.FirebaseStorage

private const val SONGS_CHILD = "songs"
private const val SONGS_COVERS_CHILD = "songs_cover"
private const val PROFILE_PICTURE_CHILD = "profile_picture"

private val STORAGE_REFERENCE = FirebaseStorage.getInstance().reference

class StorageReferenceRetriever {

    companion object{

        @JvmStatic fun placeHolder() = STORAGE_REFERENCE.child("not_found_placeholder.jpeg")

        @JvmStatic fun userImageReference(userId: String) = userDirectoryReference(userId).child(PROFILE_PICTURE_CHILD)
        @JvmStatic fun cover(userId: String, postId: String) = userCoversFolderReference(userId).child(postId)
        @JvmStatic fun song(userId: String, postId: String) = userSongsFolderReference(userId).child(postId)

        @JvmStatic private fun userDirectoryReference(userId: String) = STORAGE_REFERENCE.child(userId)
        @JvmStatic private fun userCoversFolderReference(userId: String) = userDirectoryReference(userId).child(SONGS_COVERS_CHILD)
        @JvmStatic private fun userSongsFolderReference(userId: String) = userDirectoryReference(userId).child(SONGS_CHILD)
    }

}