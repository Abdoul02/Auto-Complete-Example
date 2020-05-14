package com.abdoul.booksearch.data

object ApiUtils {

    private const val BASE_URL = "https://www.googleapis.com/"

    fun getBookApiService(): BookApiService {
        return RetrofitClient.getClient(BASE_URL)!!.create(BookApiService::class.java)
    }
}