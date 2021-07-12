package kz.diaspora.app.domain.model.response

import com.google.gson.annotations.SerializedName

data class SendCommentResponse(
    @SerializedName("message") val message: Boolean
)
