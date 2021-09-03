package it.units.musicplatform.utilities

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.os.Message
import it.units.musicplatform.adapters.FollowersPostsAdapter
import java.io.IOException

class MediaPlayerManager(private val adapter: FollowersPostsAdapter) {

    val mediaPlayer = MediaPlayer()

    var currentSong = -1
        private set

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.what == 1 && mediaPlayer.isPlaying) {
                val songTimeInSeconds = mediaPlayer.currentPosition / 1000
                adapter.updateProgressBar(currentSong, songTimeInSeconds)
                sendEmptyMessageDelayed(msg.what, 1000)
            }
        }
    }

    init {
        mediaPlayer.setOnCompletionListener {
            adapter.resetPost(currentSong)
            mediaPlayer.reset()
        }
    }

    fun doAction(songIndex: Int) {
        if (currentSong == songIndex) {
            if (mediaPlayer.isPlaying) pauseSong(songIndex) else resumeSong(songIndex)
        } else {
            stopSong(currentSong)
            playAnotherSong(songIndex)
        }
    }

    private fun playAnotherSong(postIndex: Int) {
        if (currentSong != -1) mediaPlayer.reset()

        try {
            mediaPlayer.setDataSource(adapter.followersPostsList[postIndex].songFileDownloadString)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                it.start()
                adapter.songStarted(currentSong, postIndex)
                currentSong = postIndex
                handler.sendEmptyMessageAtTime(1, 1000)
            }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    private fun pauseSong(postIndex: Int) {
        mediaPlayer.pause()
        handler.removeMessages(1)
        adapter.songPaused(postIndex)
    }

    private fun resumeSong(postIndex: Int) {
        mediaPlayer.start()
        handler.sendEmptyMessageAtTime(1, 1000)
        adapter.songResumed(postIndex)
    }

    private fun stopSong(postIndex: Int) {
        if (postIndex == -1) return
        mediaPlayer.pause()
        handler.removeMessages(1)
        adapter.songStopped(postIndex)
    }

}