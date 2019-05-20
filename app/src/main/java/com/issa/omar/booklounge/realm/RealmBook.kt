package com.issa.omar.booklounge.realm

import com.issa.omar.booklounge.model.Book.Companion.BASE_IMAGE_URL
import com.issa.omar.booklounge.model.Book.Companion.EMPTY_IMAGE_URL_SMALL
import com.issa.omar.booklounge.model.Book.Companion.IMAGE_SUFFIX
import com.issa.omar.booklounge.model.Book.Companion.SMALL_IMAGE_PREFIX
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmBook (
    @PrimaryKey
    var key: String,
    var title: String,
    var authorName: RealmList<String>,
    var smallImageId: String,
    var largeImageId: String
): RealmObject() {
    constructor(): this("", "", RealmList(), "", "")

    fun getSmallImageUrl(): String = if (!smallImageId.isBlank()) (BASE_IMAGE_URL + SMALL_IMAGE_PREFIX + smallImageId + IMAGE_SUFFIX) else EMPTY_IMAGE_URL_SMALL
}