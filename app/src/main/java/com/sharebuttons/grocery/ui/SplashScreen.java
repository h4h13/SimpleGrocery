package com.sharebuttons.grocery.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sharebuttons.grocery.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashScreen extends AppCompatActivity {
    TextView mTextView;
    Typeface mTypeface;
    SharedPreferences mSharedPreferences;
    private Intent mainIntent;
    @Bind(R.id.contentPanel)
    RelativeLayout mRelativeLayout;
    @Bind(R.id.imageView)
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setSharedElementExitTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_logo));
        }
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");

        mTextView = (TextView) findViewById(R.id.textView2);
        mTextView.setTypeface(mTypeface);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSharedPreferences.getBoolean("isLaunchedName", false)) {
                    mainIntent = new Intent(SplashScreen.this, DetailsActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                } else {

                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(SplashScreen.this, mImageView, "logoImage");

                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putBoolean("isLaunchedName", true);
                    editor.apply();

                    startActivity(new Intent(SplashScreen.this, StartUpHelp.class).putExtra("from", "help"), optionsCompat.toBundle());
                }
            }
        }, 1000);
    }

}
