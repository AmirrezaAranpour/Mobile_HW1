package model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class User(
    val login: String,
    val followers: Int,
    val following: Int,
    @SerializedName("created_at")
    val createdAt: Date,
    val repositories: List<Repository> = emptyList()
) 