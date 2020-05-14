package com.abdoul.booksearch.data

import androidx.lifecycle.LiveData
import com.abdoul.booksearch.bookDataSource.ApiDataSource
import com.abdoul.booksearch.model.Book

class BookRepository(private val apiDataSource: ApiDataSource) {

    fun getBooks(title: String): LiveData<Book> {
        getBookData(title)
        return apiDataSource.downloadedBook
    }

    private fun getBookData(title: String) {
        apiDataSource.getBookData(title)
    }
}