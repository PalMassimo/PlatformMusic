package it.units.musicplatform.repositories

import com.google.firebase.database.DataSnapshot
import it.units.musicplatform.entities.Post
import it.units.musicplatform.entities.User
import it.units.musicplatform.retrievers.DatabaseReferenceRetriever
import kotlinx.coroutines.tasks.await
import java.util.stream.StreamSupport

class UserRepository(val userId: String) {

    suspend fun getUser() = DatabaseReferenceRetriever.userReference(userId).get().await().getValue(User::class.java)

    suspend fun getPosts(): ArrayList<Post> {
        val posts = ArrayList<Post>()
        DatabaseReferenceRetriever.postsReference().get().addOnSuccessListener { filterUserPosts(it, posts) }.await()
        return posts
    }

    private fun filterUserPosts(postsSnapshot: DataSnapshot, postsList: ArrayList<Post>) {
        StreamSupport.stream(postsSnapshot.children.spliterator(), false)
            .map { it.getValue(Post::class.java) }
            .filter { it!!.uploaderId == userId }
            .forEach { postsList.add(it!!) }
    }
}
