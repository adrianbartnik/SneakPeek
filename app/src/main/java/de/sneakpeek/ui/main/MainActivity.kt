package de.sneakpeek.ui.main

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.crashlytics.android.Crashlytics
import de.sneakpeek.R
import de.sneakpeek.ui.main.fragment.MovieFragment
import de.sneakpeek.ui.main.fragment.PreviousMoviesFragment
import de.sneakpeek.ui.main.fragment.StudiosFragment
import de.sneakpeek.util.Util
import io.fabric.sdk.android.Fabric

class MainActivity : AppCompatActivity() {

    private var mPagerAdapter: SneekPeekPagerAdapter? = null
    private var mPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(if (Util.GetUseLightTheme(application)) R.style.SneakPeekLight else R.style.SneakPeekDark)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Fabric.with(this, Crashlytics())

        val toolbar = findViewById(R.id.activity_main_toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val tabLayout = findViewById(R.id.activity_main_tab_layout) as TabLayout
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        mPagerAdapter = SneekPeekPagerAdapter(supportFragmentManager)

        mPager = findViewById(R.id.activity_main_viewpager) as ViewPager
        mPager?.adapter = mPagerAdapter
        mPager?.offscreenPageLimit = 1

        tabLayout.setupWithViewPager(mPager)

        mPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                mPager?.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_menu_switch_theme -> {
                Util.SetUseDarkTheme(application, !Util.GetUseLightTheme(application))
                recreate()
                return true
            }
            R.id.main_menu_about -> {
                AboutDialogFragment().show(supportFragmentManager, AboutDialogFragment.TAG)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private inner class SneekPeekPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getCount(): Int {
            return 3
        }

        override fun getItem(position: Int): Fragment {

            when (position) {
                0 -> return MovieFragment.newInstance()
                1 -> return StudiosFragment.newInstance()
                2 -> return PreviousMoviesFragment.newInstance()
                else -> throw IllegalArgumentException()
            }
        }

        override fun getPageTitle(position: Int): CharSequence {
            when (position) {
                0 -> return getString(R.string.fragment_name_movies)
                1 -> return getString(R.string.fragment_name_studios)
                2 -> return getString(R.string.fragment_name_previous_movies)
                else -> throw IllegalArgumentException()
            }
        }
    }
}
