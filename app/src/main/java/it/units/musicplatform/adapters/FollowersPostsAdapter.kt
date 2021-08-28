package it.units.musicplatform.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.units.musicplatform.databinding.PostCardBinding
import it.units.musicplatform.entities.Post
import it.units.musicplatform.entities.User
import it.units.musicplatform.retrievers.DatabaseReferenceRetriever
import it.units.musicplatform.utilities.PictureLoader

class FollowersPostsAdapter(private val context: Context, private var followersPosts: List<Post>) :
    RecyclerView.Adapter<FollowersPostsAdapter.ViewHolder>() {

    fun setFollowersPosts(followersPosts: List<Post>) {
        this.followersPosts = followersPosts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = followersPosts[position]
        holder.binding.post = post

        setUpCardView(post, holder.binding)

    }

    override fun getItemCount(): Int {
        return followersPosts.size
    }

    private fun setUpCardView(post: Post, binding: PostCardBinding) {
        DatabaseReferenceRetriever.userReference(post.uploaderId).get().addOnSuccessListener {
            binding.uploaderFullNameTextView.text = it.getValue(User::class.java)!!.fullName
        }

        PictureLoader.setSongCover(post.uploaderId, post.id, binding.songPictureImageView)
        PictureLoader.setProfileImage(post.uploaderId, binding.uploaderPictureImageView)
        binding.seekBar.max = post.numberOfSeconds
    }

    class ViewHolder(val binding: PostCardBinding) : RecyclerView.ViewHolder(binding.root){

    }


//    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//
//    }


}
