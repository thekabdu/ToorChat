package kz.diaspora.app.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SuccessModel(
        @SerializedName("message") val message: String,
        //@SerializedName("project") val project: ProjectModel
) : Serializable
