package it.units.musicplatform.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import it.units.musicplatform.R
import it.units.musicplatform.databinding.PostCardBinding
import it.units.musicplatform.entities.Post
import it.units.musicplatform.entities.User
import it.units.musicplatform.enumerations.Preference
import it.units.musicplatform.enumerations.PreferenceOperation.*
import it.units.musicplatform.fragments.HomeFragment
import it.units.musicplatform.retrievers.DatabaseReferenceRetriever
import it.units.musicplatform.retrievers.StorageReferenceRetriever
import it.units.musicplatform.utilities.*

class FollowersPostsAdapter(private val homeFragment: HomeFragment, private val recyclerView: RecyclerView, var followersPostsList: List<Post>) :
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
        setUpCardView(position, holder.binding)
        setUpCardListeners(holder.binding, position)
    }

    private fun setUpCardListeners(binding: PostCardBinding, position: Int) {

        val post = followersPostsList[position]

        binding.likeImageButton.setOnClickListener { homeFragment.changePreference(position, Preference.LIKE) }
        binding.dislikeImageButton.setOnClickListener { homeFragment.changePreference(position, Preference.DISLIKE) }
        binding.playPauseImageButton.setOnClickListener { mediaPlayerManager.doAction(position) }

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

        //TODO: questo non dovrebbe stare qui...
        DatabaseReferenceRetriever.userReference(post.uploaderId).get().addOnSuccessListener {
            binding.uploaderFullNameTextView.text = it.getValue(User::class.java)!!.fullName
        }

        binding.likeImageButton.setColorFilter(if (homeFragment.userViewModel.user.value!!.likes.containsKey(post.id)) R.color.white else R.color.black)
        binding.dislikeImageButton.setColorFilter(if (homeFragment.userViewModel.user.value!!.dislikes.containsKey(post.id)) R.color.white else R.color.black)
        binding.playPauseImageButton.setImageResource(if(mediaPlayerManager.currentSong==position) R.drawable.ic_pause else R.drawable.ic_play)
        binding.post = post

        GlideApp.with(homeFragment.requireContext()).load(StorageReferenceRetriever.coverReference(post.uploaderId, post.id))
            .into(binding.songPictureImageView)
        GlideApp.with(homeFragment.requireContext()).load(StorageReferenceRetriever.userImageReference(post.uploaderId))
            .into(binding.uploaderPictureImageView)

        binding.seekBar.max = post.numberOfSeconds
    }

    fun setLike(isLiked: Boolean, position: Int) = getPostHolder(position)!!.binding.likeImageButton.setColorFilter(if (isLiked) R.color.white else R.color.black)
    fun setDislike(isDisliked: Boolean, position: Int) = getPostHolder(position)!!.binding.dislikeImageButton.setColorFilter((if (isDisliked) R.color.white else R.color.black))


    inner class PostHolder(val binding: PostCardBinding) : RecyclerView.ViewHolder(binding.root) {

        private val songTime = SongTime()

        init {
            binding.seekBar.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
                override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        mediaPlayerManager.mediaPlayer.seekTo(progress * 1000)
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
