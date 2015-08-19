package com.sharebuttons.grocery.fragment;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharebuttons.grocery.R;
import com.sharebuttons.grocery.adapter.GroceryAdapter;
import com.sharebuttons.grocery.db.GroceryDataSource;
import com.sharebuttons.grocery.db.GroceryHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    private static final String FRAGMENT_NAME = "FRAGMENT_NAME";
    private static final String TABLE_NAME = "TABLE_NAME";

    @Bind(R.id.detailList)
    RecyclerView mRecyclerView;
    @Bind(R.id.catItem)
    TextView mTextView;
    @Bind(R.id.addItem)
    ImageView mAddItem;

    private GroceryDataSource mDataSource;
    private GroceryAdapter mAdapter;
    Context mContext;
    Bundle bundle;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mDataSource = new GroceryDataSource(mContext);
        mAdapter = new GroceryAdapter(mContext);
        bundle = getArguments();
    }

    @Override
    public void onResume() {
        mDataSource.open();

        mTextView.setText(bundle.getString(FRAGMENT_NAME));
        Cursor cursor = mDataSource.read();
        updateListView(cursor);

        super.onResume();
    }

    @Override
    public void onPause() {
        mDataSource.close();
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);
        _RecyclerViewInit(view);

        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataSource.insertDataAccordingToCat(TABLE_NAME, "Item 1", "3 kg", "#ffee44");
            }
        });

        return view;
    }

    private void _RecyclerViewInit(final View view) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper.SimpleCallback mSimpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                mAdapter.sendItemFromPosition(position);
                final String removedItem = mAdapter.sendItemFromPosition(position);
                final String removedRate = mAdapter.sendRateFormPosition(position);
                Snackbar.make(view, removedItem + " removed", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               // mDataSource.insert(removedItem, removedRate);
                               // mAdapter.addItem(removedItem, position, removedRate);
                            }
                        }).show();
                mAdapter.removeItem(position);

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mSimpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }


    public void updateListView(Cursor cursor) {
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int index = cursor.getColumnIndex(GroceryHelper.COLUMN_GROCERY);
            int index1 = cursor.getColumnIndex(GroceryHelper.COLUMN_RATE);
          //  mAdapter.addItem(cursor.getString(index), 0, cursor.getString(index1));
            cursor.moveToNext();
        }

    }

    public static DetailFragment newInstance(String item) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_NAME, item);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }
}
