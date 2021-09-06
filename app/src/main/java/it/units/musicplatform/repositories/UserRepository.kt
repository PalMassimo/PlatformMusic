package it.units.musicplatform.repositories

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import it.units.musicplatform.entities.Post
import it.units.musicplatform.entities.User
import it.units.musicplatform.firebase.DatabaseTaskManager
import it.units.musicplatform.firebase.StorageTaskManager
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

        localUriCover?.let { tasks.add(StorageTaskManager.replaceCoverTask(userId, post, it)) }

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

    fun addFollowing(followingId: String) = DatabaseTaskManager.addFollowing(userId, followingId)
    fun removeFollowing(followingId: String) = DatabaseTaskManager.removeFollowing(userId, followingId)

    fun addLike(postId: String) = DatabaseTaskManager.addLikeTask(userId, postId)
    fun removeLike(postId: String) = DatabaseTaskManager.removeLikeTask(userId, postId)
    fun addDislike(postId: String) = DatabaseTaskManager.addDislikeTask(userId, postId)
    fun removeDislike(postId: String) = DatabaseTaskManager.removeDislikeTask(userId, postId)

    fun deletePost(postId: String) {
        DatabaseTaskManager.deletePost(userId, postId)
        StorageTaskManager.deleteSong(userId, postId)
    }

    fun updateProfilePicture(uri: Uri) = StorageTaskManager.updateProfilePicture(userId, uri)


}

