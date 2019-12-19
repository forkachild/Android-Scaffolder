package {{PACKAGE}}.utils.rx

import android.view.View
import android.view.ViewTreeObserver
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

val <T> Single<T>.iofy: Single<T>
    get() = this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .unsubscribeOn(Schedulers.io())

val View.measurements: Single<Pair<Int, Int>>
    get() = Single.create(ViewDimensionSingle(this))

private class ViewDimensionSingle(private val view: View) : SingleOnSubscribe<Pair<Int, Int>> {

    private lateinit var emitter: SingleEmitter<Pair<Int, Int>>

    private val layoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        removeListener()
        emitValues()
    }

    override fun subscribe(emitter: SingleEmitter<Pair<Int, Int>>) {
        this.emitter = emitter
        if (view.measuredWidth != 0 && view.measuredHeight != 0) {
            emitValues()
            return
        }

        addListener()
    }

    private fun addListener() {
        view.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
    }

    private fun removeListener() {
        view.viewTreeObserver.removeOnGlobalLayoutListener(layoutListener)
    }

    private fun emitValues() {
        emitter.onSuccess(Pair(view.measuredWidth, view.measuredHeight))
    }

}