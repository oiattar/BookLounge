package com.issa.omar.booklounge

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.issa.omar.booklounge.model.Book
import com.issa.omar.booklounge.rest.BookApiService
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private val bookApiService by lazy {
        BookApiService.create()
    }
    var disposable: Disposable? = null

    val TAG: String = SearchFragment::class.java.simpleName
    private var listener: OnBookSelectedListener? = null

    private lateinit var resultsList: RecyclerView
    private lateinit var searchBar: SearchView
    private lateinit var errorMessage: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchBar = view.findViewById(R.id.search_bar)
        resultsList = view.findViewById(R.id.results_list)
        errorMessage = view.findViewById(R.id.error_message)

        Observable.create(ObservableOnSubscribe<String> { subscriber ->
            searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    subscriber.onNext(newText!!)
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    subscriber.onNext(query!!)
                    return false
                }
            })
        })
        .map { text -> text.toLowerCase().trim() }
        .debounce(200, TimeUnit.MILLISECONDS)
        .distinct()
        .filter { text -> text.isNotBlank() }
        .subscribe {text ->
            beginSearch(text)
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnBookSelectedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnBookSelectedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    private fun beginSearch(query: String) {
        disposable =
            bookApiService.search(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> showResult(result.books) },
                    { error -> showError(error.message) }
                )
    }

    private fun showResult(books: List<Book>) {
        resultsList.visibility = View.VISIBLE
        errorMessage.visibility = View.GONE
        resultsList.adapter = BookAdapter(books, listener)
        resultsList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
    }

    private fun showError(error: String?) {
        Log.e(TAG, "error: $error")
        errorMessage.visibility = View.VISIBLE
        resultsList.visibility = View.INVISIBLE
    }

    fun setOnBookSelectedListener(listener: OnBookSelectedListener) {
        this.listener = listener
    }

}
