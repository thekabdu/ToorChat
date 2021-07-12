package kz.diaspora.app.domain.model

import com.google.gson.annotations.SerializedName

data class CityListItem(
        @SerializedName("city_name") val city_name: String,
        @SerializedName("id") val id: Int
)