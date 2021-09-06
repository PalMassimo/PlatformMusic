package it.units.musicplatform.repositories

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import it.units.musicplatform.entities.Post
import it.units.musicplatform.entities.User
import it.units.musicplatform.firebase.DatabaseTasks
import it.units.musicplatform.firebase.StorageTasks
import kotlinx.coroutines.tasks.await

class UserRepository(private val userId: String) {

    suspend fun getUser() = DatabaseTasks.getUserTask(userId).await().getValue(User::class.java)

    suspend fun getPosts(): ArrayList<Post> {
        val posts = ArrayList<Post>()
        DatabaseTasks.getUserPostsTask(userId, posts).await()
        return posts
    }

    suspend fun addPost(post: Post, localUriSong: Uri, localUriCover: Uri?): Post {

        val tasks = HashSet<Task<Unit>>()
        tasks.add(StorageTasks.addSongTask(userId, post, localUriSong))

        localUriCover?.let { tasks.add(StorageTasks.replaceCoverTask(userId, post, it)) }

        Tasks.whenAllComplete(tasks)
            .continueWithTask { DatabaseTasks.addPostTask(post) }
            .await()

        return post
    }

    suspend fun updatePost(post: Post, songName: String?, artistName: String?, localUriCover: String?): Post {

        localUriCover?.let { StorageTasks.replaceCoverTask(userId, post, Uri.parse(it)).await() }
        songName?.let { DatabaseTasks.updateSongNameTask(post.id, it) }
        artistName?.let { DatabaseTasks.updateArtistNameTask(post.id, it) }

        return post

    }

    fun addFollowing(followingId: String) = DatabaseTasks.addFollowing(userId, followingId)
    fun removeFollowing(followingId: String) = DatabaseTasks.removeFollowing(userId, followingId)

    fun addLike(postId: String) = DatabaseTasks.addLikeTask(userId, postId)
    fun removeLike(postId: String) = DatabaseTasks.removeLikeTask(userId, postId)
    fun addDislike(postId: String) = DatabaseTasks.addDislikeTask(userId, postId)
    fun removeDislike(postId: String) = DatabaseTasks.removeDislikeTask(userId, postId)

    fun deletePost(postId: String) {
        DatabaseTasks.deletePost(userId, postId)
        StorageTasks.deleteSong(userId, postId)
    }

    fun updateProfilePicture(uri: Uri) = StorageTasks.updateProfilePicture(userId, uri)


}

