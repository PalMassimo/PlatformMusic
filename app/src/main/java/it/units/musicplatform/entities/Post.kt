package it.units.musicplatform.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post (
    var id: String = "",
    var songName: String = "",
    var artistName: String = "",
    var uploaderId: String = "",
    var numberOfLikes: Int = 0,
    var numberOfSeconds: Int = 0,
    var numberOfDislikes: Int = 0,
    var numberOfDownloads: Int = 0,
    var songDownloadString: String = "",
    var coverDownloadString: String = "",
) : Parcelable