package it.units.musicplatform.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import it.units.musicplatform.entities.User
import it.units.musicplatform.repositories.UsersSearchedRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private val userId = FirebaseAuth.getInstance().currentUser!!.uid

class UsersSearchedViewModel : ViewModel() {

    private val usersSearchRepository = UsersSearchedRepository(userId)
    private val _popularUsers = MutableLiveData<ArrayList<User>>()
    val popularUsers : LiveData<ArrayList<User>> = _popularUsers

    init{
        getPopularUsers()
    }

    private fun getPopularUsers(){
        GlobalScope.launch {
            _popularUsers.postValue(usersSearchRepository.loadPopularUsers())
        }
    }

    suspend fun searchUsers(subName: String): ArrayList<User>{
        return usersSearchRepository.searchUser(subName)
    }

}