package it.units.musicplatform.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.units.musicplatform.entities.Post
import it.units.musicplatform.repositories.FollowersPostsRepository
import kotlinx.coroutines.launch

class FollowersPostsViewModel(private val userId: String) : ViewModel() {

    private val _followersPosts = MutableLiveData(ArrayList<Post>())

    val followersPosts: LiveData<ArrayList<Post>> = _followersPosts
    private val followersPostsRepository = FollowersPostsRepository()

    private val _followingUsernames = MutableLiveData(HashMap<String, String>())
    val followingUsernames: LiveData<HashMap<String, String>> = _followingUsernames

    init {
        viewModelScope.launch {
            loadFollowingUsernames()
            loadPosts()
        }
    }

    private suspend fun loadFollowingUsernames() = _followingUsernames.postValue(followersPostsRepository.getFollowingUsernames(userId))

    suspend fun loadPosts() = _followersPosts.postValue(followersPostsRepository.loadPosts(userId))

    fun incrementNumberOfDownloads(position: Int) {
        val post = followersPosts.value!![position]
        post.numberOfDownloads++
        followersPostsRepository.setNumberOfDownloads(post.id, post.numberOfDownloads)
    }

    fun addLike(position: Int) {
        val post = followersPosts.value!![position]
        post.numberOfLikes++
        followersPostsRepository.setNumberOfLikes(post.id, post.numberOfLikes)
    }

    fun addDislike(position: Int) {
        val post = followersPosts.value!![position]
        post.numberOfDislikes++
        followersPostsRepository.setNumberOfDislikes(post.id, post.numberOfDislikes)
    }

    fun removeLike(position: Int) {
        val post = followersPosts.value!![position]
        post.numberOfLikes--
        followersPostsRepository.setNumberOfLikes(post.id, post.numberOfLikes)
    }

    fun removeDislike(position: Int) {
        val post = followersPosts.value!![position]
        post.numberOfDislikes--
        followersPostsRepository.setNumberOfDislikes(post.id, post.numberOfDislikes)
    }

    fun removeFollowing(followingId: String) {
        viewModelScope.launch {
            followersPosts.value!!.removeIf{post -> post.uploaderId == followingId}
            _followersPosts.value = followersPosts.value

            followingUsernames.value!!.remove(followingId)
            _followingUsernames.value = _followingUsernames.value
        }
    }

    fun addFollowing(followingId: String) {
        viewModelScope.launch {
            val followerPostsList = followersPostsRepository.getUserPosts(followingId)
            _followersPosts.postValue(ArrayList(followerPostsList + followersPosts.value!!))

            followingUsernames.value!![followingId] = followersPostsRepository.getFollowingUsername(followingId)
            _followingUsernames.value = _followingUsernames.value
        }
    }

}