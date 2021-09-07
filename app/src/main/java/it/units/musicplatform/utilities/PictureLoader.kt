package it.units.musicplatform.utilities

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import it.units.musicplatform.R
import it.units.musicplatform.firebase.retrievers.StorageReferenceRetriever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PictureLoader {

    companion object {

        fun loadCover(context: Context, imageView: ImageView, userId: String, postId: String) {
            GlideApp.with(context)
                .load(StorageReferenceRetriever.cover(userId, postId))
                .skipMemoryCache(true)
                .error(R.drawable.ic_music_note)
                .into(imageView)
        }

        fun loadProfilePicture(context: Context, imageView: ImageView, userId: String) {
            GlideApp.with(context)
                .load(StorageReferenceRetriever.userImageReference(userId))
                .skipMemoryCache(true)
                .error(R.drawable.ic_profile)
                .into(imageView)
        }

        fun cleanDisk(context: Context) {
            GlobalScope.launch(Dispatchers.Default) { Glide.get(context).clearDiskCache() }
        }
    }

}