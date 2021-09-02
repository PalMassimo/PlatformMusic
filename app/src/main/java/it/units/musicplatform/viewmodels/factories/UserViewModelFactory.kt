package it.units.musicplatform.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.units.musicplatform.viewmodels.UserViewModel
import java.lang.IllegalArgumentException


@Suppress("UNCHECKED_CAST")
class UserViewModelFactory(private val userId: String): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserViewModel::class.java)){
            return UserViewModel(userId) as T
        }
        throw IllegalArgumentException("View model class not found")
    }

}