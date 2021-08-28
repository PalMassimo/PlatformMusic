package it.units.musicplatform.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import it.units.musicplatform.entities.User
import it.units.musicplatform.retrievers.DatabaseReferenceRetriever

private val userId = FirebaseAuth.getInstance().currentUser!!.uid

//class UserViewModel(val userId : String) : ViewModel() {
class UserViewModel() : ViewModel() {

    private val _user = MutableLiveData<User>()
//    private var _followers = MutableLiveData<Set<String>>()

    val user: LiveData<User> = _user
//    val followers: LiveData<Set<String>> = _followers


    init {
        loadUser()
    }

    private fun loadUser() {
        DatabaseReferenceRetriever.userReference(userId).get().addOnSuccessListener { _user.value = it.getValue(User::class.java) }
    }

//    private fun getFollowers(){
//        val followers : Set<String> = HashSet()
//        DatabaseReferenceRetriever.followersReference(userId).get().addOnSuccessListener {  }
//    }


//    private val user: MutableLiveData<User> by lazy {
//        MutableLiveData<User>().also {
//            loadUser()
//        }
//    }

//    private fun loadUser() {
//        DatabaseReferenceRetriever.userReference(userId).get().addOnSuccessListener {
//            user.value = it.getValue(User::class.java)!!
//        }
//    }


}