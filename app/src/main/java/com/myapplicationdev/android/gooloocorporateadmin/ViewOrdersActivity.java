package com.myapplicationdev.android.gooloocorporateadmin;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewOrdersActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String email="";
    String companyname="";
    String restaurantname="";
    String restaurantId="";

    ViewPagerAdapter mDemoCollectionPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        int permissionCheck_Write= ContextCompat.checkSelfPermission(
                ViewOrdersActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck_Write != PermissionChecker.PERMISSION_GRANTED){
            Toast.makeText(ViewOrdersActivity.this, "Permission not granted.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(ViewOrdersActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            finish();
        }

        mDemoCollectionPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        email = getIntent().getStringExtra("email");
        companyname = getIntent().getStringExtra("companyname");
        restaurantname = getIntent().getStringExtra("restaurantname");
        restaurantId = getIntent().getStringExtra("restaurantId");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("GooLooCorporateAdmin");

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPager.setAdapter(mDemoCollectionPagerAdapter);

        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }


    public String getEmail() {
        Log.d("ViewOrdersActivity",""+email);
        return email;
    }
    public String getCompanyname(){
        Log.d("ViewOrdersActivity",""+companyname);
        return companyname;
    }
    public String getRestaurantId(){
        Log.d("ViewOrdersActivity",""+restaurantId);
        return restaurantId;
    }
    public String getRestaurantName(){
        Log.d("ViewOrdersActivity",""+restaurantname);
        return restaurantname;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ActiveFragment(), "Active");
        adapter.addFragment(new RecentFragment(), "Recent");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
