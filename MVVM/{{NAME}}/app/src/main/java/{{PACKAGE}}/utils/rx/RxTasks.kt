package {{PACKAGE}}.utils.rx

import com.google.android.gms.tasks.Task
import io.reactivex.Single

val <T> Task<T>.asSingle: Single<T>
    get() = Single.create { emitter ->
        addOnSuccessListener { emitter.onSuccess(it) }
        addOnFailureListener { emitter.onError(it) }
    }