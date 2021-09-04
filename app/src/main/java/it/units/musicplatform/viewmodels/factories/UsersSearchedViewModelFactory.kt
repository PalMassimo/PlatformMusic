package it.units.musicplatform.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.units.musicplatform.viewmodels.UsersSearchedViewModel


@Suppress("UNCHECKED_CAST")
class UsersSearchedViewModelFactory(private val userId: String): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UsersSearchedViewModel::class.java)){
            return UsersSearchedViewModel(userId) as T
        }
        throw IllegalArgumentException("FollowersPosts View model class not found")
    }

}