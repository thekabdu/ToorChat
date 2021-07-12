package kz.diaspora.app.domain.model.requests

data class SendCommentRequest (
    val post_id: Int,
    val comment: String
    )