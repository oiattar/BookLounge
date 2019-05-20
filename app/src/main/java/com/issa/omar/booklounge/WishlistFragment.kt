package com.issa.omar.booklounge

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.issa.omar.booklounge.realm.RealmBook
import io.realm.Realm

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [OnBookSelectedListener] interface.
 */
class WishlistFragment : Fragment() {

    private var listener: OnBookSelectedListener? = null

    companion object {
        fun newInstance() = WishlistFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wish_list, container, false)
        val realm = Realm.getDefaultInstance()
        val results = realm.where(RealmBook::class.java).findAll()

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = WishlistAdapter(results, listener)
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

    fun setOnBookSelectedListener(listener: OnBookSelectedListener) {
        this.listener = listener
    }

}
