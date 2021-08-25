package it.units.musicplatform.entities

data class Post(
    val id: String,
    val likes: Map<String, Boolean>,
    val dislikes: Map<String, Boolean>,
    val songName: String,
    val artistName: String,
    val uploaderId: String,
    val songExtension: String,
    val numberOfSeconds: Int,
    val numberOfDownloads: Int,
    val songFileDownloadString: String,
    val songPictureDownloadString: String,
) {

    constructor() : this("", emptyMap(), emptyMap(), "", "", "", "", 0, 0, "", "")

}