package kz.diaspora.app.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class MessageModel(
    @SerializedName("id") val id: Int,
    @SerializedName("chat_id") val chat_id: Int?,
    @SerializedName("user_id") val user_id: Int?,
    @SerializedName("target_user_id") val target_user_id: Int?,
    @SerializedName("text") val text: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("created_at") val created_at: String?,
    @SerializedName("updated_at") val updated_at: String?,
    @SerializedName("is_my_message") var is_my_message: Boolean?,
    @SerializedName("user_image") val user_image: String?,
) : Serializable