package com.issa.omar.booklounge.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Book (
    val title: String,
    @SerializedName("author_name")
    val authorName: List<String>?,
    @SerializedName("cover_edition_key")
    val smallImageId: String,
    @SerializedName("cover_i")
    val largeImageId: String
): Parcelable {
    companion object {
        const val BASE_IMAGE_URL = "https://covers.openlibrary.org/"
        const val SMALL_IMAGE_PREFIX = "b/olid/"
        const val LARGE_IMAGE_PREFIX = "w/id/"
        const val IMAGE_SUFFIX = "-M.jpg"
    }

    fun getSmallImageUrl(): String = BASE_IMAGE_URL + SMALL_IMAGE_PREFIX + smallImageId + IMAGE_SUFFIX

    fun getLargeImageUrl(): String = BASE_IMAGE_URL + LARGE_IMAGE_PREFIX + largeImageId + IMAGE_SUFFIX
}