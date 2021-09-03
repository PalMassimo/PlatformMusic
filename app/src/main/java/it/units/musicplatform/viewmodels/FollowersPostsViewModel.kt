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
        loadPosts()
    }

    private fun loadPosts() {
        DatabaseReferenceRetriever.userReference(userId).get()
            .addOnSuccessListener {
                it.getValue(User::class.java)?.following?.keys?.stream()
                    ?.map {
                        DatabaseReferenceRetriever.userPostsReference(it).get()
                    }?.forEach {
                        it.addOnSuccessListener { fromFollowersPostsToPost(it) }
                    }
            }
    }

    private fun fromFollowersPostsToPost(postsSnapshot: DataSnapshot) {
        StreamSupport.stream(postsSnapshot.children.spliterator(), false)
            .map { it.key }
            .map { DatabaseReferenceRetriever.postReference(it!!).get() }
            .forEach { it.addOnSuccessListener { fromPostSnapshotToPost(it) } }
    }

    private fun fromPostSnapshotToPost(postsSnapshot: DataSnapshot) {
        postsSnapshot.getValue(Post::class.java)?.let {
            postsList.add(it)
            _followersPosts.value = postsList
        }
    }


}