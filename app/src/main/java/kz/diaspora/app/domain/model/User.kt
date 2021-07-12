package kz.diaspora.app.domain.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("IMO") var IMO: String?,
    @SerializedName("birthday") var birthday: Any?,
    @SerializedName("city_id") val city_id: Any?,
    @SerializedName("created_at") val created_at: String?,
    @SerializedName("ctrunity_id") val ctrunity_id: Any?,
    @SerializedName("email") val email: String?,
    @SerializedName("email_verified_at") val email_verified_at: Any?,
    @SerializedName("facebook") var facebook: String?,
    @SerializedName("gender") var gender: Any?,
    @SerializedName("id") val id: Int?,
    @SerializedName("instagram") var instagram: String?,
    @SerializedName("marital_status") var marital_status: String?,
    @SerializedName("name") var name: String,
    @SerializedName("native_country_id") val native_country_id: Int?,
    @SerializedName("phone_number") var phone_number: String?,
    @SerializedName("user_image") val user_image: String,
    @SerializedName("role_id") val role_id: Int?,
    @SerializedName("status") var status: Int?,
    @SerializedName("surname") var surname: String?,
    @SerializedName("telegram") var telegram: String?,
    @SerializedName("twitter") val twitter: String?,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("username") val username: String,
    @SerializedName("viber") var viber: String?,
    @SerializedName("whatsapp") var whatsapp: String?
)