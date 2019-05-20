package com.issa.omar.booklounge

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import androidx.annotation.IdRes
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.issa.omar.booklounge.model.Book
import io.realm.Realm

class MainActivity : AppCompatActivity(), OnBookSelectedListener {

    private var savedStateSparseArray = SparseArray<Fragment.SavedState>()
    private var currentSelectItemId = R.id.navigation_search
    companion object {
        const val SAVED_STATE_CONTAINER_KEY = "ContainerKey"
        const val SAVED_STATE_CURRENT_TAB_KEY = "CurrentTabKey"
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        swapFragments(item.itemId)
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            val fragment = SearchFragment()
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, fragment.javaClass.simpleName)
                .commit()
        }
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        Realm.init(this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSparseParcelableArray(SAVED_STATE_CONTAINER_KEY, savedStateSparseArray)
        outState?.putInt(SAVED_STATE_CURRENT_TAB_KEY, currentSelectItemId)
    }

    override fun onAttachFragment(fragment: Fragment?) {
        if (fragment is SearchFragment) {
            fragment.setOnBookSelectedListener(this)
        } else if (fragment is WishlistFragment) {
            fragment.setOnBookSelectedListener(this)
        }
    }

    private fun swapFragments(@IdRes itemId: Int) {
        if (supportFragmentManager.findFragmentByTag(itemId.toString()) == null) {
            savedFragmentState(itemId)
            createFragment(itemId)
        }
    }

    private fun createFragment(itemId: Int) {
        val tag = itemId.toString()
        val fragment = supportFragmentManager.findFragmentByTag(tag) ?: when (itemId) {
            R.id.navigation_wishlist -> {
                WishlistFragment.newInstance()
            }
            else -> {
                SearchFragment.newInstance()
            }
        }
        fragment.setInitialSavedState(savedStateSparseArray[itemId])
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
    }

    private fun savedFragmentState(itemId: Int) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment != null) {
            savedStateSparseArray.put(currentSelectItemId,
                supportFragmentManager.saveFragmentInstanceState(currentFragment)
            )
        }
        currentSelectItemId = itemId
    }

    override fun onBookSelected(book: Book?) {
        val intent = Intent(this, BookDetailActivity::class.java)
        intent.putExtra("SELECTED_BOOK", book)
        startActivity(intent)
    }
}
