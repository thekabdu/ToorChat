package kz.diaspora.app.domain.model

import com.google.gson.annotations.SerializedName

data class PostsCountModel(
    @SerializedName("favourite_posts") val favourite_posts: Int,
    @SerializedName("active") val active: Int,
    @SerializedName("not_active") val not_active: Int,
    @SerializedName("wait") val wait: Int
)