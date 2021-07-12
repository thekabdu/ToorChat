package kz.diaspora.app.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CommentModel(
    @SerializedName("comment") val comment: String,
    @SerializedName("date") val date: String,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("surname") val surname: String,
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("user_image") val user_image: String
): Serializable