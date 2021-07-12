package kz.diaspora.app.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ImageModel(
    @SerializedName("id_image") val id_image: Int?,
    @SerializedName("id_exhibit") val id_exhibit: Int?,
    @SerializedName("url") val url: String?
) : Serializable