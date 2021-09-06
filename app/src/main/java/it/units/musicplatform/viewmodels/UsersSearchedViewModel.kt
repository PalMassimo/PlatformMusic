package it.units.musicplatform.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.units.musicplatform.entities.User
import it.units.musicplatform.repositories.UsersSearchedRepository
import kotlinx.coroutines.launch

class UsersSearchedViewModel(userId: String) : ViewModel() {

    private val usersSearchRepository = UsersSearchedRepository(userId)
    private val _popularUsers = MutableLiveData<ArrayList<User>>()
    val popularUsers: LiveData<ArrayList<User>> = _popularUsers

    init {
        viewModelScope.launch {
            _popularUsers.postValue(usersSearchRepository.loadPopularUsers())
        }
    }

    suspend fun searchUsers(subName: String) = usersSearchRepository.searchUser(subName)

}