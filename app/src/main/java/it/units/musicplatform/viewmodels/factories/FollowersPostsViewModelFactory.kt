package it.units.musicplatform.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.units.musicplatform.viewmodels.FollowersPostsViewModel


@Suppress("UNCHECKED_CAST")
class FollowersPostsViewModelFactory(private val userId: String): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FollowersPostsViewModel::class.java)){
            return FollowersPostsViewModel(userId) as T
        }
        throw IllegalArgumentException("FollowersPosts View model class not found")
    }

}