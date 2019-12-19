package {{PACKAGE}}.base

import androidx.databinding.Observable

interface BindingObservable : Observable {

    fun notifyChange()

    fun notifyPropertyChanged(fieldId: Int)

}