package com.sharebuttons.grocery.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sharebuttons.grocery.R;
import com.sharebuttons.grocery.adapter.DetailsAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentDetailsActivity extends AppCompatActivity {
    @Bind(R.id.pager)
    ViewPager mViewPager;

    DetailsAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        mAdapter = new DetailsAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
    }

    public void addCategory(View view) {
        //Assume thee are three tables
        mAdapter.addFragmentCat("Food");
        mAdapter.addFragmentCat("Vegg");
        mAdapter.addFragmentCat("Liquids");
    }

}
