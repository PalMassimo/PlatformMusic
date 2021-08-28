package it.units.musicplatform.entities

data class User(
    val id: String,
    val age: Int,
    val email: String,
    val fullName: String,
    val imageUrl: String,
    val numberOfLikes: Int,
    val numberOfDislikes: Int,
    val posts: Map<String, Boolean>,
    val followers: Map<String, Boolean>,
    val following: Map<String, Boolean>,
) {

    constructor() : this("", 0, "", "", "", 0, 0, emptyMap(), emptyMap(), emptyMap())

}