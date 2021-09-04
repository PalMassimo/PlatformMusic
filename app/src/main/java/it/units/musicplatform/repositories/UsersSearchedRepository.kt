package it.units.musicplatform.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import it.units.musicplatform.entities.User
import it.units.musicplatform.retrievers.DatabaseReferenceRetriever
import kotlinx.coroutines.tasks.await
import java.util.*
import java.util.stream.StreamSupport
import kotlin.collections.ArrayList

class UsersSearchedRepository(val userId: String) {

    suspend fun loadPopularUsers(): ArrayList<User> {
        val popularUsers = ArrayList<User>()
        DatabaseReferenceRetriever.users().orderByChild("numberOfLikes").limitToLast(5).get().continueWith { usersDataSnapshotTask: Task<DataSnapshot> ->
            val usersDataSnapshot = usersDataSnapshotTask.result
            StreamSupport.stream(usersDataSnapshot?.children!!.spliterator(), false)
                .map { userSnapshot: DataSnapshot -> userSnapshot.getValue(User::class.java) }
                .filter { user: User? -> !user?.id.equals(userId) }
                .forEach { user: User? -> popularUsers.add(user!!) }
        }.await()

        return popularUsers

    }

    suspend fun searchUser(subName: String): ArrayList<User>{

        val resultUsers = ArrayList<User>()

        DatabaseReferenceRetriever.users().get().continueWith { usersDataSnapshotTask :Task<DataSnapshot> ->
            StreamSupport.stream(usersDataSnapshotTask.result?.children?.spliterator(), false)
                .map{ userSnapshot: DataSnapshot -> userSnapshot.getValue(User::class.java) }
                .filter { user: User? -> user?.username!!.toLowerCase(Locale.ROOT).contains(subName.toLowerCase(Locale.ROOT)) && user.id != userId }
                .forEach { resultUsers.add(it!!) }
        }.await()

        return resultUsers
        }
    }
