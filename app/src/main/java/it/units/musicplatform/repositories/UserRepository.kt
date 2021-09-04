package it.units.musicplatform.repositories

import android.net.Uri
import com.google.android.gms.tasks.Task
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
        DatabaseReferenceRetriever.postsReference().get().continueWith { postsSnapshotTask ->
            StreamSupport.stream(postsSnapshotTask.result!!.children.spliterator(), false)
                .map { it.getValue(Post::class.java) }
                .filter { it!!.uploaderId == userId }
                .forEach { posts.add(it!!) }
        }.await()
        return posts
    }

    suspend fun addPost(post: Post, localUriSong: Uri, localUriCover: Uri?): Post {

        val tasks = HashSet<Task<Unit>>()

        localUriCover?.let { uri ->
            val addCoverTask = StorageReferenceRetriever.coverReference(userId, post.id).putFile(uri).continueWithTask {
                it.result!!.storage.downloadUrl
            }.continueWith { uriTask -> post.songFileDownloadString = uriTask.result.toString() }
            tasks.add(addCoverTask)
        }

        val addSongTask = StorageReferenceRetriever.songReference(userId, post.id).putFile(localUriSong).continueWithTask {
            it.result!!.storage.downloadUrl
        }.continueWith { uriTask -> post.songPictureDownloadString = uriTask.result.toString() }

        tasks.add(addSongTask)

        Tasks.whenAllComplete(tasks).continueWith {
            DatabaseReferenceRetriever.postReference(post.id).setValue(post)
            DatabaseReferenceRetriever.userPostReference(post.uploaderId, post.id).setValue(true)
        }.await()

        return post
    }

    suspend fun updatePost(post: Post, songName: String?, artistName: String?, localUriCover: String?): Post {

        localUriCover?.let { localUri ->
            StorageReferenceRetriever.coverReference(post.uploaderId, post.id).putFile(Uri.parse(localUri)).continueWithTask {
                it.result!!.storage.downloadUrl
            }.continueWith { uriTask -> post.songPictureDownloadString = uriTask.toString() }.await()
        }

        songName?.let { DatabaseReferenceRetriever.postSongNameReference(post.id).setValue(it) }
        artistName?.let { DatabaseReferenceRetriever.postArtistNameReference(post.id).setValue(it) }

        return post

    }

    fun addFollowing(followingId: String) {
        DatabaseReferenceRetriever.userFollowingReference(userId).child(followingId).setValue(true)
        DatabaseReferenceRetriever.userFollowersReference(followingId).child(userId).setValue(true)
    }

    fun removeFollowing(followingId: String) {
        DatabaseReferenceRetriever.userFollowingReference(userId).child(followingId).removeValue()
        DatabaseReferenceRetriever.userFollowersReference(followingId).child(userId).removeValue()
    }

    fun addLike(postId: String) = DatabaseReferenceRetriever.userLikeReference(userId, postId).setValue(true)
    fun removeLike(postId: String) = DatabaseReferenceRetriever.userLikeReference(userId, postId).removeValue()
    fun addDislike(postId: String) = DatabaseReferenceRetriever.userDislikeReference(userId, postId).setValue(true)
    fun removeDislike(postId: String) = DatabaseReferenceRetriever.userDislikeReference(userId, postId).removeValue()

    fun deletePost(postId: String) {
        DatabaseReferenceRetriever.userPostReference(userId, postId).removeValue()
        DatabaseReferenceRetriever.postReference(postId).removeValue()

        StorageReferenceRetriever.songReference(userId, postId).delete()
        StorageReferenceRetriever.coverReference(userId, postId).delete()
    }


}

