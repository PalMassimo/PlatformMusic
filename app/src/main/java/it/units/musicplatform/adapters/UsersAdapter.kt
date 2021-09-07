package it.units.musicplatform.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import it.units.musicplatform.R
import it.units.musicplatform.databinding.UserItemBinding
import it.units.musicplatform.entities.User
import it.units.musicplatform.fragments.SearchFragment
import it.units.musicplatform.utilities.PictureLoader

private val PURPLE = Color.rgb(170, 66, 245)
private val WHITE_SMOKE = Color.rgb(245, 245, 245)

class UsersAdapter(var users: ArrayList<User>, var following: MutableSet<String>, private val searchFragment: SearchFragment) :
    RecyclerView.Adapter<UsersAdapter.UserHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserHolder(binding)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val user = users[position]

        holder.binding.fullNameTextView.text = user.username
        PictureLoader.loadProfilePicture(searchFragment.requireContext(), holder.binding.profileImageView, user.id)

        if (following.contains(user.id)) setUnfollowStyle(holder.binding.followUnfollowButton) else setFollowStyle(holder.binding.followUnfollowButton)

        holder.binding.followUnfollowButton.setOnClickListener {
            if (isUnfollowButton(holder.binding.followUnfollowButton)) {
                searchFragment.removeFollowing(user.id)
                setFollowStyle(holder.binding.followUnfollowButton)
            } else {
                searchFragment.addFollowing(user.id)
                setUnfollowStyle(holder.binding.followUnfollowButton)
            }
        }

    }

    override fun getItemCount() = users.size

    fun isUnfollowButton(button: Button) = button.text == searchFragment.getString(R.string.unfollow)

    private fun setFollowStyle(button: Button) {
        button.setBackgroundColor(WHITE_SMOKE)
        button.text = searchFragment.getString(R.string.follow)
        button.setTextColor(PURPLE)
    }

    private fun setUnfollowStyle(button: Button) {
        button.setBackgroundColor(PURPLE)
        button.text = searchFragment.getString(R.string.unfollow)
        button.setTextColor(WHITE_SMOKE)
    }

    class UserHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root)
}