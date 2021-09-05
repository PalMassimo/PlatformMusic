package it.units.musicplatform.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import it.units.musicplatform.entities.Post
import it.units.musicplatform.entities.User
import it.units.musicplatform.repositories.UserRepository
import kotlinx.coroutines.launch
import kotlin.collections.set

class UserViewModel : ViewModel() {
    val userId = FirebaseAuth.getInstance().currentUser!!.uid
    private val userRepository = UserRepository(userId)
    private val _user = MutableLiveData<User>()
    private val _posts = MutableLiveData<ArrayList<Post>>()
//    private val _following = MutableLiveData<Set<String>>()


    val user: LiveData<User> = _user
    val posts: LiveData<ArrayList<Post>> = _posts
//    val following:LiveData<Set<String>> = _following


    init {
        _user.value = User()
        _posts.value = ArrayList()
        viewModelScope.launch {
            _user.postValue(userRepository.getUser())
            _posts.postValue(userRepository.getPosts())
//            _following.postValue(userRepository.getFollowing())
        }
    }

    fun addFollowing(followingId: String) {
        userRepository.addFollowing(followingId)
        user.value!!.following.put(followingId, true)
        _user.value = _user.value
    }

    fun removeFollowing(followingId: String) {
        userRepository.removeFollowing(followingId)
        user.value!!.following.remove(followingId)
        _user.value = _user.value
    }

    fun addLike(postId: String) {
        _user.value!!.likes[postId] = true
        userRepository.addLike(postId)
    }

    fun addDislike(postId: String) {
        _user.value!!.dislikes[postId] = true
        userRepository.addDislike(postId)
    }

    fun removeLike(postId: String) {
        _user.value!!.likes.remove(postId)
        userRepository.removeLike(postId)
    }

    fun removeDislike(postId: String) {
        _user.value!!.dislikes.remove(postId)
        userRepository.removeDislike(postId)
    }

    fun addPost(post: Post, localUriSong: Uri, localUriCover: Uri?) {
        viewModelScope.launch {
            userRepository.addPost(post, localUriSong, localUriCover)
            posts.value!!.add(post)
            _posts.value = _posts.value
        }
    }

    fun updatePost(position: Int, songName: String?, artistName: String?, localUriCover: String?) {

        var post = posts.value!!.get(position)
        viewModelScope.launch {
            post = userRepository.updatePost(post, songName, artistName, localUriCover)
            _posts.value = _posts.value
        }
    }

    fun deletePost(id: String) {
        userRepository.deletePost(id)
        posts.value!!.removeIf { post -> post.id == id }
    }

    fun updateProfilePicture(uri: Uri) = userRepository.updateProfilePicture(uri)

}


