package com.sharebuttons.grocery.adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.sharebuttons.grocery.R;
import com.sharebuttons.grocery.anim.AnimationHolder;
import com.sharebuttons.grocery.db.GroceryDataSource;
import com.sharebuttons.grocery.grocery.Grocery;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;

/**
 * Created by Monkey D Luffy on 7/15/2015.
 */
public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.ViewHolder> {
    private List<Grocery> mListData = new ArrayList<>();
    private GroceryDataSource mDataSource;
    private LayoutInflater mLayoutInflater;
    private int previousPosition = 0;
    private Context mContext;
    private Typeface mTypeface;

    private TextDrawable.IBuilder mTextDrawable;

    String[] colors = {"#F44336",
            "#E91E63", "#9C27B0", "#673AB7",
            "#3F51B5", "#2196F3", "#2196F3",
            "#00BCD4", "#009688", "#4CAF50",
            "#8BC34A", "#FF9800", "#FFC107",
            "#FFEB3B", "#8BC34A", "#FF5722",
            "#607D8B", "#9E9E9E", "#795548"};

    public GroceryAdapter(Context context) {
        mContext = context;
        mTextDrawable = TextDrawable.builder().round();
        mDataSource = new GroceryDataSource(context);
        mLayoutInflater = LayoutInflater.from(context);
        mTypeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Light.ttf");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.grocery_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Grocery data = mListData.get(position);
        String item = data.getGrocery();
        String rate = data.getRate();

        holder.mTextView.setText(item);
        holder.mRate.setText(rate);

        //set typeface
        holder.mRate.setTypeface(mTypeface);
        holder.mTextView.setTypeface(mTypeface);
        //holder.mIcon.setTypeface(mTypeface);


        char letter = item.charAt(0);
        TextDrawable drawable = mTextDrawable.build(String.valueOf(letter), Color.parseColor(data.getColor()));
        holder.mIcon.setImageDrawable(drawable);

        if (position > previousPosition) {
            AnimationHolder.animate(holder, true);
        } else {
            AnimationHolder.animate(holder, false);
        }
        previousPosition = position;
    }

    public String sendRateFormPosition(int position) {
        return mListData.get(position).getRate();
    }

    public String sendItemFromPosition(int position) {
        return mListData.get(position).getGrocery();
    }

    // Remove items from listview
    public void removeItem(int position) {
        try {
            removeFromDatabase(mListData.get(position).getGrocery());
            mListData.remove(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } finally {
            //notifyDataSetChanged();
            notifyItemRemoved(position);
            //notifyItemInserted(position);
        }

    }

    //removew items from database
    public void removeFromDatabase(String data) {
        mDataSource.open();
        mDataSource.delete(data);
        mDataSource.close();
        //notifyDataSetChanged();
    }

    // Add items to listview
    public void addItem(String item, int position, String total, String color) {
        Grocery grocery = new Grocery(item, total, color);
        try {
            mListData.add(position, grocery);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } finally {
            notifyItemInserted(position);
        }
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    static class ViewHolder extends AnimateViewHolder {
        TextView mTextView, mRate;
        ImageView mIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.textView);
            mRate = (TextView) itemView.findViewById(R.id.rate);
            mIcon = (ImageView) itemView.findViewById(R.id.icon);
        }

        @Override
        public void animateAddImpl(ViewPropertyAnimatorListener listener) {
            ViewCompat.animate(itemView)
                    .translationY(0)
                    .alpha(1)
                    .setDuration(300)
                    .setListener(listener)
                    .start();
        }

        @Override
        public void preAnimateAddImpl() {
            ViewCompat.setTranslationY(itemView, -itemView.getHeight() * 0.3f);
            ViewCompat.setAlpha(itemView, 0);
        }

        @Override
        public void animateRemoveImpl(ViewPropertyAnimatorListener listener) {
            ViewCompat.animate(itemView)
                    .translationY(0)
                    .alpha(1)
                    .setDuration(300)
                    .setListener(listener)
                    .start();
        }

    }
}
