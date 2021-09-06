package it.units.musicplatform.repositories

import it.units.musicplatform.entities.User
import it.units.musicplatform.firebase.DatabaseTaskManager
import kotlinx.coroutines.tasks.await

class UsersSearchedRepository(val userId: String) {

    suspend fun loadPopularUsers(): ArrayList<User> {
        val popularUsers = ArrayList<User>()
        DatabaseTaskManager.getPopularUsers(userId, popularUsers).await()
        return popularUsers

    }

    suspend fun searchUser(subName: String): ArrayList<User> {
        val resultUsers = ArrayList<User>()
        DatabaseTaskManager.getMatchingUsersTask(userId, subName, resultUsers).await()
        return resultUsers
    }
}
