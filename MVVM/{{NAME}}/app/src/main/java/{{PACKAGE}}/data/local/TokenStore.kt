package {{PACKAGE}}.data.local

import android.content.Context

class TokenStore(context: Context) : PrefStringStore(context) {

    override val key: String
        get() = TOKEN

    companion object {

        const val TOKEN = "Token"

    }

}