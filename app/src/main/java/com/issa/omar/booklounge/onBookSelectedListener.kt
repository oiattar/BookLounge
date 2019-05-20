package com.issa.omar.booklounge

import com.issa.omar.booklounge.model.Book

interface OnBookSelectedListener {
    fun onBookSelected(book: Book?)
}