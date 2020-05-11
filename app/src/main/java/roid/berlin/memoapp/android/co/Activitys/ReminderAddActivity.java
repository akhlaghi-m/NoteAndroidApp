package roid.berlin.memoapp.android.co.Activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import roid.berlin.memoapp.android.co.Model.Reminder;
import roid.berlin.memoapp.android.co.R;
import roid.berlin.memoapp.android.co.ReminderUtils.AlarmReceiver;
import roid.berlin.memoapp.android.co.SQLDatabaseReminder.ReminderDatabase;

public class ReminderAddActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {


    // Values for orientation change
    private static final String KEY_TITLE = "title_key";
    private static final String KEY_TIME = "time_key";
    private static final String KEY_DATE = "date_key";
    private static final String KEY_REPEAT = "repeat_key";
    private static final String KEY_REPEAT_NO = "repeat_no_key";
    private static final String KEY_REPEAT_TYPE = "repeat_type_key";
    private static final String KEY_ACTIVE = "active_key";
    // Constant values in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;
    private Toolbar mToolbar;
    private EditText mTitleText;
    private TextView mDateText, mTimeText, mRepeatText, mRepeatNoText, mRepeatTypeText;
    private FloatingActionButton mFAB1;
    private FloatingActionButton mFAB2;
    private Calendar mCalender;
    private int mYear, mMonth, mHour, mMinute, mDay;
    private long mRepeatTime;
    private String mTitle;
    private String mTime;
    private String mDate;
    private String mRepeat;
    private String mRepeatNo;
    private String mRepeatType;
    private String mActive;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);


        // Initialize Views
        mToolbar = findViewById(R.id.toolbar);
        mTitleText = findViewById(R.id.reminder_title);
        mDateText = findViewById(R.id.set_date);
        mTimeText = findViewById(R.id.set_time);
        mRepeatText = findViewById(R.id.set_repeat);
        mRepeatNoText = findViewById(R.id.set_repeat_no);
        mRepeatTypeText = findViewById(R.id.set_repeat_type);
        mFAB1 = findViewById(R.id.starred1);
        mFAB2 = findViewById(R.id.starred2);

        // Setup Toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Initialize default values
        mActive = "true";
        mRepeat = "true";
        mRepeatNo = Integer.toString(1);
        mRepeatType = "ساعت";

        mCalender = Calendar.getInstance();
        mHour = mCalender.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalender.get(Calendar.MINUTE);
        mYear = mCalender.get(Calendar.YEAR);
        mMonth = mCalender.get(Calendar.MONTH) + 1;
        mDay = mCalender.get(Calendar.DATE);


        // Setup Reminder Title EditText
        mTitleText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                mTitle = s.toString().trim();
                mTitleText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

        //setUp textViews using Reminder Values
        mDateText.setText(mDate);
        mTimeText.setText(mTime);
        mRepeatNoText.setText(mRepeatNo);
        mRepeatTypeText.setText(mRepeatType);
        mRepeatText.setText(" هر" + mRepeatNo + "" + mRepeatType);


        //To save state on device rotation
        if (savedInstanceState != null)
        {
            String savedTitle = savedInstanceState.getString(KEY_TITLE);
            mTitleText.setText(savedTitle);
            mTitle = savedTitle;

            String savedTime = savedInstanceState.getString(KEY_TIME);
            mTimeText.setText(savedTime);
            mTime = savedTime;

            String savedDate = savedInstanceState.getString(KEY_DATE);
            mDateText.setText(savedDate);
            mDate = savedDate;

            String saveRepeat = savedInstanceState.getString(KEY_REPEAT);
            mRepeatText.setText(saveRepeat);
            mRepeat = saveRepeat;

            String savedRepeatNo = savedInstanceState.getString(KEY_REPEAT_NO);
            mRepeatNoText.setText(savedRepeatNo);
            mRepeatNo = savedRepeatNo;

            String savedRepeatType = savedInstanceState.getString(KEY_REPEAT_TYPE);
            mRepeatTypeText.setText(savedRepeatType);
            mRepeatType = savedRepeatType;

            mActive = savedInstanceState.getString(KEY_ACTIVE);
        }

