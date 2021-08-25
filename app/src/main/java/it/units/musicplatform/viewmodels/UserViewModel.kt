package it.units.musicplatform.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import it.units.musicplatform.entities.User

class UserViewModel : ViewModel() {

    //    var userViewModel = MutableLiveData<User>()
    private val user: MutableLiveData<User> by lazy {
        MutableLiveData<User>().also {
            loadUser()
        }
    }

    private fun getUser() : LiveData<User>{
        return user
    }

    private fun loadUser(){
        Firebase.database.getReference("https://sharemusic-99f8a-default-rtdb.europe-west1.firebasedatabase.app/")
            .child("Users").get().addOnSuccessListener {

            }



    }


}