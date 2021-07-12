package  kz.diaspora.app.domain.model.response

/**
 * various error status to know what happened if something goes wrong with a repository call
 */
enum class ErrorStatus {
    /**
     * error in connecting to repository (Server or Database)
     */
    NO_CONNECTION,
    /**
     * error in getting value (Json Error, Server Error, etc)
     */
    BAD_RESPONSE,
    /**
     * Time out  error
     */
    TIMEOUT,
    /**
     * an unexpected error
     */
    NOT_DEFINED,
    /**
     * bad credential
     */
    UNAUTHORIZED
}