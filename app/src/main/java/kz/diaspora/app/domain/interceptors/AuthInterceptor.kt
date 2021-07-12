package kz.diaspora.app.domain.interceptors

import kz.diaspora.app.data.db.Prefs
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AuthInterceptor(var prefs: Prefs) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest: Request
        val requestBuilder = request.newBuilder()
        if (prefs.getToken() != null) {
            val token = "Bearer " + prefs.getToken()
            requestBuilder.addHeader("Authorization", token)
        }
        newRequest = requestBuilder.build()
        return chain.proceed(newRequest)
    }

}