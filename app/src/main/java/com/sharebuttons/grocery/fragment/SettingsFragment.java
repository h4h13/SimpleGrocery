package com.sharebuttons.grocery.fragment;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharebuttons.grocery.settings.GrocerySettings;
import com.sharebuttons.grocery.R;
import com.sharebuttons.grocery.ui.DetailsActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private Intent mainIntent;
    GrocerySettings mSettings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getString("textSaying").equals("Next")) {
                view = inflater.inflate(R.layout.name_show, container, false);
                final EditText editText = (EditText) view.findViewById(R.id.enterName);
                Button button = (Button) view.findViewById(R.id.next);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mSettings = new GrocerySettings(getActivity());
                        mSettings.setUserName(editText.getText().toString().trim());

                        mainIntent = new Intent(getActivity(), DetailsActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                    }
                });
            } else if (bundle.get("textSaying").equals("about")) {
                view = inflater.inflate(R.layout.about, container, false);
                TextView textView = (TextView) view.findViewById(R.id.version);
                try {
                    PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                    textView.setText("Version " + packageInfo.versionName);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                view = inflater.inflate(R.layout.help, container, false);
                ImageView imageView = (ImageView) view.findViewById(R.id.helpImegeView);
                imageView.setImageResource(bundle.getInt("imageView"));

                TextView textView = (TextView) view.findViewById(R.id.gotoName);
                textView.setText(bundle.getString("textSaying"));
            }
        }

        return view;
    }

    public SettingsFragment() {
        // Required empty public constructor
    }

    public Fragment instance(int help, String saying) {
        SettingsFragment settingsFragment = new SettingsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("imageView", help);
        bundle.putString("textSaying", saying);
        settingsFragment.setArguments(bundle);
        return settingsFragment;
    }
}
