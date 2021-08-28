package it.units.musicplatform.retrievers

import com.google.firebase.storage.FirebaseStorage

private const val PROFILE_PICTURE = "profile_picture"
private val STORAGE_REFERENCE = FirebaseStorage.getInstance().reference

class StorageReferenceRetrievers {

    companion object{

        fun userImageReference(userId: String) = STORAGE_REFERENCE.child(userId).child(PROFILE_PICTURE)

    }

}