package {{PACKAGE}}.base

import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import {{PACKAGE}}.data.remote.Repository
import {{PACKAGE}}.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(protected val repository: Repository) : ViewModel(),
    BindingObservable by BindingBaseObservable() {

    protected val disposableBag = CompositeDisposable()

    private val _navigateBack = SingleLiveEvent()
    val navigateBack: LiveData<Void>
        get() = _navigateBack

    @get:Bindable
    var title: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
        }

    @get:Bindable
    var subtitle: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
        }

    open fun onAttach() {

    }

    open fun onStart() {

    }

    open fun onForeground() {

    }

    open fun onBackground() {

    }

    open fun onStop() {

    }

    open fun onDetach() {

    }

    open fun backClick() {
        _navigateBack()
    }

}