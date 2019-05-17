package com.issa.omar.booklounge.model

import com.google.gson.annotations.SerializedName

data class BookResponse(
    val numFound: Int,
    @SerializedName("docs")
    val books: List<Book>
)