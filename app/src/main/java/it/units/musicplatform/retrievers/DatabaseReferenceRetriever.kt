package it.units.musicplatform.retrievers

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import it.units.musicplatform.entities.User
import java.lang.ref.Reference

private const val DATABASE_URL = "https://sharemusic-99f8a-default-rtdb.europe-west1.firebasedatabase.app/"
private val DATABASE_REFERENCE = Firebase.database(DATABASE_URL).reference
//private val USERS_REFERENCE = DATABASE_REFERENCE.child("Users")
private val POSTS_REFERENCE = DATABASE_REFERENCE.child("posts")


class DatabaseReferenceRetriever {

    companion object{

        fun usersReference() = DATABASE_REFERENCE.child("Users")
        fun userReference(userId: String) = usersReference().child(userId)
        fun userFollowersReference(userId: String) = userReference(userId).child("followers")
        fun userFollowingReference(userId: String) = userReference(userId).child("following")
        fun userLikesReference(userId: String) = userReference(userId).child("likes")
        fun userLikeReference(userId: String, postId: String) = userLikesReference(userId).child(postId)
        fun userDislikesReference(userId: String) = userReference(userId).child("dislikes")
        fun userDislikeReference(userId: String, postId:String) = userDislikesReference(userId).child(postId)
        fun userPostsReference(userId:String) = userReference(userId).child("posts")
        fun userPostReference(userId: String, postId: String) = userPostsReference(userId).child(postId)

        fun postsReference() = POSTS_REFERENCE
        fun postReference(postId: String) = postsReference().child(postId)
        fun postSongNameReference(postId: String) = postReference(postId).child("songName")
        fun postArtistNameReference(postId: String) = postReference(postId).child("artistName")
        fun postCoverDownloadString(postId: String) = postReference(postId).child("songPictureDownloadString")
        fun postNumberOfLikesReference(postId: String) = postReference(postId).child("numberOfLikes")
        fun postNumberOfDislikesReference(postId: String) = postReference(postId).child("numberOfDislikes")
        fun postNumberOfDownloads(postId: String) = postReference(postId).child("numberOfDownloads")


    }

}