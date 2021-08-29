package it.units.musicplatform.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import it.units.musicplatform.databinding.PostItemBinding
import it.units.musicplatform.entities.Post
import it.units.musicplatform.fragments.ProfileFragment
import it.units.musicplatform.utilities.PictureLoader


private var userId = FirebaseAuth.getInstance().currentUser!!.uid

class UserPostsAdapter(val profileFragment: ProfileFragment, var userPosts: List<Post>) : RecyclerView.Adapter<UserPostsAdapter.PostHolder>() {

    fun updateUsersPost(updatedUserPosts: List<Post>){
        userPosts = updatedUserPosts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostHolder(binding)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post = userPosts[position]
        holder.binding.songTextView.text = post.songName
        holder.binding.artistTextView.text = post.artistName
        PictureLoader.setSongCover(userId, post.id, holder.binding.songPictureImageView)
    }

    override fun getItemCount() = userPosts.size

    class PostHolder(val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root)
}