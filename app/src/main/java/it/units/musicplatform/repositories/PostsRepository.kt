package it.units.musicplatform.repositories

import it.units.musicplatform.entities.Post
import it.units.musicplatform.retrievers.DatabaseReferenceRetriever
import kotlinx.coroutines.tasks.await
import java.util.stream.StreamSupport

class PostsRepository {

    fun setNumberOfLikes(id: String, numberOfLikes: Int) = DatabaseReferenceRetriever.postNumberOfLikes(id).setValue(numberOfLikes)
    fun setNumberOfDislikes(id: String, numberOfDislikes: Int) = DatabaseReferenceRetriever.postNumberOfDislikes(id).setValue(numberOfDislikes)
    fun setNumberOfDownloads(id: String, numberOfDownloads: Int) = DatabaseReferenceRetriever.postNumberOfDownloads(id).setValue(numberOfDownloads)

    suspend fun getUserPost(userId: String): ArrayList<Post> {

        val followingPosts = ArrayList<Post>()

        DatabaseReferenceRetriever.posts().orderByChild("uploaderId").equalTo(userId).get().addOnSuccessListener {
            StreamSupport.stream(it.children.spliterator(), true)
                .map { postSnapshot -> postSnapshot.getValue(Post::class.java)!! }
                .forEach { post -> followingPosts.add(post) }
        }.await()
        return followingPosts
    }

}