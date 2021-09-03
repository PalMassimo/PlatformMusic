package it.units.musicplatform.repositories

import android.provider.ContactsContract
import it.units.musicplatform.retrievers.DatabaseReferenceRetriever

class PostsRepository {

    fun setNumberOfLikes(id: String, numberOfLikes: Int) = DatabaseReferenceRetriever.postNumberOfLikesReference(id).setValue(numberOfLikes)
    fun setNumberOfDislikes(id: String, numberOfDislikes:Int) = DatabaseReferenceRetriever.postNumberOfDislikesReference(id).setValue(numberOfDislikes)
    fun setNumberOfDownloads(id:String, numberOfDownloads: Int) = DatabaseReferenceRetriever.postNumberOfDownloads(id).setValue(numberOfDownloads)
}