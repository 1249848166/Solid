package com.su.example.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.su.example.R;
import com.su.example.adapter.FragmentPagerAdapter;
import com.su.example.fragment.FindFragment;
import com.su.example.fragment.MainFragment;
import com.su.example.fragment.MeFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentPagerActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager pager;

    private FragmentPagerAdapter adapter;
    private final List<Fragment> fragments=new ArrayList<>();

    private final String[] tabStrings=new String[]{"首页","发现","我的"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_pager);

        try {

            tabLayout = findViewById(R.id.tabLayout);
            pager = findViewById(R.id.pager);

            for (String tabStr : tabStrings) {
                final TabLayout.Tab tab = tabLayout.newTab().setText(tabStr);
                tabLayout.addTab(tab);
            }

            fragments.add(new MainFragment());
            fragments.add(new FindFragment());
            fragments.add(new MeFragment());
            adapter = new FragmentPagerAdapter(getSupportFragmentManager(), fragments);
            pager.setAdapter(adapter);
            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            tabLayout.setupWithViewPager(pager);
            final int size = tabStrings.length;
            for (int i = 0; i < size; i++) {
                Objects.requireNonNull(tabLayout.getTabAt(i)).setText(tabStrings[i]);
            }

        }catch (Throwable e){
            e.printStackTrace();
        }
    }
}