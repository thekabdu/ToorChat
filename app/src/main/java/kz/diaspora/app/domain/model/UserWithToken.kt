package kz.diaspora.app.domain.model

import com.google.gson.annotations.SerializedName

data class UserWithToken(
        @SerializedName("access_token") val access_token: String,
        @SerializedName("user") val user: User
)