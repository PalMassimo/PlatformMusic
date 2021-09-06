package it.units.musicplatform.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import it.units.musicplatform.entities.Post
import it.units.musicplatform.entities.User
import it.units.musicplatform.repositories.PostsRepository
import it.units.musicplatform.retrievers.DatabaseReferenceRetriever
import kotlinx.coroutines.launch
import java.util.stream.StreamSupport

class FollowersPostsViewModel(private val userId: String) : ViewModel() {

    private var postsList = ArrayList<Post>()
    private val _followersPosts = MutableLiveData<List<Post>>()
    val followersPosts: LiveData<List<Post>> = _followersPosts
    private val postsRepository = PostsRepository()

    init {
        _followersPosts.value = ArrayList()
        loadPosts()
    }

    private fun loadPosts() {

        //TODO: replace addOnSuccessListener with continueWith
        DatabaseReferenceRetriever.user(userId).get().addOnSuccessListener {
            it.getValue(User::class.java)?.following?.keys?.stream()
                ?.map { followingId -> DatabaseReferenceRetriever.userPosts(followingId).get() }
                ?.forEach { followingUserTask -> followingUserTask.addOnSuccessListener { followingUser -> fromFollowersPostsToPost(followingUser) } }
        }
    }

    private fun fromFollowersPostsToPost(postsSnapshot: DataSnapshot) {
        StreamSupport.stream(postsSnapshot.children.spliterator(), false)
            .map { it.key }
            .map { DatabaseReferenceRetriever.post(it!!).get() }
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
        post.numberOfDownloads++
        postsRepository.setNumberOfDownloads(post.id, post.numberOfDownloads)
    }

    fun addLike(position: Int) {
        val post = followersPosts.value!![position]
        post.numberOfLikes++
        postsRepository.setNumberOfLikes(post.id, post.numberOfLikes)
    }

    fun addDislike(position: Int) {
        val post = followersPosts.value!![position]
        post.numberOfDislikes++
        postsRepository.setNumberOfDislikes(post.id, post.numberOfDislikes)
    }

    fun removeLike(position: Int) {
        val post = followersPosts.value!![position]
        post.numberOfLikes--
        postsRepository.setNumberOfLikes(post.id, post.numberOfLikes)
    }

    fun removeDislike(position: Int) {
        val post = followersPosts.value!![position]
        post.numberOfDislikes--
        postsRepository.setNumberOfDislikes(post.id, post.numberOfDislikes)
    }

    fun removeFollowing(followingId: String) {
        viewModelScope.launch {
            postsList.removeIf { post -> post.uploaderId == followingId }
            _followersPosts.postValue(postsList)
        }
    }

    fun addFollowing(followingId: String) {
        viewModelScope.launch {
            _followersPosts.postValue(postsRepository.getUserPosts(followingId))
        }
    }

}