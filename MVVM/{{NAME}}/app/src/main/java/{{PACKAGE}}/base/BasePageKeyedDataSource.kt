package {{PACKAGE}}.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import {{PACKAGE}}.data.remote.Repository
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

abstract class BasePageKeyedDataSource<Data>(
    protected val repository: Repository,
    private val disposableBag: CompositeDisposable
) : PageKeyedDataSource<Int, Data>() {

    private val _initialLoading = MutableLiveData<Boolean>()
    val initialLoading: LiveData<Boolean>
        get() = _initialLoading

    private val _loadingBefore = MutableLiveData<Boolean>()
    val loadingBefore: LiveData<Boolean>
        get() = _loadingBefore

    private val _loadingAfter = MutableLiveData<Boolean>()
    val loadingAfter: LiveData<Boolean>
        get() = _loadingAfter

    final override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Data>
    ) {

        disposableBag.add(
            load(1, params.requestedLoadSize)
                .doOnSubscribe { _initialLoading.postValue(true) }
                .doFinally { _initialLoading.postValue(false) }
                .subscribe(
                    { callback.onResult(it, null, 2) },
                    { it.printStackTrace() }
                )
        )

    }

    final override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Data>) {

        disposableBag.add(
            load(params.key, params.requestedLoadSize)
                .doOnSubscribe { _loadingBefore.postValue(true) }
                .doFinally { _loadingBefore.postValue(false) }
                .subscribe(
                    { callback.onResult(it, params.key - 1) },
                    { it.printStackTrace() }
                )
        )

    }

    final override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Data>) {

        disposableBag.add(
            load(params.key, params.requestedLoadSize)
                .doOnSubscribe { _loadingAfter.postValue(true) }
                .doFinally { _loadingAfter.postValue(false) }
                .subscribe(
                    { callback.onResult(it, params.key + 1) },
                    { it.printStackTrace() }
                )
        )

    }

    abstract fun load(page: Int, pageSize: Int): Single<List<Data>>

    abstract class Factory<Data, DS : BasePageKeyedDataSource<Data>>(
        private val repository: Repository,
        private val disposableBag: CompositeDisposable
    ) : DataSource.Factory<Int, Data>() {

        private val _newDataSource = MutableLiveData<DS>()
        val newDataSource: LiveData<DS>
            get() = _newDataSource

        val initialLoading: LiveData<Boolean> =
            Transformations.switchMap(_newDataSource) { it.initialLoading }

        val loadingBefore: LiveData<Boolean> =
            Transformations.switchMap(_newDataSource) { it.loadingBefore }

        val loadingAfter: LiveData<Boolean> =
            Transformations.switchMap(_newDataSource) { it.loadingAfter }

        final override fun create(): DataSource<Int, Data> =
            create(repository, disposableBag).apply {
                _newDataSource.postValue(this)
            }

        abstract fun create(repository: Repository, disposableBag: CompositeDisposable): DS

    }

}