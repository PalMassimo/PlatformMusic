package it.units.musicplatform.repositories

import it.units.musicplatform.retrievers.DatabaseReferenceRetriever

class PostsRepository {

    fun setNumberOfLikes(id: String, numberOfLikes: Int) = DatabaseReferenceRetriever.postNumberOfLikes(id).setValue(numberOfLikes)
    fun setNumberOfDislikes(id: String, numberOfDislikes:Int) = DatabaseReferenceRetriever.postNumberOfDislikes(id).setValue(numberOfDislikes)
    fun setNumberOfDownloads(id:String, numberOfDownloads: Int) = DatabaseReferenceRetriever.postNumberOfDownloads(id).setValue(numberOfDownloads)

}