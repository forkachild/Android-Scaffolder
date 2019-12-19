package {{PACKAGE}}.ui.login

import android.content.Intent
import android.os.Bundle
import {{PACKAGE}}.R
import {{PACKAGE}}.base.BaseFragment
import {{PACKAGE}}.databinding.FragmentLoginBinding
import {{PACKAGE}}.utils.observeEvent
import {{PACKAGE}}.utils.rx.FacebookDelegate
import {{PACKAGE}}.utils.rx.GoogleDelegate
import {{PACKAGE}}.utils.rx.createFacebookMaybe
import {{PACKAGE}}.utils.rx.createGoogleMaybe
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    override val layoutRes: Int = R.layout.fragment_login
    override val viewModel: LoginViewModel by viewModel()

    private val facebookDelegate by lazy { FacebookDelegate() }
    private val googleDelegate by lazy { GoogleDelegate() }

    override fun onCreated(savedInstanceState: Bundle?) {
        super.onCreated(savedInstanceState)

        viewModel.loginWithFacebook.observeEvent(this) {
            viewModel.submitSocialModel(createFacebookMaybe(this, facebookDelegate))
        }

        viewModel.loginWithGoogle.observeEvent(this) {
            viewModel.submitSocialModel(createGoogleMaybe(this, googleDelegate))
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookDelegate.onActivityResult(requestCode, resultCode, data)
        googleDelegate.onActivityResult(requestCode, resultCode, data)
    }

}