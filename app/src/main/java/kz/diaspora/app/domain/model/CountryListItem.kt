package kz.diaspora.app.domain.model

import com.google.gson.annotations.SerializedName

data class CountryListItem(
        @SerializedName("country_name") val country_name: String,
        @SerializedName("id") val id: Int
)