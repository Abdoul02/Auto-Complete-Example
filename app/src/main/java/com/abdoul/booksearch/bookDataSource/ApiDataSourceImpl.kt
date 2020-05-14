package com.abdoul.booksearch.bookDataSource

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abdoul.booksearch.data.ApiUtils
import com.abdoul.booksearch.model.Book
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiDataSourceImpl : ApiDataSource {

    private val _downloadedBook = MutableLiveData<Book>()
    override val downloadedBook: LiveData<Book>
        get() = _downloadedBook

    @SuppressLint("CheckResult")
    override fun getBookData(title: String) {
        ApiUtils.getBookApiService().getBookData(title,10)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _downloadedBook.postValue(result)
            }, { error -> Log.d("AutoSearch", "${error.message}") })
    }
}


/*
*
* enqueue(object : Callback<Book> {
            override fun onFailure(call: Call<Book>, t: Throwable) {
                Log.d("Error", "Error: ${t.message}")
            }

            override fun onResponse(
                call: Call<Book>,
                response: Response<Book>
            ) {
                _downloadedBook.postValue(response.body())
            }
        })*/