package {{PACKAGE}}.utils.rx

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import {{PACKAGE}}.model.SocialModel
import io.reactivex.Maybe
import io.reactivex.MaybeEmitter
import io.reactivex.MaybeOnSubscribe

fun createFacebookMaybe(
    fragment: Fragment,
    callbackManager: CallbackManager
): Maybe<SocialModel> = Maybe.create(
    FacebookMaybeOnSubscribe(
        fragment,
        callbackManager
    )
)

class FacebookDelegate : CallbackManager by CallbackManager.Factory.create()

class FacebookMaybeOnSubscribe(
    private val fragment: Fragment,
    private val callbackManager: CallbackManager
) : MaybeOnSubscribe<SocialModel> {

    private lateinit var emitter: MaybeEmitter<SocialModel>
    private var graphRequest: GraphRequestAsyncTask? = null

    private val isLoggedIn: Boolean
        get() {
            val accessToken = AccessToken.getCurrentAccessToken()
            return accessToken != null && !accessToken.isExpired
        }

    private val loginResultCallback = object : FacebookCallback<LoginResult> {

        override fun onSuccess(result: LoginResult) {
            fetchProfile()
        }

        override fun onCancel() {
            cancel()
        }

        override fun onError(error: FacebookException) {
            error(error)
        }

    }

    private val graphCallback = GraphRequest.GraphJSONObjectCallback { json, response ->

        try {
            val id = json.getString("id")
            val name = json.getString("name")
            val email = json.getString("email")

            success(SocialModel(id, name, email, SocialModel.Provider.Facebook))
        } catch (e: Exception) {
            cancel()
        }

    }

    override fun subscribe(emitter: MaybeEmitter<SocialModel>) {
        this.emitter = emitter

        if (!isLoggedIn) {
            LoginManager.getInstance().registerCallback(callbackManager, loginResultCallback)
            LoginManager.getInstance().logInWithReadPermissions(
                fragment,
                PERMISSIONS
            )
        } else {
            fetchProfile()
        }

        emitter.setCancellable {
            graphRequest?.apply {
                if (!isCancelled) {
                    cancel(true)
                }
                graphRequest = null
            }
        }
    }

    private fun fetchProfile() {
        val accessToken = AccessToken.getCurrentAccessToken()

        graphRequest = GraphRequest.newMeRequest(
            accessToken,
            graphCallback
        ).apply {
            parameters =
                FIELDS
        }.executeAsync()
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

        private val PERMISSIONS = listOf("public_profile", "email")
        private val FIELDS = Bundle().apply { putString("fields", "id,name,email") }

    }

}