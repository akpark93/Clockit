package com.example.andrewpark.clockit;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.example.andrewpark.clockit.Adapter.MainTabPagerAdapter;

public class MainActivity extends FragmentActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private ViewPager mViewPager;
    private MainTabPagerAdapter mAdapter;

    private Drawable[] app_icon_drawables;
    private Drawable[] app_icon_drawables_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app_icon_drawables = new Drawable[]{getResources().getDrawable(R.drawable.alarm_icon)
                ,getResources().getDrawable(R.drawable.clock_icon), getResources().getDrawable(R.drawable.timer_icon)
                ,getResources().getDrawable(R.drawable.stopwatch_icon)
        };
        app_icon_drawables_selected = new Drawable[]{getResources().getDrawable(R.drawable.alarm_icon_selected)
                ,getResources().getDrawable(R.drawable.clock_icon_selected), getResources().getDrawable(R.drawable.timer_icon_selected)
                ,getResources().getDrawable(R.drawable.stopwatch_icon_selected)
        };

        setContentView(R.layout.activity_main);
        mAdapter = new MainTabPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                mViewPager.setCurrentItem(tab.getPosition());
                setSelectedIcon(tab);
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                setUnselectedIcon(tab);
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }
        };

        for (int i=0; i<4; i++) {
            actionBar.addTab(actionBar.newTab().setIcon(app_icon_drawables[i]).setTabListener(tabListener));
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setSelectedIcon(ActionBar.Tab tab) {
        for (int i=0; i<4; i++) {
            Drawable icon = app_icon_drawables[i];
            if (tab.getIcon() == icon && i==0) {
                tab.setIcon(app_icon_drawables_selected[i]);
            } else if (tab.getIcon() == icon && i==1) {
                tab.setIcon(app_icon_drawables_selected[i]);
            } else if (tab.getIcon() == icon && i==2) {
                tab.setIcon(app_icon_drawables_selected[i]);
            } else if (tab.getIcon() == icon && i==3) {
                tab.setIcon(app_icon_drawables_selected[i]);
            }
        }
    }

    private void setUnselectedIcon(ActionBar.Tab tab) {
        for (int i=0; i<4; i++) {
            Drawable icon = app_icon_drawables_selected[i];
            if (tab.getIcon() == icon && i==0) {
                tab.setIcon(app_icon_drawables[i]);
            } else if (tab.getIcon() == icon && i==1) {
                tab.setIcon(app_icon_drawables[i]);
            } else if (tab.getIcon() == icon && i==2) {
                tab.setIcon(app_icon_drawables[i]);
            } else if (tab.getIcon() == icon && i==3) {
                tab.setIcon(app_icon_drawables[i]);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
