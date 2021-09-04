package it.units.musicplatform.repositories

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import it.units.musicplatform.entities.Post
import it.units.musicplatform.entities.User
import it.units.musicplatform.retrievers.DatabaseReferenceRetriever
import it.units.musicplatform.retrievers.StorageReferenceRetriever
import kotlinx.coroutines.tasks.await
import java.util.stream.StreamSupport

class UserRepository(private val userId: String) {

    suspend fun getUser() = DatabaseReferenceRetriever.user(userId).get().await().getValue(User::class.java)

    suspend fun getPosts(): ArrayList<Post> {
        val posts = ArrayList<Post>()
        DatabaseReferenceRetriever.posts().get().continueWith { postsSnapshotTask ->
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
            }.continueWith { uriTask -> post.songDownloadString = uriTask.result.toString() }
            tasks.add(addCoverTask)
        }

        val addSongTask = StorageReferenceRetriever.songReference(userId, post.id).putFile(localUriSong).continueWithTask {
            it.result!!.storage.downloadUrl
        }.continueWith { uriTask -> post.coverDownloadString = uriTask.result.toString() }

        tasks.add(addSongTask)

        Tasks.whenAllComplete(tasks).continueWith {
            DatabaseReferenceRetriever.post(post.id).setValue(post)
            DatabaseReferenceRetriever.userPost(post.uploaderId, post.id).setValue(true)
        }.await()

        return post
    }

    suspend fun updatePost(post: Post, songName: String?, artistName: String?, localUriCover: String?): Post {

        localUriCover?.let { localUri ->
            StorageReferenceRetriever.coverReference(post.uploaderId, post.id).putFile(Uri.parse(localUri)).continueWithTask {
                it.result!!.storage.downloadUrl
            }.continueWith { uriTask -> post.coverDownloadString = uriTask.toString() }.await()
        }

        songName?.let { DatabaseReferenceRetriever.postSongName(post.id).setValue(it) }
        artistName?.let { DatabaseReferenceRetriever.postArtistName(post.id).setValue(it) }

        return post

    }

    fun addFollowing(followingId: String) {
        DatabaseReferenceRetriever.userFollowing(userId).child(followingId).setValue(true)
        DatabaseReferenceRetriever.userFollowers(followingId).child(userId).setValue(true)
    }

    fun removeFollowing(followingId: String) {
        DatabaseReferenceRetriever.userFollowing(userId).child(followingId).removeValue()
        DatabaseReferenceRetriever.userFollowers(followingId).child(userId).removeValue()
    }

    fun addLike(postId: String) = DatabaseReferenceRetriever.userLike(userId, postId).setValue(true)
    fun removeLike(postId: String) = DatabaseReferenceRetriever.userLike(userId, postId).removeValue()
    fun addDislike(postId: String) = DatabaseReferenceRetriever.userDislike(userId, postId).setValue(true)
    fun removeDislike(postId: String) = DatabaseReferenceRetriever.userDislike(userId, postId).removeValue()

    fun deletePost(postId: String) {
        DatabaseReferenceRetriever.userPost(userId, postId).removeValue()
        DatabaseReferenceRetriever.post(postId).removeValue()

        StorageReferenceRetriever.songReference(userId, postId).delete()
        StorageReferenceRetriever.coverReference(userId, postId).delete()
    }

    fun updateProfilePicture(uri: Uri) = StorageReferenceRetriever.userImageReference(userId).putFile(uri)


}

