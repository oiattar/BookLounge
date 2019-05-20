package com.issa.omar.booklounge

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.issa.omar.booklounge.model.Book
import com.issa.omar.booklounge.model.BookDetails
import com.issa.omar.booklounge.model.BookDetailsResponse
import com.issa.omar.booklounge.realm.RealmBook
import com.issa.omar.booklounge.rest.BookApiService
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_book_detail.*
import kotlinx.android.synthetic.main.content_book_detail.*

class BookDetailActivity : AppCompatActivity() {

    private lateinit var book: Book
    private lateinit var realm: Realm
    private var isWishlist: Boolean = false
    var disposable: Disposable? = null
    private val bookApiService by lazy {
        BookApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        realm = Realm.getDefaultInstance()
        book = intent.getParcelableExtra("SELECTED_BOOK")
        if(!book.smallImageId.isNullOrBlank()) getBookDetails()
        setBookDetails()
        isWishlist = checkIfWishlist()
        setWishlistIcon(isWishlist)
        wishlist_button.setOnClickListener { view ->
            onWishlistClicked()
            val snackMessage = if(isWishlist) R.string.add_wishlist_message else R.string.remove_wishlist_message
            Snackbar.make(view, snackMessage, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show()
        }
    }

    private fun setBookDetails() {
        title = book.title
        Picasso.get().load(book.getLargeImageUrl()).into(book_image)
        book_title.text = book.title
        book_author.text = book.authorName?.joinToString { it }
    }

    private fun getBookDetails() {
        disposable =
            bookApiService.getDetails(book.getDetailsKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> showResult(result) },
                    { error -> showError(error.message) }
                )
    }

    private fun showResult(details: BookDetailsResponse) {
        Log.d("MOONZ", "successdd: $details")
        //book_description.text = details.description
    }

    private fun showError(error: String?) {
        Log.d("MOONZ", "errordd: $error")
    }

    private fun onWishlistClicked() {
        when(isWishlist) {
            true -> removeFromWishlist()
            false -> addToWishlist()
        }
    }

    private fun checkIfWishlist(): Boolean {
        val results = realm.where(RealmBook::class.java).equalTo("key", book.key).findFirst()
        return results != null
    }

    private fun addToWishlist() {
        realm.beginTransaction()
        val bookRealm = realm.createObject(RealmBook::class.java, book.key)
        bookRealm.title = book.title
        for (name in book.authorName.orEmpty()) bookRealm.authorName.add(name)
        bookRealm.smallImageId = if (book.smallImageId.isNullOrBlank()) "" else book.smallImageId!!
        bookRealm.largeImageId = if (book.largeImageId.isNullOrBlank()) "" else book.largeImageId!!
        realm.commitTransaction()
        setWishlistIcon(true)
        isWishlist = true
    }

    private fun removeFromWishlist() {
        realm.executeTransaction { realm.where(RealmBook::class.java).equalTo("key", book.key).findFirst()?.deleteFromRealm()}
        setWishlistIcon(false)
        isWishlist = false
    }

    private fun setWishlistIcon(clicked: Boolean) {
        when(clicked) {
            true -> wishlist_button.setImageResource(android.R.drawable.btn_star_big_on)
            false -> wishlist_button.setImageResource(android.R.drawable.btn_star_big_off)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
