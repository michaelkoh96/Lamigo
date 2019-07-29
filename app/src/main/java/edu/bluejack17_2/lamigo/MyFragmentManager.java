package edu.bluejack17_2.lamigo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentManager extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> fragmentTitleList = new ArrayList<>();

    public MyFragmentManager(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment f, String title){
        fragmentList.add(f);
        fragmentTitleList.add(title);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    public String getTitle(int position){
        return fragmentTitleList.get(position);
    }
}
