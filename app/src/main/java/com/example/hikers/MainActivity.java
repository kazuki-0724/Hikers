package com.example.hikers;

import android.os.Bundle;



import com.google.android.material.tabs.TabLayout;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


import com.example.hikers.ui.main.SectionsPagerAdapter;
import com.example.hikers.databinding.ActivityMainBinding;

/**
 * @author Kazuki0724
 */
public class MainActivity extends AppCompatActivity{


    private ActivityMainBinding binding;
    private Toolbar toolbar;
    private TabLayout tabs;
    private int[] tabIcons = {
            R.drawable.ic_baseline_directions_walk_24,
            R.drawable.ic_baseline_library_books_24,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);

        tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        toolbar = binding.toolbar;


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        getSupportActionBar().setTitle(null);

        tabs.setupWithViewPager(viewPager);

        setUpTabIcon();
    }

    /**
     * アイコン埋め込むメソッド
     */
    private void setUpTabIcon() {
        tabs.getTabAt(0).setIcon(tabIcons[0]);
        tabs.getTabAt(1).setIcon(tabIcons[1]);
    }


}