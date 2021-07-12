package kz.diaspora.app.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SavePostModel(
    @SerializedName("path") val path: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("category_id") val category_id: String?,
//    @SerializedName("email") val email: String?,
    @SerializedName("phone") val phone: String?,
) : Serializable