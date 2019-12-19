package {{PACKAGE}}.ui.login

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LiveData
import {{PACKAGE}}.R
import {{PACKAGE}}.base.BaseViewModel
import {{PACKAGE}}.data.remote.Repository
import {{PACKAGE}}.model.SocialModel
import {{PACKAGE}}.utils.SingleLiveEvent
import io.reactivex.Maybe

class LoginViewModel(repository: Repository) : BaseViewModel(repository) {

    private val _loginWithFacebook = SingleLiveEvent()
    val loginWithFacebook: LiveData<Void>
        get() = _loginWithFacebook

    private val _loginWithGoogle = SingleLiveEvent()
    val loginWithGoogle: LiveData<Void>
        get() = _loginWithGoogle

    @get:Bindable
    var email: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.email)
        }

    @get:Bindable
    var password: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.password)
        }

    override fun onAttach() {
        super.onAttach()
        title = repository.getString(R.string.login)
    }

    fun forgotPasswordClick() {

    }

    fun loginClick() {

    }

    fun continueWithGoogleClick() {
        _loginWithGoogle()
    }

    fun continueWithFacebookClick() {
        _loginWithFacebook()
    }

    fun submitSocialModel(data: Maybe<SocialModel>) {
        disposableBag.add(

            data.subscribe({ Log.e("Social", it.toString()) }, { it.printStackTrace() })

        )
    }

}