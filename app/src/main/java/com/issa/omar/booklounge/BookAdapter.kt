package com.issa.omar.booklounge

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


import com.issa.omar.booklounge.SearchFragment.OnBookSelectedListener
import com.issa.omar.booklounge.model.Book
import com.squareup.picasso.Picasso

/**
 * [RecyclerView.Adapter] that can display a [Book] and makes a call to the
 * specified [OnBookSelectedListener].
 * TODO: Replace the implementation with code for your data type.
 */
class BookAdapter(
    private val books: List<Book>,
    private val listener: OnBookSelectedListener?
) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val book = v.tag as Book
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            listener?.onBookSelected(book)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = books[position]
        Picasso.get().load(book.getSmallImageUrl()).into(holder.image)
        holder.title.text = book.title
        holder.author.text = book.authorName?.joinToString { it }

        with(holder.view) {
            tag = book
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount(): Int = books.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.book_image)
        val title: TextView = view.findViewById(R.id.book_title)
        val author: TextView = view.findViewById(R.id.book_author)

        override fun toString(): String {
            return super.toString() + " '" + title.text + "'"
        }
    }
}
