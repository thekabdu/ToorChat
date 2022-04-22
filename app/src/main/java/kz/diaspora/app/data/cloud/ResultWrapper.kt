package kz.diaspora.app.data.cloud

import kz.diaspora.app.domain.model.response.ErrorResponse
import kz.diaspora.app.domain.model.response.ErrorStatus

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T): ResultWrapper<T>()
    data class Error(val status: ErrorStatus? = null, val code: Int? = null, val error: String? = null): ResultWrapper<Nothing>()
    data class GenericError(val code: Int? = null, val error: ErrorResponse? = null): ResultWrapper<Nothing>()
    object NetworkError: ResultWrapper<Nothing>()
}