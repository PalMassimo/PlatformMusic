package it.units.musicplatform.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import it.units.musicplatform.entities.Post
import it.units.musicplatform.entities.User
import it.units.musicplatform.repositories.UserRepository
import it.units.musicplatform.retrievers.DatabaseReferenceRetriever
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.stream.StreamSupport

private val USER_ID = FirebaseAuth.getInstance().currentUser!!.uid

//class UserViewModel(val userId : String) : ViewModel() {
class UserViewModel : ViewModel() {

    private val userRepository = UserRepository(USER_ID)
    private val _user = MutableLiveData<User>()
    private val _posts = MutableLiveData<ArrayList<Post>>()

    val user: LiveData<User> = _user
    val posts: LiveData<ArrayList<Post>> = _posts


    init {
        GlobalScope.launch {
            _user.postValue(userRepository.getUser())
            _posts.postValue(userRepository.getPosts())
        }
    }

    private fun refreshPosts() {
        GlobalScope.launch { _posts.postValue(userRepository.getPosts()) }
    }

    fun addPost(post: Post) {
        GlobalScope.launch {
            userRepository.addPost(post)
            //improve: I should add the post to the LiveData and notify it
            refreshPosts()
        }
    }


}