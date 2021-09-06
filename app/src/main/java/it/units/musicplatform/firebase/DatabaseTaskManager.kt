package it.units.musicplatform.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import it.units.musicplatform.entities.Post
import it.units.musicplatform.retrievers.DatabaseReferenceRetriever
import java.util.stream.StreamSupport

class DatabaseTaskManager {

    companion object {

        @JvmStatic
        fun getUserTask(userId: String) = DatabaseReferenceRetriever.user(userId).get()

        @JvmStatic
        fun getUserPostsTask(userId: String, posts: ArrayList<Post>): Task<Unit> {
            return DatabaseReferenceRetriever.posts().get().continueWith { postsSnapshotTask ->
                StreamSupport.stream(postsSnapshotTask.result!!.children.spliterator(), true)
                    .map { it.getValue(Post::class.java) }
                    .filter { it!!.uploaderId == userId }
                    .forEach { posts.add(it!!) }
            }
        }

        @JvmStatic
        fun addPostTask(post: Post): Task<Void> {
            return DatabaseReferenceRetriever.post(post.id).setValue(post).addOnSuccessListener {
                DatabaseReferenceRetriever.userPost(post.uploaderId, post.id).setValue(true)
            }
        }

        @JvmStatic
        fun updateSongNameTask(postId: String, songName: String) = DatabaseReferenceRetriever.postSongName(postId).setValue(songName)

        @JvmStatic
        fun updateArtistNameTask(postId: String, artistName: String) = DatabaseReferenceRetriever.postArtistName(postId).setValue(artistName)

        @JvmStatic
        fun addFollowing(userId: String, followingId: String) {
            DatabaseReferenceRetriever.userFollowing(userId, followingId).setValue(true)
            DatabaseReferenceRetriever.userFollower(followingId, userId).setValue(true)
            changeNumberOfFollowers(followingId, true)
        }

        @JvmStatic
        fun removeFollowing(userId: String, followingId: String){
            DatabaseReferenceRetriever.userFollowing(userId, followingId).removeValue()
            DatabaseReferenceRetriever.userFollower(followingId, userId).removeValue()
            changeNumberOfFollowers(followingId, false)
        }


        private fun changeNumberOfFollowers(followingId: String, increase: Boolean) {

            DatabaseReferenceRetriever.userNumberOfFollowers(followingId).get()
                .continueWith { it.result!!.getValue(Int::class.java) }
                .addOnSuccessListener { numberOfFollowers ->
                    val updatedNumberOfFollowers = if (increase) numberOfFollowers!! + 1 else numberOfFollowers!! - 1
                    DatabaseReferenceRetriever.userNumberOfFollowers(followingId).setValue(updatedNumberOfFollowers)
                }
        }


    }


}