package kz.diaspora.app.domain.model.requests

import com.google.gson.annotations.SerializedName

data class RegisterUserRequest(
        @SerializedName("username") val username: String,
        @SerializedName("password") val password: String,
        @SerializedName("name") val name: String,
        @SerializedName("surname") val surname: String,
        @SerializedName("phone_or_email") val phone_or_email: String
)
