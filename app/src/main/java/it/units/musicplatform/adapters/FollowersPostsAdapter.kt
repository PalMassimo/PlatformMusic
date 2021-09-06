package it.units.musicplatform.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import it.units.musicplatform.R
import it.units.musicplatform.databinding.PostCardBinding
import it.units.musicplatform.entities.Post
import it.units.musicplatform.entities.User
import it.units.musicplatform.enumerations.Preference
import it.units.musicplatform.fragments.HomeFragment
import it.units.musicplatform.firebase.retrievers.DatabaseReferenceRetriever
import it.units.musicplatform.utilities.*

class FollowersPostsAdapter(
    private val homeFragment: HomeFragment,
    private val recyclerView: RecyclerView,
    var followersPostsList: List<Post>,
    var followersUsernames: HashMap<String, String>,
) : RecyclerView.Adapter<FollowersPostsAdapter.PostHolder>() {

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
        setUpCardView(position, holder.binding)
        setUpCardListeners(holder.binding, position)
    }

    private fun setUpCardListeners(binding: PostCardBinding, position: Int) {

        val post = followersPostsList[position]

        binding.likeImageButton.setOnClickListener { homeFragment.changePreference(position, Preference.LIKE) }
        binding.dislikeImageButton.setOnClickListener { homeFragment.changePreference(position, Preference.DISLIKE) }
        binding.playPauseImageButton.setOnClickListener {
            mediaPlayerManager.doAction(position)
        }

        binding.downloadImageButton.setOnClickListener { downloadView ->
            val songDownloader = SongDownloader(downloadView.context, post)
            songDownloader.download()
            homeFragment.updateNumberOfDownloads(position)
        }
    }

    override fun getItemCount() = followersPostsList.size

    private fun getPostHolder(position: Int) = recyclerView.findViewHolderForAdapterPosition(position) as PostHolder?


    fun songStarted(positionOldSong: Int, positionNewSong: Int) {
        if (positionOldSong != -1) {
            getPostHolder(positionOldSong)?.songStopped()
        }
        getPostHolder(positionNewSong)?.songPlayed()
    }

    fun songResumed(songPosition: Int) = getPostHolder(songPosition)?.songResumed()

    fun songPaused(songPosition: Int) = getPostHolder(songPosition)?.songPaused()

    fun songStopped(songPosition: Int) = getPostHolder(songPosition)?.songStopped()

    fun updateProgressBar(position: Int, progress: Int) = getPostHolder(position)?.updateSeekBar(progress)

    fun resetPost(currentSong: Int) = getPostHolder(currentSong)?.songStopped()

    private fun setUpCardView(position: Int, binding: PostCardBinding) {

        val post = followersPostsList[position]

        binding.uploaderFullNameTextView.text = followersUsernames[post.uploaderId]

        binding.post = post

        PictureLoader.loadCover(homeFragment.requireContext(), binding.songPictureImageView, post.uploaderId, post.id)
        PictureLoader.loadProfilePicture(homeFragment.requireContext(), binding.uploaderPictureImageView, post.uploaderId)

        binding.likeImageButton.setColorFilter(if (homeFragment.userViewModel.user.value!!.likes.containsKey(post.id)) R.color.white else R.color.black)
        binding.dislikeImageButton.setColorFilter(if (homeFragment.userViewModel.user.value!!.dislikes.containsKey(post.id)) R.color.white else R.color.black)
        binding.playPauseImageButton.setImageResource(if (mediaPlayerManager.currentSong == position) R.drawable.ic_pause else R.drawable.ic_play)
        binding.seekBar.max = post.numberOfSeconds
    }

    fun setLike(isLiked: Boolean, position: Int) = getPostHolder(position)!!.binding.likeImageButton.setColorFilter(if (isLiked) R.color.white else R.color.black)
    fun setDislike(isDisliked: Boolean, position: Int) = getPostHolder(position)!!.binding.dislikeImageButton.setColorFilter((if (isDisliked) R.color.white else R.color.black))


    inner class PostHolder(val binding: PostCardBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.seekBar.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
                override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        mediaPlayerManager.mediaPlayer.seekTo(progress * 1000)
                        binding.songTimeTextView.text = SongTime.format(progress)
                    }
                }
            })
        }


        fun songPlayed() = binding.playPauseImageButton.setImageResource(R.drawable.ic_pause)
        fun songPaused() = binding.playPauseImageButton.setImageResource(R.drawable.ic_play)
        fun songResumed() = binding.playPauseImageButton.setImageResource(R.drawable.ic_pause)
        fun songStopped() {
            songPaused()
            binding.songTimeTextView.text = SongTime.format(0)
            binding.seekBar.progress = 0
        }

        fun updateSeekBar(progress: Int) {
            binding.songTimeTextView.text = SongTime.format(progress)
            binding.seekBar.progress = progress
        }
    }


}
