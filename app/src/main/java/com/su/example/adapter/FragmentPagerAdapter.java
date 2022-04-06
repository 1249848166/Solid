package com.su.example.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.su.example.fragment.MainFragment;

import java.util.List;

public class FragmentPagerAdapter extends androidx.fragment.app.FragmentPagerAdapter {

    private final List<Class<? extends Fragment>> fragments;

    public FragmentPagerAdapter(@NonNull FragmentManager fm, List<Class<? extends Fragment>> fragments) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments=fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        try {
            return fragments.get(position).newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return new MainFragment();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
