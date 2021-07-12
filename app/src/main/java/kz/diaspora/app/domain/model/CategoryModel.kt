package kz.diaspora.app.domain.model

import com.google.gson.annotations.SerializedName

data class CategoryModel(
    @SerializedName("category_name") val category_name: String,
    @SerializedName("id") val id: Int,
    @SerializedName("image_path") val image_path: String,
    @SerializedName("post_count") val post_count: Int
)