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

private val USER_ID = FirebaseAuth.getInstance().currentUser!!.uid

//class UserViewModel(val userId : String) : ViewModel() {
class UserViewModel() : ViewModel() {

    private val _user = MutableLiveData<User>()
    private val _posts = MutableLiveData<ArrayList<Post>>()

    val user: LiveData<User> = _user
    val posts: LiveData<ArrayList<Post>> = _posts



    init {
        loadUser()
        loadPosts()
    }

    private fun loadUser() {
        DatabaseReferenceRetriever.userReference(USER_ID).get().addOnSuccessListener { _user.value = it.getValue(User::class.java) }
    }

    private fun loadPosts(){
        val posts = ArrayList<Post>()
        DatabaseReferenceRetriever.postsReference().get().addOnSuccessListener {
            filterUserPosts(it, posts)
        }.addOnSuccessListener { _posts.value = posts }
    }

    private fun filterUserPosts(postsSnapshot: DataSnapshot, postsList: ArrayList<Post>){
        StreamSupport.stream(postsSnapshot.children.spliterator(), false)
            .map{it.getValue(Post::class.java)}
            .filter{it!!.uploaderId.equals(USER_ID)}
            .forEach{ postsList.add(it!!) }
    }


}