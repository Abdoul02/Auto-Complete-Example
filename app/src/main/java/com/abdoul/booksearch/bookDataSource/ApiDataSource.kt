package com.abdoul.booksearch.bookDataSource

import androidx.lifecycle.LiveData
import com.abdoul.booksearch.model.Book

interface ApiDataSource {

    val downloadedBook: LiveData<Book>
    fun getBookData(title: String)
}