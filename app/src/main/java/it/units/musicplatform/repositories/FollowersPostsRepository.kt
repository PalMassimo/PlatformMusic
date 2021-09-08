package it.units.musicplatform.repositories

import it.units.musicplatform.entities.Post
import it.units.musicplatform.entities.User
import it.units.musicplatform.firebase.DatabaseTasks
import kotlinx.coroutines.tasks.await

class FollowersPostsRepository {

    fun setNumberOfLikes(id: String, numberOfLikes: Int) = DatabaseTasks.setNumberOfLikesTask(id, numberOfLikes)
    fun setNumberOfDislikes(id: String, numberOfDislikes: Int) = DatabaseTasks.setNumberOfDislikesTask(id, numberOfDislikes)
    fun setNumberOfDownloads(id: String, numberOfDownloads: Int) = DatabaseTasks.setNumberOfDownloadsTask(id, numberOfDownloads)

    suspend fun getUserPosts(userId: String): ArrayList<Post> {
        val userPosts = ArrayList<Post>()
        DatabaseTasks.getUserPostTask(userId, userPosts).await()
        return userPosts
    }

    suspend fun getFollowingUsername(followingId: String) = DatabaseTasks.getUserTask(followingId).await().getValue(User::class.java)!!.username
    suspend fun getFollowingUsernames(userId: String) = DatabaseTasks.getFollowingUsernames(userId)

    suspend fun loadPosts(userId: String) = DatabaseTasks.loadUserPosts(userId)


}