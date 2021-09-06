package it.units.musicplatform.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import it.units.musicplatform.entities.Post
import it.units.musicplatform.entities.User
import it.units.musicplatform.retrievers.DatabaseReferenceRetriever
import java.util.*
import java.util.stream.StreamSupport
import kotlin.collections.ArrayList

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
        fun getUserPostTask(userId: String, posts: ArrayList<Post>): Task<DataSnapshot> {
            posts.clear()
            return DatabaseReferenceRetriever.posts().orderByChild("uploaderId").equalTo(userId).get().addOnSuccessListener {
                StreamSupport.stream(it.children.spliterator(), true)
                    .map { postSnapshot -> postSnapshot.getValue(Post::class.java)!! }
                    .forEach { post -> posts.add(post) }
            }
        }

        @JvmStatic
        fun getPopularUsers(userId: String, popularUsers: ArrayList<User>): Task<Unit> {
            popularUsers.clear()
            return DatabaseReferenceRetriever.users().orderByChild("numberOfFollowers").limitToLast(5).get()
                .continueWith { usersDataSnapshotTask ->
                    val usersDataSnapshot = usersDataSnapshotTask.result
                    StreamSupport.stream(usersDataSnapshot?.children!!.spliterator(), true)
                        .map { userSnapshot: DataSnapshot -> userSnapshot.getValue(User::class.java) }
                        .filter { user: User? -> !user?.id.equals(userId) }
                        .forEach { user: User? -> popularUsers.add(user!!) }

                }
        }

        @JvmStatic
        fun getMatchingUsersTask(userId: String, pattern: String, resultUsers: ArrayList<User>): Task<Unit> {

            resultUsers.clear()

            return DatabaseReferenceRetriever.users().get().continueWith { usersDataSnapshotTask: Task<DataSnapshot> ->
                StreamSupport.stream(usersDataSnapshotTask.result?.children?.spliterator(), false)
                    .map { userSnapshot: DataSnapshot -> userSnapshot.getValue(User::class.java) }
                    .filter { user: User? -> user?.username!!.toLowerCase(Locale.ROOT).contains(pattern.toLowerCase(Locale.ROOT)) && user.id != userId }
                    .forEach { resultUsers.add(it!!) }
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
        fun removeFollowing(userId: String, followingId: String) {
            DatabaseReferenceRetriever.userFollowing(userId, followingId).removeValue()
            DatabaseReferenceRetriever.userFollower(followingId, userId).removeValue()
            changeNumberOfFollowers(followingId, false)
        }

        @JvmStatic
        fun deletePost(userId: String, postId: String) {
            DatabaseReferenceRetriever.userPost(userId, postId).removeValue()
            DatabaseReferenceRetriever.post(postId).removeValue()
        }


        @JvmStatic
        fun addLikeTask(userId: String, postId: String) = DatabaseReferenceRetriever.userLike(userId, postId).setValue(true)

        @JvmStatic
        fun removeLikeTask(userId: String, postId: String) = DatabaseReferenceRetriever.userLike(userId, postId).removeValue()

        @JvmStatic
        fun addDislikeTask(userId: String, postId: String) = DatabaseReferenceRetriever.userDislike(userId, postId).setValue(true)

        @JvmStatic
        fun removeDislikeTask(userId: String, postId: String) = DatabaseReferenceRetriever.userDislike(userId, postId).removeValue()

        @JvmStatic
        fun setNumberOfLikesTask(postId: String, numberOfLikes: Int) = DatabaseReferenceRetriever.postNumberOfLikes(postId).setValue(numberOfLikes)

        @JvmStatic
        fun setNumberOfDislikesTask(postId: String, numberOfDislikes: Int) = DatabaseReferenceRetriever.postNumberOfDislikes(postId).setValue(numberOfDislikes)

        @JvmStatic
        fun setNumberOfDownloadsTask(postId: String, numberOfDownloads: Int) = DatabaseReferenceRetriever.postNumberOfDownloads(postId).setValue(numberOfDownloads)

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