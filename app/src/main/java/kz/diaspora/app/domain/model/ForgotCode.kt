package kz.diaspora.app.domain.model

data class ForgotCode(
        val email: String,
        val two_factor_code: Int
)
