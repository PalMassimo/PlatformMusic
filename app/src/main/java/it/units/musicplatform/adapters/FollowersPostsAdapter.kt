package it.units.musicplatform.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import it.units.musicplatform.R
import it.units.musicplatform.databinding.PostCardBinding
import it.units.musicplatform.entities.Post
import it.units.musicplatform.entities.User
import it.units.musicplatform.retrievers.DatabaseReferenceRetriever
import it.units.musicplatform.utilities.MediaPlayerManager
import it.units.musicplatform.utilities.PictureLoader
import it.units.musicplatform.utilities.SongDownloader
import it.units.musicplatform.utilities.SongTime

class FollowersPostsAdapter(private val context: Context, private val recyclerView: RecyclerView, var followersPostsList: List<Post>) :
    RecyclerView.Adapter<FollowersPostsAdapter.PostHolder>() {

    val mediaPlayerManager = MediaPlayerManager(this)

    fun setFollowersPosts(followersPostsList: List<Post>) {
        this.followersPostsList = followersPostsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = PostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostHolder(binding)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post = followersPostsList[position]
        holder.binding.post = post

        setUpCardView(post, holder.binding)
        setUpCardListeners(post, holder.binding, position)
    }

    private fun setUpCardListeners(post: Post, binding: PostCardBinding, position: Int) {
        binding.playPauseImageButton.setOnClickListener { mediaPlayerManager.doAction(position) }
        binding.downloadImageButton.setOnClickListener{
            val songDownloader = SongDownloader(context, post)
            songDownloader.download()
        }
    }

    override fun getItemCount(): Int = followersPostsList.size

    private fun getPostHolder(position: Int) = recyclerView.findViewHolderForAdapterPosition(position) as PostHolder

    fun songStarted(positionOldSong: Int, positionNewSong: Int) {
        if (positionOldSong != -1) {
            getPostHolder(positionOldSong).songStopped()
        }
        getPostHolder(positionNewSong).songPlayed()
    }

    fun songResumed(songPosition: Int) = getPostHolder(songPosition).songResumed()

    fun songPaused(songPosition: Int) = getPostHolder(songPosition).songPaused()

    fun songStopped(songPosition: Int) = getPostHolder(songPosition).songStopped()

    fun updateProgressBar(position: Int, progress: Int) = getPostHolder(position).updateSeekBar(progress)

    fun resetPost(currentSong: Int) = getPostHolder(currentSong).songStopped()

    private fun setUpCardView(post: Post, binding: PostCardBinding) {
        DatabaseReferenceRetriever.userReference(post.uploaderId).get().addOnSuccessListener {
            binding.uploaderFullNameTextView.text = it.getValue(User::class.java)!!.fullName
        }

        PictureLoader.setSongCover(post.uploaderId, post.id, binding.songPictureImageView)
        PictureLoader.setProfileImage(post.uploaderId, binding.uploaderPictureImageView)
        binding.seekBar.max = post.numberOfSeconds
    }

    inner class PostHolder(val binding: PostCardBinding) : RecyclerView.ViewHolder(binding.root) {

        private val songTime = SongTime()

        init {
            binding.seekBar.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
                override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        mediaPlayerManager.mediaPlayer.seekTo(progress * 1000);
                        songTime.setSongTime(progress)
                        binding.songTimeTextView.text = songTime.toString()
                    }
                }

            })
        }


        fun songPlayed() = binding.playPauseImageButton.setImageResource(R.drawable.ic_pause)
        fun songPaused() = binding.playPauseImageButton.setImageResource(R.drawable.ic_play)
        fun songResumed() = binding.playPauseImageButton.setImageResource(R.drawable.ic_pause)
        fun songStopped() {
            songPaused()
            binding.songTimeTextView.text = SongTime.toString(0, 0)
            binding.seekBar.progress = 0
        }

        fun updateSeekBar(progress: Int) {
            songTime.setSongTime(progress)
            binding.songTimeTextView.text = songTime.toString()
            binding.seekBar.progress = progress
        }
    }


}
