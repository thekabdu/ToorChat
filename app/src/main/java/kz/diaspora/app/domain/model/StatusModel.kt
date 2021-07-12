package kz.diaspora.app.domain.model

import com.google.gson.annotations.SerializedName

data class StatusModel(
    @SerializedName("message") val message: Boolean?
)