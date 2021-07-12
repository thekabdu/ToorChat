package kz.diaspora.app.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MaritalStatusModel(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?
) : Serializable