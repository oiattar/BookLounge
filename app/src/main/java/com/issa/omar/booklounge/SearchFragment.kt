package com.issa.omar.booklounge

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
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

    private lateinit var viewModel: SearchViewModel


    private var listener: OnBookSelectedListener? = null

    private lateinit var resultsList: RecyclerView
    private lateinit var searchBar: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchBar = view.findViewById(R.id.search_bar)
        resultsList = view.findViewById(R.id.results_list)

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
        .debounce(250, TimeUnit.MILLISECONDS)
        .distinct()
        .filter { text -> text.isNotBlank() }
        .subscribe {text ->
            beginSearch(text)
            Log.d("MOONZ", "sub: $text")
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        // TODO: Use the ViewModel
    }

    /*override fun onAttach(context: Context) {
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
    }*/

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
        Log.d("MOONZ", "success: $books")
        resultsList.visibility = View.VISIBLE
        resultsList.adapter = BookAdapter(books)
    }

    private fun showError(error: String?) {
        Log.d("MOONZ", "error: $error")
    }

    interface OnBookSelectedListener {
        // TODO: Update argument type and name
        fun onBookSelected(item: Book?)
    }

    fun setOnBookSelectedListener(listener: OnBookSelectedListener) {
        this.listener = listener
    }

}
