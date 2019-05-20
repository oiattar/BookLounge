package com.issa.omar.booklounge.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Book (
    var key: String,
    var title: String,
    @SerializedName("author_name")
    var authorName: List<String>?,
    @SerializedName("cover_edition_key")
    var smallImageId: String?,
    @SerializedName("cover_i")
    var largeImageId: String?
): Parcelable {

    companion object {
        const val BASE_IMAGE_URL = "https://covers.openlibrary.org/"
        const val SMALL_IMAGE_PREFIX = "b/olid/"
        const val LARGE_IMAGE_PREFIX = "w/id/"
        const val IMAGE_SUFFIX = "-M.jpg"
        const val EMPTY_IMAGE_URL_SMALL = "https://openlibrary.org/images/icons/avatar_book-sm.png"
        const val EMPTY_IMAGE_URL_LARGE = "https://openlibrary.org/images/icons/avatar_book-lg.png"
        const val BOOK_DETAILS_PREFIX = "OLID:"
    }

    fun getSmallImageUrl(): String = if (!smallImageId.isNullOrBlank()) (BASE_IMAGE_URL + SMALL_IMAGE_PREFIX + smallImageId + IMAGE_SUFFIX) else EMPTY_IMAGE_URL_SMALL

    fun getLargeImageUrl(): String = if (!largeImageId.isNullOrBlank()) (BASE_IMAGE_URL + LARGE_IMAGE_PREFIX + largeImageId + IMAGE_SUFFIX) else EMPTY_IMAGE_URL_LARGE

    fun getDetailsKey(): String = if (!smallImageId.isNullOrBlank()) (BOOK_DETAILS_PREFIX + smallImageId) else ""
}