package com.issa.omar.booklounge.rest

import com.issa.omar.booklounge.model.BookResponse
import retrofit2.Call
import retrofit2.http.GET

interface BookApiService {

    @GET("search.json")
    fun search(): Call<BookResponse>
}