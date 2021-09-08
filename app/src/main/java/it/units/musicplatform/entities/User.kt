package it.units.musicplatform.entities

data class User(
    val id: String = "",
    val email: String = "",
    val username: String = "",
    val numberOfFollowers: Int = 0,
    val posts: HashMap<String, Boolean> = HashMap(),
    val likes: HashMap<String, Boolean> = HashMap(),
    val dislikes: HashMap<String, Boolean> = HashMap(),
    val followers: HashMap<String, Boolean> = HashMap(),
    val following: HashMap<String, Boolean> = HashMap(),
)


{

    constructor(id: String, email: String, username: String) : this(id = id, email = email, username = username, 0)

}
