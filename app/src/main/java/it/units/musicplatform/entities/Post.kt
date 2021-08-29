package it.units.musicplatform.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    var id: String,
    val likes: Map<String, Boolean>,
    val dislikes: Map<String, Boolean>,
    var songName: String,
    var artistName: String,
    var uploaderId: String,
    var songExtension: String,
    var numberOfSeconds: Int,
    var numberOfDownloads: Int,
    var songFileDownloadString: String,
    var songPictureDownloadString: String,
) : Parcelable {

    constructor() : this("", emptyMap(), emptyMap(), "", "", "", "", 0, 0, "", "")

}