package it.units.musicplatform.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import it.units.musicplatform.databinding.PostItemBinding
import it.units.musicplatform.entities.Post
import it.units.musicplatform.fragments.BottomSheetFragment
import it.units.musicplatform.fragments.ProfileFragment
import it.units.musicplatform.utilities.PictureLoader


private var userId = FirebaseAuth.getInstance().currentUser!!.uid

class UserPostsAdapter(private val profileFragment: ProfileFragment, var userPosts: ArrayList<Post>) : RecyclerView.Adapter<UserPostsAdapter.PostHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostHolder(binding)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post = userPosts[position]

        setUpHolderView(holder.binding, post)

        holder.itemView.setOnLongClickListener{

            val bottomSheetFragment = BottomSheetFragment.newInstance(position)
            bottomSheetFragment.show(profileFragment.parentFragmentManager, bottomSheetFragment.tag)


            return@setOnLongClickListener true
        }

    }

    private fun setUpHolderView(binding: PostItemBinding, post: Post){
        binding.songTextView.text = post.songName
        binding.artistTextView.text = post.artistName
        PictureLoader.setSongCover(userId, post.id, binding.songPictureImageView)
    }

    fun removeElementAtPosition(position : Int){
        userPosts.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount() = userPosts.size

    class PostHolder(val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root)
}