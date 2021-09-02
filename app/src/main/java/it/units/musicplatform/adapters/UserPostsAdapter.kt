package it.units.musicplatform.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.units.musicplatform.databinding.PostItemBinding
import it.units.musicplatform.entities.Post
import it.units.musicplatform.fragments.BottomSheetFragment
import it.units.musicplatform.fragments.ProfileFragment
import it.units.musicplatform.retrievers.StorageReferenceRetriever
import it.units.musicplatform.utilities.GlideApp


class UserPostsAdapter(private val profileFragment: ProfileFragment, var userPosts: ArrayList<Post>) : RecyclerView.Adapter<UserPostsAdapter.PostHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostHolder(binding)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post = userPosts[position]

        setUpHolderView(holder.binding, post)

        holder.itemView.setOnLongClickListener {
            BottomSheetFragment.newInstance(position).run { show(profileFragment.parentFragmentManager, tag) }
            return@setOnLongClickListener true
        }

    }

    private fun setUpHolderView(binding: PostItemBinding, post: Post) {
        binding.songTextView.text = post.songName
        binding.artistTextView.text = post.artistName
        GlideApp.with(profileFragment.requireContext()).load(StorageReferenceRetriever.coverReference(post.uploaderId, post.id)).into(binding.songPictureImageView)
    }

    fun removeElementAtPosition(position: Int) {
        userPosts.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount() = userPosts.size

    class PostHolder(val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root)
}