package it.units.musicplatform.utilities

import android.widget.ImageView
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import it.units.musicplatform.R
import it.units.musicplatform.retrievers.StorageReferenceRetrievers

class PictureLoader {

    companion object{

        fun setProfileImage (userId: String, imageView : ImageView){
            StorageReferenceRetrievers.userImageReference(userId).downloadUrl.addOnSuccessListener {
                Picasso.get().load(it).placeholder(R.drawable.ic_profile).rotate(90F).into(imageView)
            }
        }

        fun setSongCover(userId: String, postId: String, imageView : ImageView){
            val storageReference = StorageReferenceRetrievers.coverReference(userId, postId)
            storageReference.downloadUrl.addOnSuccessListener {
                Picasso.get().load(it).placeholder(R.drawable.ic_music_note).into(imageView)
            }
        }


    }

}