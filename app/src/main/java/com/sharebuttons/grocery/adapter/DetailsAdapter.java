package com.sharebuttons.grocery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sharebuttons.grocery.fragment.DetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Monkey D Luffy on 7/31/2015.
 */
public class DetailsAdapter extends FragmentStatePagerAdapter {
    List<String> mList = new ArrayList<>();

    public DetailsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return DetailFragment.newInstance(mList.get(position));
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    public void addFragmentCat(String item) {
        mList.add(item);
        notifyDataSetChanged();
    }
}
