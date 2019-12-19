package {{PACKAGE}}.utils.rx

import android.content.Intent
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import {{PACKAGE}}.model.SocialModel
import io.reactivex.Maybe
import io.reactivex.MaybeEmitter
import io.reactivex.MaybeOnSubscribe

fun createGoogleMaybe(
    fragment: Fragment,
    delegate: GoogleDelegate
): Maybe<SocialModel> = Maybe.create(GoogleMaybeOnSubscribe(fragment, delegate))

class GoogleDelegate {

    var client: GoogleSignInClient? = null
    var onSuccess: ((GoogleSignInAccount) -> Unit)? = null
    var onError: ((Throwable) -> Unit)? = null

    fun signIn(fragment: Fragment) {
        fragment.startActivityForResult(
            client?.signInIntent,
            REQUEST_CODE
        )
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            try {
                GoogleSignIn.getSignedInAccountFromIntent(data)
                    .getResult(ApiException::class.java)?.run {
                        onSuccess?.invoke(this)
                    }
            } catch (e: ApiException) {
                Log.e("Status", e.statusCode.toString())
                onError?.invoke(e)
            }
        }
    }

    companion object {

        private const val REQUEST_CODE = 62000

    }

}

class GoogleMaybeOnSubscribe(
    private val fragment: Fragment,
    private val delegate: GoogleDelegate
) : MaybeOnSubscribe<SocialModel> {

    private lateinit var emitter: MaybeEmitter<SocialModel>

    private val isLoggedIn: Boolean
        get() = GoogleSignIn.getLastSignedInAccount(fragment.requireContext()) != null

    override fun subscribe(emitter: MaybeEmitter<SocialModel>) {
        this.emitter = emitter

        delegate.client = GoogleSignIn.getClient(fragment.requireContext(), GSO)
        delegate.onSuccess = { fetchProfile(it) }
        delegate.onError = { error(it) }

        if (!isLoggedIn) {
            delegate.signIn(fragment)
        } else {
            GoogleSignIn.getLastSignedInAccount(fragment.requireContext())?.let {
                fetchProfile(it)
            }
        }
    }

    private fun fetchProfile(account: GoogleSignInAccount) {
        try {
            val id = account.id!!
            val name = account.displayName!!
            val email = account.email!!

            success(SocialModel(id, name, email, SocialModel.Provider.Google))
        } catch (e: Exception) {
            error(e)
        }
    }

    private fun success(data: SocialModel) {
        emitter.onSuccess(data)
    }

    private fun cancel() {
        emitter.onComplete()
    }

    private fun <T : Throwable> error(error: T) {
        emitter.onError(error)
    }

    companion object {

        private val GSO = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

    }

}