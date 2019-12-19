package {{PACKAGE}}.data.remote

import androidx.annotation.StringRes

interface Repository {

    fun getString(@StringRes resId: Int): String

}