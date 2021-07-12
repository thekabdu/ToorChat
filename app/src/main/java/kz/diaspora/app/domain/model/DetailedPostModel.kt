package kz.diaspora.app.domain.model

import com.google.gson.annotations.SerializedName

data class DetailedPostModel(
    @SerializedName("comments") val comments: List<CommentModel>,
    @SerializedName("post") val post: PostModel
)