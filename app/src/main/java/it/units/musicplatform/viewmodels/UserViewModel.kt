package it.units.musicplatform.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.units.musicplatform.entities.Post
import it.units.musicplatform.entities.User
import it.units.musicplatform.repositories.UserRepository
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList
import kotlin.collections.set

class UserViewModel(val userId: String) : ViewModel() {
    private val userRepository = UserRepository(userId)
    private val _user = MutableLiveData<User>()
    private val _posts = MutableLiveData<ArrayList<Post>>()
//    private val _following = MutableLiveData<Set<String>>()


    val user: LiveData<User> = _user
    val posts: LiveData<ArrayList<Post>> = _posts
//    val following:LiveData<Set<String>> = _following


    init {
//        _user.value = User()
//        _posts.value = ArrayList()
        viewModelScope.launch {
            _user.postValue(userRepository.getUser())
            _posts.postValue(userRepository.getPosts())
//            _following.postValue(userRepository.getFollowing())
        }
    }


    private fun refreshUser() = viewModelScope.launch { _user.postValue(userRepository.getUser()) }



    fun addFollowing(followingId: String) {
        userRepository.addFollowing(followingId)
        refreshUser()
    }

    fun removeFollowing(followingId: String) {
        userRepository.removeFollowing(followingId)
        refreshUser()
    }

    fun addLike(postId: String, numberOfLikes: Int) {
        _user.value!!.likes[postId] = true
        userRepository.addLike(postId, numberOfLikes)
    }

    fun addDislike(postId: String, numberOfDislikes: Int) {
        _user.value!!.dislikes[postId] = true
        userRepository.addDislike(postId, numberOfDislikes)
    }

    fun removeLike(postId: String, numberOfLikes: Int) {
        _user.value!!.likes.remove(postId)
        userRepository.removeLike(postId, numberOfLikes)
    }

    fun removeDislike(postId: String, numberOfDislikes: Int) {
        _user.value!!.dislikes.remove(postId)
        userRepository.removeDislike(postId, numberOfDislikes)
    }

    fun fromLikeToDislike(postId: String, numberOfLikes: Int, numberOfDislikes: Int) {
        removeLike(postId, numberOfLikes)
        addDislike(postId, numberOfDislikes)
    }

    fun fromDislikeToLike(postId: String, numberOfLikes: Int, numberOfDislikes: Int) {
        addLike(postId, numberOfLikes)
        removeDislike(postId, numberOfDislikes)
    }

    fun addPost(post: Post) {
        viewModelScope.launch {
            userRepository.addPost(post)
            posts.value!!.add(post)
            //improve: I should add the post to the LiveData and notify it
            _posts.value = _posts.value
        }
    }

    fun updatePost(position: Int, songName: String?, artistName: String?, coverDownloadString: String?) {

        songName?.let { posts.value!!.get(position).songName = it }
        artistName?.let { posts.value!!.get(position).artistName = it }
        coverDownloadString?.let { posts.value!!.get(position).songPictureDownloadString = it }

        userRepository.updatePost(posts.value!!.get(position).id, songName, artistName, coverDownloadString)
    }

    fun deletePost(id: String) {
        userRepository.deletePost(id)
        posts.value!!.removeIf { post -> post.id == id }
    }



}


