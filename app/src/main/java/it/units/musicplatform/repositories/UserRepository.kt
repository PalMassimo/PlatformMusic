package it.units.musicplatform.repositories

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import it.units.musicplatform.entities.Post
import it.units.musicplatform.entities.User
import it.units.musicplatform.firebase.DatabaseTaskManager
import it.units.musicplatform.firebase.StorageTaskManager
import it.units.musicplatform.retrievers.DatabaseReferenceRetriever
import it.units.musicplatform.retrievers.StorageReferenceRetriever
import kotlinx.coroutines.tasks.await

class UserRepository(private val userId: String) {

    suspend fun getUser() = DatabaseTaskManager.getUserTask(userId).await().getValue(User::class.java)


    suspend fun getPosts(): ArrayList<Post> {
        val posts = ArrayList<Post>()
        DatabaseTaskManager.getUserPostsTask(userId, posts).await()
        return posts
    }

    suspend fun addPost(post: Post, localUriSong: Uri, localUriCover: Uri?): Post {

        val tasks = HashSet<Task<Unit>>()
        tasks.add(StorageTaskManager.addSongTask(userId, post, localUriSong))

        localUriCover?.let { uri ->
            tasks.add(StorageTaskManager.replaceCoverTask(userId, post, uri))
        }

        Tasks.whenAllComplete(tasks)
            .continueWithTask { DatabaseTaskManager.addPostTask(post) }
            .await()

        return post
    }

    suspend fun updatePost(post: Post, songName: String?, artistName: String?, localUriCover: String?): Post {

        localUriCover?.let { StorageTaskManager.replaceCoverTask(userId, post, Uri.parse(it)).await() }
        songName?.let { DatabaseTaskManager.updateSongNameTask(post.id, it) }
        artistName?.let { DatabaseTaskManager.updateArtistNameTask(post.id, it) }

        return post

    }

    fun addFollowing(followingId: String) {
        DatabaseTaskManager.addFollowing(userId, followingId)
//        changeNumberOfFollowers(followingId, increase = true)
    }

    fun removeFollowing(followingId: String) {
        DatabaseTaskManager.removeFollowing(userId, followingId)
//        changeNumberOfFollowers(followingId, increase = false)
    }

//    private fun changeNumberOfFollowers(followingId: String, increase: Boolean) {
//
//        DatabaseReferenceRetriever.userNumberOfFollowers(followingId).get()
//            .continueWith { it.result!!.getValue(Int::class.java) }
//            .addOnSuccessListener { numberOfFollowers ->
//                val updatedNumberOfFollowers = if (increase) numberOfFollowers!! + 1 else numberOfFollowers!! - 1
//                DatabaseReferenceRetriever.userNumberOfFollowers(followingId).setValue(updatedNumberOfFollowers)
//            }
//    }

    fun addLike(postId: String) = DatabaseReferenceRetriever.userLike(userId, postId).setValue(true)
    fun removeLike(postId: String) = DatabaseReferenceRetriever.userLike(userId, postId).removeValue()
    fun addDislike(postId: String) = DatabaseReferenceRetriever.userDislike(userId, postId).setValue(true)
    fun removeDislike(postId: String) = DatabaseReferenceRetriever.userDislike(userId, postId).removeValue()

    fun deletePost(postId: String) {
        DatabaseReferenceRetriever.userPost(userId, postId).removeValue()
        DatabaseReferenceRetriever.post(postId).removeValue()

        StorageReferenceRetriever.song(userId, postId).delete()
        StorageReferenceRetriever.cover(userId, postId).delete()
    }

    fun updateProfilePicture(uri: Uri) = StorageReferenceRetriever.userImageReference(userId).putFile(uri)


}

