package kz.diaspora.app.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RatingModel(
    @SerializedName("rating") val rating: Double?
) : Serializable