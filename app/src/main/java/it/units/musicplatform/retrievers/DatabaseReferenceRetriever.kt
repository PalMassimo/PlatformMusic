package it.units.musicplatform.retrievers

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import it.units.musicplatform.entities.User

private const val DATABASE_URL = "https://sharemusic-99f8a-default-rtdb.europe-west1.firebasedatabase.app/"
private val DATABASE_REFERENCE = Firebase.database(DATABASE_URL).reference
//private val USERS_REFERENCE = DATABASE_REFERENCE.child("Users")
private val POSTS_REFERENCE = DATABASE_REFERENCE.child("posts")


class DatabaseReferenceRetriever {

    companion object{

        fun usersReference() = DATABASE_REFERENCE.child("Users")
        fun userReference(userId: String) = usersReference().child(userId)
        fun followersReference(userId: String) = userReference(userId).child("followers")
        fun followingReference(userId: String) = userReference(userId).child("following")
        fun userPostsReference(userId:String) = userReference(userId).child("posts")
        fun userPostReference(userId: String, postId: String) = userPostsReference(userId).child(postId)

        fun postsReference() = POSTS_REFERENCE
        fun postReference(postId: String) = postsReference().child(postId)

    }

}