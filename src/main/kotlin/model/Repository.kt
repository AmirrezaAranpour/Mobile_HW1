package model

data class Repository(
    val name: String,
    val description: String?,
    val url: String,
    val stars: Int,
    val forks: Int
) 