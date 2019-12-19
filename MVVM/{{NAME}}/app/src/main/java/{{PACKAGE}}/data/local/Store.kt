package {{PACKAGE}}.data.local

interface Store<T> {

    fun put(data: T)

    fun get(): T?

    fun remove()

}