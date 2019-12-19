package {{PACKAGE}}.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (T?) -> Unit) {
    observe(owner, Observer { value: T? ->
        observer.invoke(value)
    })
}

fun <T> LiveData<T>.observeEvent(owner: LifecycleOwner, observer: () -> Unit) {
    observe(owner, Observer { observer.invoke() })
}

fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, observer: (T) -> Unit) {
    observe(owner, Observer { value: T? ->

        if (value != null) {
            observer.invoke(value)
        }

    })
}