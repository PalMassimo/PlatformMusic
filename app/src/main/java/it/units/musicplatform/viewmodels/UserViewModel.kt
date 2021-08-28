package it.units.musicplatform.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import it.units.musicplatform.entities.User
import it.units.musicplatform.retrievers.DatabaseReferenceRetriever

private val userId = FirebaseAuth.getInstance().currentUser!!.uid

//class UserViewModel(val userId : String) : ViewModel() {
class UserViewModel() : ViewModel() {


    fun getUser(): LiveData<User> {
        return user
    }

    private val user: MutableLiveData<User> by lazy {
        MutableLiveData<User>().also {
            loadUser()
        }
    }


    private fun loadUser() {
//        Firebase.database("https://sharemusic-99f8a-default-rtdb.europe-west1.firebasedatabase.app/").reference
//            .child("Users").child(userId).get().addOnSuccessListener {
//                user.value = it.getValue(User::class.java)!!
//            }
        DatabaseReferenceRetriever.userReference(userId).get().addOnSuccessListener {
            user.value = it.getValue(User::class.java)!!
        }
    }


}