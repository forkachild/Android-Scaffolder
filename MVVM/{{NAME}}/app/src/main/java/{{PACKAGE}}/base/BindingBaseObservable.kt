package {{PACKAGE}}.base

import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.databinding.PropertyChangeRegistry

open class BindingBaseObservable : BindingObservable {

    @Transient
    private var callbacks = PropertyChangeRegistry()

    override fun addOnPropertyChangedCallback(callback: OnPropertyChangedCallback) {
        callbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: OnPropertyChangedCallback) {
        callbacks.remove(callback)
    }

    override fun notifyChange() {
        callbacks.notifyCallbacks(this, 0, null)
    }

    override fun notifyPropertyChanged(fieldId: Int) {
        callbacks.notifyCallbacks(this, fieldId, null)
    }

}