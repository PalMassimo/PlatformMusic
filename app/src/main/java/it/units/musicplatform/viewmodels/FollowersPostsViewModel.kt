package it.units.musicplatform.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import it.units.musicplatform.entities.Post
import it.units.musicplatform.entities.User
import it.units.musicplatform.retrievers.DatabaseReferenceRetriever
import java.util.stream.StreamSupport

private val userId = FirebaseAuth.getInstance().currentUser!!.uid

class FollowersPostsViewModel : ViewModel() {

    private val postsList = ArrayList<Post>()
    private val _followersPosts = MutableLiveData<List<Post>>()
    val followersPosts: LiveData<List<Post>> = _followersPosts

    init {
        _followersPosts.value = ArrayList()
        loadPosts()
    }

    private fun loadPosts() {

        //TODO: replace addOnSuccessListener with continueWith
        DatabaseReferenceRetriever.userReference(userId).get()
            .addOnSuccessListener {
                it.getValue(User::class.java)?.following?.keys?.stream()
                    ?.map { followingId -> DatabaseReferenceRetriever.userPostsReference(followingId).get() }
                    ?.forEach { followingUserTask -> followingUserTask.addOnSuccessListener { followingUser -> fromFollowersPostsToPost(followingUser) } }
            }
    }

    private fun fromFollowersPostsToPost(postsSnapshot: DataSnapshot) {
        StreamSupport.stream(postsSnapshot.children.spliterator(), false)
            .map { it.key }
            .map { DatabaseReferenceRetriever.postReference(it!!).get() }
            .forEach { it.addOnSuccessListener { fromPostSnapshotToPost(it) } }
    }

    private fun fromPostSnapshotToPost(postsSnapshot: DataSnapshot) {
        postsSnapshot.getValue(Post::class.java)?.let { post ->
            postsList.add(post)
            _followersPosts.value = postsList
        }
    }

    fun incrementNumberOfDownloads(position: Int) {
        val post = followersPosts.value!![position]
        DatabaseReferenceRetriever.postNumberOfDownloads(post.id).setValue(++post.numberOfDownloads)
    }


}