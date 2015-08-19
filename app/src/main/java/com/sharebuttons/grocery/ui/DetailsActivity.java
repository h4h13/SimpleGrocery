package com.sharebuttons.grocery.ui;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sharebuttons.grocery.AppRater;
import com.sharebuttons.grocery.LowerToUp;
import com.sharebuttons.grocery.R;
import com.sharebuttons.grocery.adapter.GroceryAdapter;
import com.sharebuttons.grocery.db.GroceryDataSource;
import com.sharebuttons.grocery.db.GroceryHelper;
import com.sharebuttons.grocery.receiver.AlertReceiver;
import com.sharebuttons.grocery.settings.GrocerySettings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailsActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener {
    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;
    public int year, month, day, hour, minute;
    private int mYear, mMonth, mDay, mHour, mMinute;

    public DetailsActivity() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
    }

    @Bind(R.id.main_drawer)
    NavigationView mNavigationView;
    @Bind(android.R.id.list)
    RecyclerView mListView;
    @Bind(R.id.grocery_label)
    TextView mTextView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.app_bar)
    Toolbar mToolbar;
    @Bind(R.id.user_name)
    TextView mUsername;

    private GrocerySettings mSettings;

    EditText itemName;
    EditText itemRate;
    Spinner metrics;

    private String met = " ";


    private GroceryAdapter mAdapter;
    protected GroceryDataSource mDataSource;
    private AlertDialog dialog;
    private final int REQ_CODE_SPEECH_INPUT = 0;

    private String[] colors = {"#F44336", "#E91E63", "#9C27B0", "#673AB7", "#3F51B5", "#2196F3", "#2196F3", "#00BCD4",
            "#009688", "#4CAF50", "#8BC34A", "#FF9800", "#FFC107", "#FFEB3B", "#8BC34A", "#FF5722", "#607D8B",
            "#9E9E9E", "#795548"};

    private String _color = colors[new Random().nextInt(colors.length)];
    private LowerToUp mLowerToUp = new LowerToUp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.enter));
        }
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        classesInit();
        setUpRecyclerView();
        initValues();
        setUpDrawer();
    }

    private void initValues() {
        AppRater.app_launched(getApplicationContext());
        mSettings = new GrocerySettings(getApplicationContext());
        mUsername.setText(mSettings.getUserName());

        mNavigationView.setNavigationItemSelectedListener(this);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        mTextView.setTypeface(typeface);
        setSupportActionBar(mToolbar);
    }


    private void setUpDrawer() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //mFab.setTranslationY(slideOffset * 300);
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }


    private void classesInit() {
        mDataSource = new GroceryDataSource(DetailsActivity.this);
        mAdapter = new GroceryAdapter(this);
    }

    private void setUpRecyclerView() {
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mListView.getItemAnimator().setAddDuration(100);
        mListView.getItemAnimator().setRemoveDuration(300);

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

                Snackbar.make(findViewById(R.id.contentPanel), removedItem, Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mAdapter.addItem(removedItem, position, removedRate, _color);
                                mDataSource.insert(removedItem, removedRate, _color);
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.md_red_500))
                        .show();
                mAdapter.removeItem(position);

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mSimpleCallback);
        itemTouchHelper.attachToRecyclerView(mListView);

    }


    @OnClick(R.id.fab)
    public void add() {
        addItems();
    }

    @OnClick(R.id.textSpeech)
    public void speech() {
        if (isNetworkAvailable()) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
            try {
                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please Connect to Internet", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    private void showDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View inflate = inflater.inflate(R.layout.custom_dialog, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Add grocery item");
        alert.setView(inflate);
        itemName = ButterKnife.findById(inflate, R.id.item);
        itemRate = ButterKnife.findById(inflate, R.id.rate);
        metrics = ButterKnife.findById(inflate, R.id.how_many);

        metrics.setOnItemSelectedListener(DetailsActivity.this);

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (itemName.getText().toString().trim().length() > 0) {
                            if (itemRate.getText().toString().trim().length() > 0) {
                                double rate = Double.parseDouble(itemRate.getText().toString().trim());

                                String total = rate + " " + met;
                                mAdapter.addItem(itemName.getText().toString().trim(), 0, total, _color);
                                mDataSource.insert(itemName.getText().toString(), total, _color);
                            } else {
                                Toast.makeText(getApplicationContext(), "Must enter units", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Enter Item name", Toast.LENGTH_LONG).show();
                        }
                    }
                }

        );
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                }

        );
        dialog = alert.create();
        dialog.show();
    }

    public void addItems() {
        showDialog();
    }


    @Override
    protected void onRestart() {

        mSettings.setIsLaunched(true);
        mSettings.setFromApp(true);

        super.onRestart();
    }

    @Override
    protected void onResume() {
        mDataSource.open();

        if (mSettings.getIsLaunched()) {
            //Toast.makeText(getApplicationContext(), "fromClose", Toast.LENGTH_LONG).show();
        } else {
            if (mSettings.getFromApp()) {
                //Toast.makeText(getApplication(), "fromApp", Toast.LENGTH_LONG).show();
            } else {
                Cursor cursor = mDataSource.read();
                updateListView(cursor);

                mSettings.setFromApp(true);
                mSettings.setIsLaunched(true);
            }
        }
        super.onResume();

    }

    @Override
    protected void onPause() {
        mDataSource.close();
        mSettings.setIsLaunched(true);
        mSettings.setFromApp(true);
        super.onPause();
    }

    @Override
    protected void onStop() {
        mSettings.setIsLaunched(false);
        mSettings.setFromApp(false);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mSettings.setIsLaunched(false);
        mSettings.setFromApp(false);
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        this.finish();
        super.onDestroy();
    }

    public void updateListView(Cursor cursor) {
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int item = cursor.getColumnIndex(GroceryHelper.COLUMN_GROCERY);
            int rate = cursor.getColumnIndex(GroceryHelper.COLUMN_RATE);
            String color = cursor.getString(cursor.getColumnIndex(GroceryHelper.COLUMN_COLOR));
            mAdapter.addItem(cursor.getString(item), 0, cursor.getString(rate), color);
            cursor.moveToNext();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        met = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        // the callback received when the user "sets" the Date in the DatePickerDialog
        public void onDateSet(DatePicker view, int yearSelected,
                              int monthOfYear, int dayOfMonth) {
            year = yearSelected;
            month = monthOfYear;
            day = dayOfMonth;
            // Set the Selected Date in Select date Button
            showDialog(TIME_DIALOG_ID);
            //Toast.makeText(getApplicationContext(), "Date selected : " + day + "-" + month + "-" + year, Toast.LENGTH_LONG).show();
        }
    };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        // the callback received when the user "sets" the TimePickerDialog in the dialog
        public void onTimeSet(TimePicker view, int hourOfDay, int min) {
            hour = hourOfDay;
            minute = min;
            // Set the Selected Date in Select date Button
            Calendar alertTime = setTimeForNotification();
            Intent alarmIntent = new Intent(DetailsActivity.this, AlertReceiver.class);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime.getTimeInMillis(),
                    PendingIntent.getBroadcast(DetailsActivity.this, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT));

            Toast.makeText(getApplicationContext(), "Reminder set!", Toast.LENGTH_LONG).show();

        }
    };

    private Calendar setTimeForNotification() {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        return c;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        String appPackageName = getPackageName();
        Intent intent;
        String appName = "\"Grocery List - ShareButtons\"";
        mDrawerLayout.closeDrawer(GravityCompat.START);
        switch (menuItem.getItemId()) {

            case R.id.actions_reminder:
                showDialog(DATE_DIALOG_ID);
                break;
            case R.id.action_share_content:
                Cursor cursor = mDataSource.read();
                String items = sendData(cursor);
                try {
                    intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Grocery Items\n");
                    intent.putExtra(Intent.EXTRA_TEXT, items);
                    startActivity(Intent.createChooser(intent, "Choose an app for share"));
                } catch (android.content.ActivityNotFoundException anfe) {
                    anfe.printStackTrace();
                }
                break;
            case R.id.action_share:
                try {
                    intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "ShareButtons\n");
                    intent.putExtra(Intent.EXTRA_TEXT, "Check out this " + appName + " https://play.google.com/store/apps/details?id=com.sharebuttons.grocery");
                    startActivity(Intent.createChooser(intent, "Choose an app for share"));

                } catch (android.content.ActivityNotFoundException anfe) {
                    anfe.printStackTrace();
                }
                break;
            case R.id.action_rate:

                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
                    startActivity(Intent.createChooser(intent, "Choose app for open"));
                }
                break;
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Settings will be added in future", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_help:
                startActivity(new Intent(DetailsActivity.this, StartUpHelp.class).putExtra("from", "help"));
                break;
            case R.id.action_about:
                startActivity(new Intent(DetailsActivity.this, StartUpHelp.class).putExtra("from", "about"));
                break;
            case R.id.actions_exit:
                //startActivity(new Intent(DetailsActivity.this, FragmentDetailsActivity.class));
                this.finish();
                break;
        }
        return false;
    }

    private String sendData(Cursor cursor) {
        String builder = "";
        int number = 1;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int index = cursor.getColumnIndex(GroceryHelper.COLUMN_GROCERY);
            int index1 = cursor.getColumnIndex(GroceryHelper.COLUMN_RATE);
            builder = builder + number++ + ". " + cursor.getString(index) + " - " + cursor.getString(index1) + "\n";
            cursor.moveToNext();
        }
        return builder;
    }


    //Add items from voice
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mAdapter.addItem(mLowerToUp.firstLetterUpperCase(result.get(0)), 0, "", _color);
                    mDataSource.insert(mLowerToUp.firstLetterUpperCase(result.get(0)), "", _color);
                    //Toast.makeText(getApplicationContext(), result.get(0), Toast.LENGTH_LONG).show();
                }
                break;
            }

        }
    }

}
