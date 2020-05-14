package com.abdoul.booksearch.data

import com.abdoul.booksearch.model.Book
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApiService {

    @GET("books/v1/volumes")
    fun getBookData(
        @Query("q") title: String,
        @Query("maxResults") maxResults: Int
    ): Observable<Book>
}