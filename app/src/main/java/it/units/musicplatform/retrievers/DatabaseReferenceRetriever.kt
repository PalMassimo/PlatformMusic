package it.units.musicplatform.retrievers

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val DATABASE_URL = "https://sharemusic-99f8a-default-rtdb.europe-west1.firebasedatabase.app/"
private const val SONG_NAME_CHILD = "songName"
private const val ARTIST_NAME_CHILD = "artistName"
private const val FOLLOWERS_CHILD = "followers"
private const val FOLLOWING_CHILD = "following"
private const val LIKES_CHILD = "likes"
private const val DISLIKES_CHILD = "dislikes"


private const val POSTS_CHILD = "posts"
private const val NUMBER_OF_LIKES = "numberOfLikes"
private const val NUMBER_OF_DISLIKES = "numberOfDislikes"
private const val NUMBER_OF_DOWNLOADS = "numberOfDownloads"

private val DATABASE_REFERENCE = Firebase.database(DATABASE_URL).reference
private val USERS_REFERENCE = DATABASE_REFERENCE.child("Users")
private val POSTS_REFERENCE = DATABASE_REFERENCE.child("posts")


class DatabaseReferenceRetriever {

    companion object {

        fun users() = USERS_REFERENCE
        fun user(userId: String) = users().child(userId)
        fun userFollowers(userId: String) = user(userId).child(FOLLOWERS_CHILD)
        fun userFollowing(userId: String) = user(userId).child(FOLLOWING_CHILD)
        fun userLikes(userId: String) = user(userId).child(LIKES_CHILD)
        fun userLike(userId: String, postId: String) = userLikes(userId).child(postId)
        fun userDislikes(userId: String) = user(userId).child(DISLIKES_CHILD)
        fun userDislike(userId: String, postId: String) = userDislikes(userId).child(postId)
        fun userPosts(userId: String) = user(userId).child(POSTS_CHILD)
        fun userPost(userId: String, postId: String) = userPosts(userId).child(postId)

        fun posts() = POSTS_REFERENCE
        fun post(postId: String) = posts().child(postId)
        fun postSongName(postId: String) = post(postId).child(SONG_NAME_CHILD)
        fun postArtistName(postId: String) = post(postId).child(ARTIST_NAME_CHILD)
        fun postNumberOfLikes(postId: String) = post(postId).child(NUMBER_OF_LIKES)
        fun postNumberOfDislikes(postId: String) = post(postId).child(NUMBER_OF_DISLIKES)
        fun postNumberOfDownloads(postId: String) = post(postId).child(NUMBER_OF_DOWNLOADS)


    }

}