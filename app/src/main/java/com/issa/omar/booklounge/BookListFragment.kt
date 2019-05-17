package com.issa.omar.booklounge

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.issa.omar.booklounge.dummy.DummyContent
import com.issa.omar.booklounge.dummy.DummyContent.DummyItem

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [BookListFragment.OnBookSelectedListener] interface.
 */
class BookListFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1

    private var listener: OnBookSelectedListener? = null

    companion object {
        fun newInstance() = BookListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyBookRecyclerViewAdapter(DummyContent.ITEMS, listener)
            }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnBookSelectedListener {
        // TODO: Update argument type and name
        fun onBookSelected(item: DummyItem?)
    }

    fun setOnBookSelectedListener(listener: OnBookSelectedListener) {
        this.listener = listener
    }

}
