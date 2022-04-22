package kz.diaspora.app.domain.model.requests

data class ForgotResetPassword(
    val email: String,
    val password: String,
    val password_confirmation: String
)
