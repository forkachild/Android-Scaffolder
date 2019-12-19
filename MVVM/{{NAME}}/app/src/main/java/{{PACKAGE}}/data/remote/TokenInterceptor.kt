package {{PACKAGE}}.data.remote

import {{PACKAGE}}.AUTH_HEADER
import {{PACKAGE}}.data.local.TokenStore
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val tokenStore: TokenStore) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        tokenStore.get()?.run { request.header(AUTH_HEADER, this) }

        val response = chain.proceed(request.build())
        response.header(AUTH_HEADER, null)?.run { tokenStore.put(this) }
        return response
    }

}