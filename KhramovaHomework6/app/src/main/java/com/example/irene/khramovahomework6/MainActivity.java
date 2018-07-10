package com.example.irene.khramovahomework6;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements ItemThreeFragment.OnViewPagerFragmentListener, PageFragment.OnImageClick {

    public static final String TAG_VIEW_PAGER = "ViewPagerFragment";
    @BindView(R.id.drawerLayout) DrawerLayout mDrawerLayout;
    @BindView(R.id.navigationView) NavigationView mNavigationView;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.bottomNavigationView) BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.nav_first:
                                Toast.makeText(MainActivity.this, R.string.nav_first, Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_second:
                                Toast.makeText(MainActivity.this, R.string.nav_second, Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_third:
                                Toast.makeText(MainActivity.this, R.string.nav_third, Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_item_one:
                        changeFragment(ItemOneFragment.newInstance());
                        getSupportActionBar().setTitle(R.string.title_item_one);
                        return true;
                    case R.id.bottom_item_two:
                        changeFragment(ItemTwoFragment.newInstance());
                        getSupportActionBar().setTitle(R.string.title_item_two);
                        return true;
                    case R.id.bottom_item_three:
                        changeFragment(ItemThreeFragment.newInstance());
                        getSupportActionBar().setTitle(R.string.title_item_three);
                        return true;
                }
                return false;
            }
        });
        mBottomNavigationView.setSelectedItemId(R.id.bottom_item_one);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                Toast.makeText(MainActivity.this, R.string.menu_search, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_one:
                Toast.makeText(MainActivity.this, R.string.menu_one, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_two:
                Toast.makeText(MainActivity.this, R.string.menu_two, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_three:
                Toast.makeText(MainActivity.this, R.string.menu_three, Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutConteiner, fragment).commit();
    }

    @Override
    public void showPagerFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frameLayoutForViewPager, ViewPagerFragment.newInstance(), TAG_VIEW_PAGER)
                .commit();
    }

    @Override
    public void hidePagerFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(TAG_VIEW_PAGER)).commit();
    }

    @Override
    public void onClick(String title) {
        Toast.makeText(MainActivity.this, title, Toast.LENGTH_SHORT).show();
    }
}