//set up active buttons
        if (mActive.equals("false"))
        {
            mFAB1.setVisibility(View.VISIBLE);
            mFAB2.setVisibility(View.GONE);
        }
        else if (mActive.equals("true"))
        {
            mFAB1.setVisibility(View.GONE);
            mFAB2.setVisibility(View.VISIBLE);
        }

    }

    // To save state on device rotation
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_TITLE, mTitleText.getText());
        outState.putCharSequence(KEY_TIME, mTimeText.getText());
        outState.putCharSequence(KEY_DATE, mDateText.getText());
        outState.putCharSequence(KEY_REPEAT, mRepeatText.getText());
        outState.putCharSequence(KEY_REPEAT_NO, mRepeatNoText.getText());
        outState.putCharSequence(KEY_REPEAT_TYPE, mRepeatTypeText.getText());
        outState.putCharSequence(KEY_ACTIVE, mActive);
    }


    public void setDate(View view)
    {
//        PersianCalendar now = PersianCalendar.getInstance();
//        DatePickerDialog dpd = DatePickerDialog.newInstance(
//                (DatePickerDialog.OnDateSetListener) this,
//                now.get(Calendar.YEAR),
//                now.get(Calendar.MONTH),
//                now.get(Calendar.DAY_OF_MONTH)
//        );
//        dpd.show(getFragmentManager(), "Datepickerdialog");

        PersianCalendar persianCalendar = new PersianCalendar();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                ReminderAddActivity.this,
                persianCalendar.getPersianYear(),
                persianCalendar.getPersianMonth(),
                persianCalendar.getPersianDay()
        );
        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");


    }

    public void setTime(View view)
    {

//        Calendar now = Calendar.getInstance();
//        TimePickerDialog tpd = TimePickerDialog.newInstance(
//                (TimePickerDialog.OnTimeSetListener) this,
//                now.get(Calendar.HOUR_OF_DAY),
//                now.get(Calendar.MINUTE),
//                false
//        );
//        tpd.setThemeDark(false);
//        tpd.show(getFragmentManager(), "Timepickerdialog");
//        PersianCalendar persianCalendar =new PersianCalendar();
//        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
//                ReminderAddActivity.this,
//                persianCalendar,
//                persianCalendar.get(Calendar.MINUTE),false
//
//        );
//        timePickerDialog.show(getFragmentManager(), "Timepickerdialog");
    }

