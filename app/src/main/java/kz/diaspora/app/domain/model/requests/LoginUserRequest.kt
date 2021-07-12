package kz.diaspora.app.domain.model.requests

data class LoginUserRequest(
        val username: String,
        val password: String
)
