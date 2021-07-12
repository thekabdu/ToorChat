package kz.diaspora.app.domain.model

import com.google.gson.annotations.SerializedName

data class PostListItemModel(
    @SerializedName("category_id") val category_id: Int,
    @SerializedName("city_id") val city_id: Int,
    @SerializedName("comments_count") val comments_count: Int,
    @SerializedName("date") val date: String,
    @SerializedName("description") val description: String,
    @SerializedName("email") val email: String,
    @SerializedName("id") val id: Int,
    @SerializedName("last_comment") val _commentModel: CommentModel,
    @SerializedName("likes") val likes: Int,
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("post_image") val post_image: String,
    @SerializedName("seen") val seen: Int,
    @SerializedName("surname") val surname: String,
    @SerializedName("title") val title: String,
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("user_image") val user_image: String
)