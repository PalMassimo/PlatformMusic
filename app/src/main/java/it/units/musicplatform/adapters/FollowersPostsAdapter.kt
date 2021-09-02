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
import it.units.musicplatform.enumerations.PreferenceOperation.*
import it.units.musicplatform.fragments.HomeFragment
import it.units.musicplatform.retrievers.DatabaseReferenceRetriever
import it.units.musicplatform.utilities.MediaPlayerManager
import it.units.musicplatform.utilities.PictureLoader
import it.units.musicplatform.utilities.PreferenceOperationParser
import it.units.musicplatform.utilities.SongTime

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
        val post = followersPostsList[position]

        setUpCardView(post, holder.binding)
        setUpCardListeners(post, holder.binding, position)
    }

    private fun likeAndDislikeImagesButtonListenersCallback(binding: PostCardBinding, post: Post, preference: Preference) {
        when (PreferenceOperationParser.changePreference(preference, post.id, homeFragment.userViewModel.user.value!!.likes, homeFragment.userViewModel.user.value!!.dislikes)) {
            ADD_LIKE -> homeFragment.userViewModel.addLike(post.id, ++post.numberOfLikes)
            REMOVE_LIKE -> homeFragment.userViewModel.removeLike(post.id, --post.numberOfLikes)
            ADD_DISLIKE -> homeFragment.userViewModel.addDislike(post.id, ++post.numberOfDislikes)
            REMOVE_DISLIKE -> homeFragment.userViewModel.removeDislike(post.id, --post.numberOfDislikes)
            FROM_LIKE_TO_DISLIKE -> homeFragment.userViewModel.fromLikeToDislike(post.id, --post.numberOfLikes, ++post.numberOfDislikes)
            FROM_DISLIKE_TO_LIKE -> homeFragment.userViewModel.fromDislikeToLike(post.id, ++post.numberOfLikes, --post.numberOfDislikes)
        }
        binding.numberOfLikesTextView.text = post.numberOfLikes.toString()
        binding.numberOfDislikesTextView.text = post.numberOfDislikes.toString()
        setLikeAndDislikeButton(binding, post.id)
    }


    private fun setUpCardListeners(post: Post, binding: PostCardBinding, position: Int) {

        binding.playPauseImageButton.setOnClickListener { mediaPlayerManager.doAction(position) }

        binding.likeImageButton.setOnClickListener { likeAndDislikeImagesButtonListenersCallback(binding, post, Preference.LIKE) }

        binding.dislikeImageButton.setOnClickListener { likeAndDislikeImagesButtonListenersCallback(binding, post, Preference.DISLIKE) }


//        binding.dow.setOnClickListener{
//            val songDownloader = SongDownloader(context, post)
//            songDownloader.download()
//            //TODO: set numberOf Downloads
//        }
    }

    override fun getItemCount() = followersPostsList.size

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

    private fun setLikeAndDislikeButton(binding: PostCardBinding, postId: String) {
        binding.likeImageButton.setColorFilter(if (homeFragment.userViewModel.user.value!!.likes.containsKey(postId)) R.color.white else R.color.black)
        binding.dislikeImageButton.setColorFilter(if (homeFragment.userViewModel.user.value!!.dislikes.containsKey(postId)) R.color.white else R.color.black)
    }

    private fun setUpCardView(post: Post, binding: PostCardBinding) {
        //TODO: questo non dovrebbe stare qui...
        DatabaseReferenceRetriever.userReference(post.uploaderId).get().addOnSuccessListener {
            binding.uploaderFullNameTextView.text = it.getValue(User::class.java)!!.fullName
        }

        setLikeAndDislikeButton(binding, post.id)
        binding.songTextView.text = post.songName
        binding.artistTextView.text = post.artistName
        binding.numberOfLikesTextView.text = post.numberOfLikes.toString()
        binding.numberOfDislikesTextView.text = post.numberOfDislikes.toString()
        binding.numberOfDownloadsTextView.text = post.numberOfDownloads.toString()
        binding.songDurationTextView.text = SongTime.toString(post.numberOfSeconds.toLong())

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
