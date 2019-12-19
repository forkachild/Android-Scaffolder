package {{PACKAGE}}.data.local

import android.content.Context

sealed class PrefStore<T>(context: Context) : Store<T> {

    abstract val key: String
    protected val prefs =
        context.getSharedPreferences("${context.packageName}.store", Context.MODE_PRIVATE)

}

abstract class PrefStringStore(context: Context) : PrefStore<String>(context) {

    override fun put(data: String) {
        prefs.edit().putString(key, data).apply()
    }

    override fun get(): String? =
        prefs.getString(key, null)

    override fun remove() {
        prefs.edit().remove(key).apply()
    }

}