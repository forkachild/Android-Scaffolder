package {{PACKAGE}}.utils

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent : MutableLiveData<Void>() {

    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in Void>) {

        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of change")
        }

        super.observe(owner, Observer {

            if (pending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }

        })

    }

    @MainThread
    override fun setValue(value: Void?) {
        pending.set(true)
        super.setValue(value)
    }

    @MainThread
    operator fun invoke() {
        value = null
    }

    companion object {

        const val TAG = "SingleLiveEvent"

    }

}