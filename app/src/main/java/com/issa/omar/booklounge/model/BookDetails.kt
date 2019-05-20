package com.issa.omar.booklounge.model

import com.google.gson.annotations.SerializedName

data class BookDetails (
    val subjects: List<String>,
    val publishers: List<String>,
    val description: String,
    @SerializedName("publish_date")
    val publishDate: String
)