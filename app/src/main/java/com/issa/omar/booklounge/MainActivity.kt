package com.issa.omar.booklounge

import android.os.Bundle
import android.util.SparseArray
import androidx.annotation.IdRes
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.issa.omar.booklounge.dummy.DummyContent

class MainActivity : AppCompatActivity(), BookListFragment.OnBookSelectedListener {

    private var savedStateSparseArray = SparseArray<Fragment.SavedState>()
    private var currentSelectItemId = R.id.navigation_search
    companion object {
        const val SAVED_STATE_CONTAINER_KEY = "ContainerKey"
        const val SAVED_STATE_CURRENT_TAB_KEY = "CurrentTabKey"
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        /*when (item.itemId) {
            R.id.navigation_search -> {
                textMessage.setText(R.string.title_search)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_wishlist -> {
                textMessage.setText(R.string.title_wishlist)
                return@OnNavigationItemSelectedListener true
            }
        }
        false*/
        swapFragments(item.itemId);
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
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSparseParcelableArray(SAVED_STATE_CONTAINER_KEY, savedStateSparseArray)
        outState?.putInt(SAVED_STATE_CURRENT_TAB_KEY, currentSelectItemId)
    }

    override fun onAttachFragment(fragment: Fragment?) {
        if (fragment is BookListFragment) {
            fragment.setOnBookSelectedListener(this);
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
                BookListFragment.newInstance()
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

    override fun onBookSelected(item: DummyContent.DummyItem?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}