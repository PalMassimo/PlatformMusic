package it.units.musicplatform.utilities

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment.DIRECTORY_DOWNLOADS
import it.units.musicplatform.entities.Post

class SongDownloader(val context: Context, val post: Post) {

    fun download() {

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val uri = Uri.parse(post.songFileDownloadString)
        val request = android.app.DownloadManager.Request(uri)
        request.setTitle("Download")
        request.setDescription("downloading song...")
        request.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, post.songName.plus(".").plus(post.songExtension))
        downloadManager.enqueue(request)

    }

}

