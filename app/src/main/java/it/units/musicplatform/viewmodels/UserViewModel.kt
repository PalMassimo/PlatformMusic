package it.units.musicplatform.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.auth.FirebaseAuth
import it.units.musicplatform.entities.Post
import it.units.musicplatform.entities.User
import it.units.musicplatform.repositories.UserRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private val USER_ID = FirebaseAuth.getInstance().currentUser!!.uid

//class UserViewModel(val userId : String) : ViewModel() {
class UserViewModel : ViewModel() {

    private val userRepository = UserRepository(USER_ID)
    private val _user = MutableLiveData<User>()
    private val _posts = MutableLiveData<ArrayList<Post>>()
    private val _following = MutableLiveData<Set<String>>()

    val user: LiveData<User> = _user
    val posts: LiveData<ArrayList<Post>> = _posts
//    val following:LiveData<Set<String>> = _following


    init {
        GlobalScope.launch {
            _user.postValue(userRepository.getUser())
            _posts.postValue(userRepository.getPosts())
//            _following.postValue(userRepository.getFollowing())
        }
    }

    private fun refreshPosts() = GlobalScope.launch { _posts.postValue(userRepository.getPosts()) }
    private fun refreshUser() = GlobalScope.launch { _user.postValue(userRepository.getUser()) }

    fun addPost(post: Post) {
        GlobalScope.launch {
            userRepository.addPost(post)
            //improve: I should add the post to the LiveData and notify it
            refreshPosts()
        }
    }

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

}