//    @Override
//    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//        monthOfYear++;
//        mDay = dayOfMonth;
//        mMonth = monthOfYear;
//        mYear = year;
//        mDate = dayOfMonth + "/" + monthOfYear + "/" + year;
//        mDateText.setText(mDate);
//    }
//
//    @Override
//    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
//
//
//        mHour = hourOfDay;
//        mMinute = minute;
//        if (minute < 10) {
//            mTime = hourOfDay + ":" + "0" + minute;
//        } else {
//            mTime = hourOfDay + ":" + minute;
//        }
//        mTimeText.setText(mTime);
//
//    }


    // On clicking the active button
    public void selectFab1(View v)
    {
        mFAB1 = findViewById(R.id.starred1);
        mFAB1.setVisibility(View.GONE);
        mFAB2 = findViewById(R.id.starred2);
        mFAB2.setVisibility(View.VISIBLE);
        mActive = "true";
    }

    // On clicking the inactive button
    public void selectFab2(View view)
    {
        mFAB2 = findViewById(R.id.starred2);
        mFAB2.setVisibility(View.GONE);
        mFAB1 = findViewById(R.id.starred1);
        mFAB1.setVisibility(View.VISIBLE);
        mActive = "false";
    }


    //On click time Picker


    // On clicking the repeat switch
    public void onSwitchRepeat(View view)
    {
        boolean on = ((Switch) view).isChecked();
        if (on)
        {
            mRepeat = "true";
            mRepeatText.setText(" هر" + mRepeatNo + " " + mRepeatType);
        }
        else
        {
            mRepeat = "false";
            mRepeatText.setText(R.string.repeat_off);
        }
    }


    public void setRepeatNo(View v)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("عدد مورد نظر راوارد کنید");

        // Create EditText box to input repeat number
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("تایید",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {

                        if (input.getText().toString().length() == 0)
                        {
                            mRepeatNo = Integer.toString(1);
                            mRepeatNoText.setText(mRepeatNo);
                            mRepeatText.setText("هر " + mRepeatNo + " " + mRepeatType);
                        }
                        else
                        {
                            mRepeatNo = input.getText().toString().trim();
                            mRepeatNoText.setText(mRepeatNo);
                            mRepeatText.setText("هر " + mRepeatNo + " " + mRepeatType);
                        }
                    }
                });
        alert.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                // do nothing
            }
        });
        alert.show();
    }

    public void select_RepeatType(View view)
    {

        final String[] items = new String[5];

        items[0] = "دقیقه";
        items[1] = "ساعت";
        items[2] = "روز";
        items[3] = "هفته";
        items[4] = "ماه";

        // Create List Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("انتخاب نوع تکرار");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item)
            {

                mRepeatType = items[item];
                mRepeatTypeText.setText(mRepeatType);
                mRepeatText.setText("هر " + mRepeatNo + " " + mRepeatType);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }


    // On pressing the back button
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    // Creating the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
        return true;
    }

    // On clicking menu buttons
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {

            // On clicking the back arrow
            // Discard any changes
            case android.R.id.home:
                onBackPressed();
                return true;

            // On clicking save reminder button
            // Update reminder
            case R.id.save_reminder:
                mTitleText.setText(mTitle);

                if (mTitleText.getText().toString().length() == 0)
                {
                    mTitleText.setError("لطفا عنوان مورد نظر را تایپ کنید!");
                }

                else
                {
                    saveReminder();
                }
                return true;

            // On clicking discard reminder button
            // Discard any changes
            case R.id.discard_reminder:
                Toast.makeText(getApplicationContext(), "ذخیره نشد!",
                        Toast.LENGTH_SHORT).show();

                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // On clicking the save button
    public void saveReminder()
    {
        ReminderDatabase rb = new ReminderDatabase(this);

        // Creating Reminder
        int ID = rb.addReminder(new Reminder(mTitle, mDate, mTime, mRepeat, mRepeatNo, mRepeatType, mActive));

        // Set up calender for creating the notification
        mCalender.set(Calendar.MONTH, --mMonth);
        mCalender.set(Calendar.YEAR, mYear);
        mCalender.set(Calendar.DAY_OF_MONTH, mDay);
        mCalender.set(Calendar.HOUR_OF_DAY, mHour);
        mCalender.set(Calendar.MINUTE, mMinute);
        mCalender.set(Calendar.SECOND, 0);

        // Check repeat type
        if (mRepeatType.equals("Minute"))
        {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milMinute;
        }
        else if (mRepeatType.equals("Hour"))
        {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milHour;
        }
        else if (mRepeatType.equals("Day"))
        {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milDay;
        }
        else if (mRepeatType.equals("Week"))
        {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milWeek;
        }
        else if (mRepeatType.equals("Month"))
        {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milMonth;
        }

        // Create a new notification
        if (mActive.equals("true"))
        {
            if (mRepeat.equals("true"))
            {
                new AlarmReceiver().setRepeatAlarm(getApplicationContext(), mCalender, ID, mRepeatTime);
            }
            else if (mRepeat.equals("false"))
            {
                new AlarmReceiver().setAlarm(getApplicationContext(), mCalender, ID);
            }
        }

        // Create toast to confirm new reminder
        Toast.makeText(getApplicationContext(), "Saved",
                Toast.LENGTH_SHORT).show();

        onBackPressed();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)
    {

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute)
    {

    }
}
