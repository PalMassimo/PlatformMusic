package it.units.musicplatform.utilities

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import it.units.musicplatform.R
import it.units.musicplatform.retrievers.StorageReferenceRetriever

class PictureLoader {

    companion object {

        fun loadCover(context: Context, imageView: ImageView, userId: String, postId: String) {
            GlideApp.with(context)
                .load(StorageReferenceRetriever.cover(userId, postId))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_music_note)
                .into(imageView)
        }

        fun loadProfilePicture(context: Context, imageView: ImageView, userId: String){
            GlideApp.with(context)
                .load(StorageReferenceRetriever.userImageReference(userId))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_profile)
                .into(imageView)
        }
    }

}