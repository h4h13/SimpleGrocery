package com.sharebuttons.grocery.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.sharebuttons.grocery.R;
import com.sharebuttons.grocery.fragment.SettingsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StartUpHelp extends AppCompatActivity {

    @Bind(R.id.pager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up_help);

        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();

        if (bundle.getString("from").equals("about")) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment().instance(R.drawable.help, "about")).commit();
        } else {
            mViewPager.setAdapter(new HelpAdapter(getSupportFragmentManager()));
        }


    }

    class HelpAdapter extends FragmentStatePagerAdapter {
        public HelpAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new SettingsFragment().instance(R.drawable.ic_add, "To add Grocery items");
                    break;
                case 1:
                    fragment = new SettingsFragment().instance(R.drawable.ic_add_1, "Enter details in require fields!");
                    break;
                case 2:
                    fragment = new SettingsFragment().instance(R.drawable.ic_delete, "Edit items, Click \"Edit items\" from navigation menu");
                    break;
                case 3:
                    fragment = new SettingsFragment().instance(R.drawable.web_hi_res_512, "Next");
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

}
