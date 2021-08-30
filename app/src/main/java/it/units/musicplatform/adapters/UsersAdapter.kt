package it.units.musicplatform.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.units.musicplatform.databinding.UserListItemBinding
import it.units.musicplatform.entities.User
import it.units.musicplatform.fragments.SearchFragment
import it.units.musicplatform.utilities.PictureLoader

class UsersAdapter(users: ArrayList<User>?, following: Set<String>?, val searchFragment: SearchFragment) : RecyclerView.Adapter<UsersAdapter.UserHolder>() {

    var users: ArrayList<User> = arrayListOf()
    var following: Set<String> = setOf()

    init {
        if (users != null) this.users = users
        if (following != null) this.following = following
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val binding = UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserHolder(binding)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val user = users[position]
        holder.binding.fullNameTextView.text = user.fullName
        PictureLoader.setProfileImage(user.id, holder.binding.profileImageView)

        holder.binding.switchElement.isChecked = following.contains(user.id)

        holder.binding.switchElement.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) searchFragment.addFollowing(user.id) else searchFragment.removeFollowing(user.id)
        }

    }

    override fun getItemCount() = users.size

    class UserHolder(val binding: UserListItemBinding) : RecyclerView.ViewHolder(binding.root)
}