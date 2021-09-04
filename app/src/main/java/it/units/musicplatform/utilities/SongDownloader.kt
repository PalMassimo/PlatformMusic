package it.units.musicplatform.utilities

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment.DIRECTORY_DOWNLOADS
import it.units.musicplatform.entities.Post

class SongDownloader(val context: Context, private val post: Post) {

    fun download() {

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val uri = Uri.parse(post.songFileDownloadString)
        val request = DownloadManager.Request(uri)
        request.setTitle("Download song " + post.songName)
        request.setMimeType("audio")
        request.setDescription("downloading song...")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, post.songName)
        downloadManager.enqueue(request)

    }

}

