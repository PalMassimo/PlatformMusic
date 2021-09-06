package it.units.musicplatform.firebase.retrievers

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val DATABASE_URL = "https://sharemusic-99f8a-default-rtdb.europe-west1.firebasedatabase.app/"
private const val USERS_CHILD = "users"
private const val SONG_NAME_CHILD = "songName"
private const val ARTIST_NAME_CHILD = "artistName"
private const val FOLLOWERS_CHILD = "followers"
private const val FOLLOWING_CHILD = "following"
private const val LIKES_CHILD = "likes"
private const val DISLIKES_CHILD = "dislikes"
private const val NUMBER_OF_FOLLOWERS = "numberOfFollowers"

private const val POSTS_CHILD = "posts"
private const val NUMBER_OF_LIKES = "numberOfLikes"
private const val NUMBER_OF_DISLIKES = "numberOfDislikes"
private const val NUMBER_OF_DOWNLOADS = "numberOfDownloads"

private val DATABASE_REFERENCE = Firebase.database(DATABASE_URL).reference
private val USERS_REFERENCE = DATABASE_REFERENCE.child(USERS_CHILD)
private val POSTS_REFERENCE = DATABASE_REFERENCE.child(POSTS_CHILD)


class DatabaseReferenceRetriever {

    companion object {

        @JvmStatic fun users() = USERS_REFERENCE
        @JvmStatic fun user(userId: String) = users().child(userId)
        @JvmStatic fun userNumberOfFollowers(userId: String) = user(userId).child(NUMBER_OF_FOLLOWERS)
        @JvmStatic fun userFollowers(userId: String) = user(userId).child(FOLLOWERS_CHILD)
        @JvmStatic fun userFollower(userId: String, followerId: String) = userFollowers(userId).child(followerId)
        @JvmStatic fun userFollowings(userId: String) = user(userId).child(FOLLOWING_CHILD)
        @JvmStatic fun userFollowing(userId: String, followingId: String) = userFollowings(userId).child(followingId)
        @JvmStatic fun userLikes(userId: String) = user(userId).child(LIKES_CHILD)
        @JvmStatic fun userLike(userId: String, postId: String) = userLikes(userId).child(postId)
        @JvmStatic fun userDislikes(userId: String) = user(userId).child(DISLIKES_CHILD)
        @JvmStatic fun userDislike(userId: String, postId: String) = userDislikes(userId).child(postId)
        @JvmStatic fun userPosts(userId: String) = user(userId).child(POSTS_CHILD)
        @JvmStatic fun userPost(userId: String, postId: String) = userPosts(userId).child(postId)

        @JvmStatic fun posts() = POSTS_REFERENCE
        @JvmStatic fun post(postId: String) = posts().child(postId)
        @JvmStatic fun postSongName(postId: String) = post(postId).child(SONG_NAME_CHILD)
        @JvmStatic fun postArtistName(postId: String) = post(postId).child(ARTIST_NAME_CHILD)
        @JvmStatic fun postNumberOfLikes(postId: String) = post(postId).child(NUMBER_OF_LIKES)
        @JvmStatic fun postNumberOfDislikes(postId: String) = post(postId).child(NUMBER_OF_DISLIKES)
        @JvmStatic fun postNumberOfDownloads(postId: String) = post(postId).child(NUMBER_OF_DOWNLOADS)


    }

}