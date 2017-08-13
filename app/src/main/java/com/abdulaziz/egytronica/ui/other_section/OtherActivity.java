package com.abdulaziz.egytronica.ui.other_section;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.abdulaziz.egytronica.R;
import com.abdulaziz.egytronica.ui.other_section.events_fragment.EventsListFragment;
import com.abdulaziz.egytronica.ui.other_section.news_fragment.NewsListFragment;
import com.abdulaziz.egytronica.utils.GlobalEntities;
import com.abdulaziz.egytronica.utils.adapters.PagerAdapter;

public class OtherActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NewsListFragment.OnNewsListFragmentInteractionListener, EventsListFragment.OnEventsListFragmentInteractionListener{


    private View mainView;
    private BottomNavigationView bottomNav;
    private ViewPager viewPager;
    private PagerAdapter fragmentAdapter;

    public static Intent getStartIntent(Context context){
        Intent i = new Intent(context, OtherActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        init();

    }

    private void init(){
        //main view
        mainView = findViewById(R.id.main_view);

        //pager
        viewPager = (ViewPager) findViewById(R.id.other_view_pager);

        fragmentAdapter = new PagerAdapter(getSupportFragmentManager());

        fragmentAdapter.addFragment(NewsListFragment.newInstance());
        fragmentAdapter.addFragment(EventsListFragment.newInstance());

        viewPager.setAdapter(fragmentAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNav.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //bottom nav
        bottomNav = (BottomNavigationView)
                findViewById(R.id.other_bottom_navigation);

        bottomNav.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottom_nav_menu_news:
                Log.i(GlobalEntities.OTHER_ACTIVITY, "onNavigationItemSelected: news");
                viewPager.setCurrentItem(0);
                break;
            case R.id.bottom_nav_menu_events:
                Log.i(GlobalEntities.OTHER_ACTIVITY, "onNavigationItemSelected: events");
                viewPager.setCurrentItem(1);
                break;
        }
        return true;
    }

    @Override
    public void onEventsListFragmentInteraction(String action, String message) {

    }

    @Override
    public void onNewsListFragmentInteraction(String action, String message) {

    }
}
