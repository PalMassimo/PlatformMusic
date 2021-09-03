package it.units.musicplatform.repositories

import android.net.Uri
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import it.units.musicplatform.entities.Post
import it.units.musicplatform.entities.User
import it.units.musicplatform.retrievers.DatabaseReferenceRetriever
import it.units.musicplatform.retrievers.StorageReferenceRetriever
import kotlinx.coroutines.tasks.await
import java.util.stream.StreamSupport

class UserRepository(private val userId: String) {

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

    suspend fun addPost(post: Post, localUriSong: Uri, localUriCover: Uri): Post {

        val addCoverTask = StorageReferenceRetriever.coverReference(userId, post.id).putFile(localUriCover).continueWithTask {
            it.result!!.storage.downloadUrl
        }.continueWith { uriTask -> post.songFileDownloadString = uriTask.result.toString() }

        val addSongTask = StorageReferenceRetriever.songReference(userId, post.id).putFile(localUriSong).continueWithTask {
            it.result!!.storage.downloadUrl
        }.continueWith { uriTask -> post.songPictureDownloadString = uriTask.result.toString() }

        Tasks.whenAllComplete(addCoverTask, addSongTask).continueWith {
            DatabaseReferenceRetriever.postReference(post.id).setValue(post)
            DatabaseReferenceRetriever.userPostReference(post.uploaderId, post.id).setValue(true)
        }.await()

        return post
    }

    fun updatePost(id: String, songName: String?, artistName: String?, coverDownloadString: String?) {
        songName?.let { DatabaseReferenceRetriever.postSongNameReference(id).setValue(it) }
        artistName?.let { DatabaseReferenceRetriever.postArtistNameReference(id).setValue(it) }
        coverDownloadString?.let { DatabaseReferenceRetriever.postCoverDownloadString(id).setValue(it) }
    }

    fun addFollowing(followingId: String) {
        DatabaseReferenceRetriever.userFollowingReference(userId).child(followingId).setValue(true)
        DatabaseReferenceRetriever.userFollowersReference(followingId).child(userId).setValue(true)
    }

    fun removeFollowing(followingId: String) {
        DatabaseReferenceRetriever.userFollowingReference(userId).child(followingId).removeValue()
        DatabaseReferenceRetriever.userFollowersReference(followingId).child(userId).removeValue()
    }

    fun addLike(postId: String/*, numberOfLikes: Int*/) {
        DatabaseReferenceRetriever.userLikeReference(userId, postId).setValue(true)
//        DatabaseReferenceRetriever.postNumberOfLikesReference(postId).setValue(numberOfLikes)
    }

    fun removeLike(postId: String, numberOfLikes: Int) {
        DatabaseReferenceRetriever.userLikeReference(userId, postId).removeValue()
        DatabaseReferenceRetriever.postNumberOfLikesReference(postId).setValue(numberOfLikes)
    }

    fun addDislike(postId: String, numberOfDislikes: Int) {
        DatabaseReferenceRetriever.userDislikeReference(userId, postId).setValue(true)
        DatabaseReferenceRetriever.postNumberOfDislikesReference(postId).setValue(numberOfDislikes)
    }

    fun removeDislike(postId: String, numberOfDislikes: Int) {
        DatabaseReferenceRetriever.userDislikeReference(userId, postId).removeValue()
        DatabaseReferenceRetriever.postNumberOfDislikesReference(postId).setValue(numberOfDislikes)
    }

    fun deletePost(postId: String) {
        DatabaseReferenceRetriever.userPostReference(userId, postId).removeValue()
        DatabaseReferenceRetriever.postReference(postId).removeValue()

        StorageReferenceRetriever.songReference(userId, postId).delete()
        StorageReferenceRetriever.coverReference(userId, postId).delete()
    }


}

