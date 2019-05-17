package com.issa.omar.booklounge.rest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class API {
    companion object {
        const val BASE_URL: String = "https://openlibrary.org/"

        fun books(): BookApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BookApiService::class.java)
        }
    }
}