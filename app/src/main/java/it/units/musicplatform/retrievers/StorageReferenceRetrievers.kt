package it.units.musicplatform.retrievers

import com.google.firebase.storage.FirebaseStorage

private const val PROFILE_PICTURE = "profile_picture"
private val STORAGE_REFERENCE = FirebaseStorage.getInstance().reference

class StorageReferenceRetrievers {

    companion object{

        fun userImageReference(userId: String) = userStorageReference(userId).child(PROFILE_PICTURE)
        fun coverReference(userId: String, postId: String) = userCoversStorageReference(userId).child(postId)

        private fun userStorageReference(userId: String) = STORAGE_REFERENCE.child(userId)
        private fun userCoversStorageReference(userId: String) = userStorageReference(userId).child("song_pictures")
    }

}