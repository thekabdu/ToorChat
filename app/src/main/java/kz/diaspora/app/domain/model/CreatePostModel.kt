package kz.diaspora.app.domain.model

data class CreatePostModel(
    val category_id: Int,
    val city_id: Int,
    val created_at: String,
    val description: String,
    val id: Int,
    val photo_path: String,
    val seen: Int,
    val updated_at: String,
    val title: String,
    val phone: String,
    val user_id: Int
)