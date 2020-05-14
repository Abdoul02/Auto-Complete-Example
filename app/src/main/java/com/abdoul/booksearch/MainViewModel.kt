package com.abdoul.booksearch

import android.annotation.SuppressLint
import android.util.Log
import android.widget.AutoCompleteTextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abdoul.booksearch.data.ApiUtils
import com.abdoul.booksearch.model.Book
import com.abdoul.booksearch.model.Item
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {
    private val autoCompletePublishSubject = PublishRelay.create<String>()
    private var autoCompleteDisposable: Disposable? = null
    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>>
        get() = _items

    init {
        configureAutoComplete()
    }

    @SuppressLint("CheckResult")
    private fun configureAutoComplete() {
        autoCompleteDisposable = autoCompletePublishSubject
            .debounce(300, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMap { ApiUtils.getBookApiService().getBookData(it, 5) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _items.postValue(result.items)
            }, { error -> Log.d("AutoSearch", "${error.message}") })
    }

    fun onTextInputStateChanged(query: String) {
        if (query.trim().isNotEmpty()) {
            autoCompletePublishSubject.accept(query)
        }
    }



    override fun onCleared() {
        super.onCleared()
        autoCompleteDisposable?.dispose()
    }

}