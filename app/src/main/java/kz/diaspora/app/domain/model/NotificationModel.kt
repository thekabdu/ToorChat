package kz.diaspora.app.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NotificationModel(
    @SerializedName("id") val id: Int?,
    @SerializedName("comment") val comment: String?,
    @SerializedName("user_id") val user_id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("surname") val surname: String?,
    @SerializedName("user_image") val user_image: String?,
    @SerializedName("date") val date: String?,
    @SerializedName("post_id") val post_id: Int?,
) : Serializable