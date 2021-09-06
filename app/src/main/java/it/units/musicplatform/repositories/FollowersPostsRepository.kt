package it.units.musicplatform.repositories

import it.units.musicplatform.entities.Post
import it.units.musicplatform.entities.User
import it.units.musicplatform.firebase.DatabaseTaskManager
import kotlinx.coroutines.tasks.await

class FollowersPostsRepository {

    fun setNumberOfLikes(id: String, numberOfLikes: Int) = DatabaseTaskManager.setNumberOfLikesTask(id, numberOfLikes)
    fun setNumberOfDislikes(id: String, numberOfDislikes: Int) = DatabaseTaskManager.setNumberOfDislikesTask(id, numberOfDislikes)
    fun setNumberOfDownloads(id: String, numberOfDownloads: Int) = DatabaseTaskManager.setNumberOfDownloadsTask(id, numberOfDownloads)

    suspend fun getUserPosts(userId: String): ArrayList<Post> {
        val userPosts = ArrayList<Post>()
        DatabaseTaskManager.getUserPostTask(userId, userPosts).await()
        return userPosts
    }

    suspend fun getFollowingUsername(followingId: String) = DatabaseTaskManager.getUserTask(followingId).await().getValue(User::class.java)!!.username
    suspend fun getFollowingUsernames(userId: String) = DatabaseTaskManager.getFollowingUsernames(userId)


}