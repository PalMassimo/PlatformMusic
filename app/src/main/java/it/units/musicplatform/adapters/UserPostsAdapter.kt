package it.units.musicplatform.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.units.musicplatform.databinding.PostItemBinding
import it.units.musicplatform.entities.Post
import it.units.musicplatform.fragments.BottomSheetFragment
import it.units.musicplatform.fragments.ProfileFragment
import it.units.musicplatform.utilities.PictureLoader


class UserPostsAdapter(private val profileFragment: ProfileFragment, var postsIds: List<String>, var posts: ArrayList<Post>) :
    RecyclerView.Adapter<UserPostsAdapter.PostHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostHolder(binding)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {

        val post = posts[position]
        holder.binding.post = post
        PictureLoader.loadCover(profileFragment.requireContext(), holder.binding.songPictureImageView, post.uploaderId, post.id)

        holder.itemView.setOnLongClickListener {
            BottomSheetFragment.newInstance(position).run { show(profileFragment.parentFragmentManager, tag) }
            return@setOnLongClickListener true
        }

    }

    override fun getItemCount() = posts.size

    class PostHolder(val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root)
}