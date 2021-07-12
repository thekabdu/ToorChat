package kz.diaspora.app.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class ChatModel(
    @SerializedName("id") var id: Int,
    @SerializedName("chat_name") var chat_name: String?,
    @SerializedName("country_id") var country_id: Int?,
    @SerializedName("city_id") var city_id: Int?,
    @SerializedName("chat_room") var chat_room: String?,
    @SerializedName("created_user_id") var created_user_id: Int?,
    @SerializedName("created_at") var created_at: Date?,
    @SerializedName("updated_at") var updated_at: Date?,
    @SerializedName("chat_users_count") var chat_users_count: Int?,
    @SerializedName("is_run") var is_run: Boolean?,
    @SerializedName("is_liked") var is_liked: Boolean,
) :Serializable