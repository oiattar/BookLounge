package com.issa.omar.booklounge.rest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.issa.omar.booklounge.model.BookDetails
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

        private val gson: Gson =
            GsonBuilder()
                .registerTypeAdapter(BookDetails::class.java, BookDetailsDeserializer())
                .create()

        fun create(): BookApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(BookApiService::class.java)
        }
    }

    @GET("search.json")
    fun search(@Query("q") query: String): Observable<BookResponse>

    @GET("/api/books")
    fun getDetails(@Query("bibkeys") key: String,
                   @Query("jscmd") jscmd: String = "details",
                   @Query("format") format: String = "json") : Observable<BookDetails>
}