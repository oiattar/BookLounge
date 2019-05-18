package com.issa.omar.booklounge.rest

import com.issa.omar.booklounge.model.BookResponse
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApiService {

    companion object {
        private const val BASE_URL: String = "https://openlibrary.org/"

        fun create(): BookApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(BookApiService::class.java)
        }
    }

    @GET("search.json")
    fun search(@Query("q") query: String): Observable<BookResponse>
}