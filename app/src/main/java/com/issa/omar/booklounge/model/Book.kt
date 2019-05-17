package com.issa.omar.booklounge.model

import com.google.gson.annotations.SerializedName

data class Book (
    val title: String,
    @SerializedName("author_name")
    val authorName: String,
    @SerializedName("cover_edition_key")
    val smallImageId: String,
    @SerializedName("cover_i")
    val largeImageId: String
)