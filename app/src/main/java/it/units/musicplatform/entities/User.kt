package it.units.musicplatform.entities

data class User(
    val id: String= "",
    var age: Int = 0,
    val email: String = "",
    var fullName: String = "",
    val imageUrl: String = "",
    val posts: HashMap<String, Boolean> = HashMap(),
    val likes: HashMap<String, Boolean> = HashMap(),
    val dislikes: HashMap<String, Boolean> = HashMap(),
    val followers: HashMap<String, Boolean> = HashMap(),
    val following: HashMap<String, Boolean> = HashMap(),
)