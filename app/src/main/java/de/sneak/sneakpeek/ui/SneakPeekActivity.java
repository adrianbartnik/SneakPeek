package de.sneak.sneakpeek.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;

import de.sneak.sneakpeek.R;
import de.sneak.sneakpeek.ui.fragment.AboutDialogFragment;
import de.sneak.sneakpeek.ui.fragment.MovieFragment;
import de.sneak.sneakpeek.ui.fragment.PreviousMoviesFragment;
import de.sneak.sneakpeek.ui.fragment.StudiosFragment;
import de.sneak.sneakpeek.util.Util;
import io.fabric.sdk.android.Fabric;

public class SneakPeekActivity extends AppCompatActivity {

    private static final String TAG = SneakPeekActivity.class.getSimpleName();

    SneekPeekPagerAdapter mPagerAdapter;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(Util.GetUseLightTheme(getApplication()) ? R.style.SneakPeekLight : R.style.SneakPeekDark);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fabric.with(this, new Crashlytics());

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_main_tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mPagerAdapter = new SneekPeekPagerAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.activity_main_viewpager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(1);

        tabLayout.setupWithViewPager(mPager);

        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_switch_theme:
                Util.SetUseDarkTheme(getApplication(), !Util.GetUseLightTheme(getApplication()));
                recreate();
                return true;
            case R.id.main_menu_about:
                new AboutDialogFragment().show(getSupportFragmentManager(), AboutDialogFragment.TAG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class SneekPeekPagerAdapter extends FragmentPagerAdapter {
        public SneekPeekPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return MovieFragment.newInstance();
                case 1:
                    return StudiosFragment.newInstance();
                case 2:
                    return PreviousMoviesFragment.newInstance();
                default:
                    throw new IllegalArgumentException();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Movies";
                case 1:
                    return "Studios";
                case 2:
                    return "Previous Movies";
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}
