package com.issa.omar.booklounge

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.issa.omar.booklounge.realm.RealmBook
import io.realm.Realm

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [OnBookSelectedListener] interface.
 */
class WishlistFragment : Fragment() {

    private var listener: OnBookSelectedListener? = null
    private lateinit var emptyListMessage: TextView
    private lateinit var wishlist: RecyclerView

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
        Log.d("MOONZ", results.toString())

        emptyListMessage = view.findViewById(R.id.empty_list_message)
        wishlist = view.findViewById(R.id.wishlist)

        if(results.isNullOrEmpty()) {
            showEmptyListMessage()
        } else {
            showWishlist()
            wishlist.layoutManager = LinearLayoutManager(context)
            wishlist.adapter = WishlistAdapter(results, listener)
            wishlist.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
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

    override fun onResume() {
        super.onResume()
        if(wishlist.adapter?.itemCount == 0)
            showEmptyListMessage()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun setOnBookSelectedListener(listener: OnBookSelectedListener) {
        this.listener = listener
    }

    private fun showEmptyListMessage() {
        emptyListMessage.visibility = View.VISIBLE
        wishlist.visibility = View.GONE
    }

    private fun showWishlist() {
        emptyListMessage.visibility = View.GONE
        wishlist.visibility = View.VISIBLE
    }
}
