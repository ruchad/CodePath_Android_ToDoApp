package codepathapplication.ruchad.todo.fragments;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import codepathapplication.ruchad.todo.R;
import codepathapplication.ruchad.todo.receivers.ReminderAlarmService;
import codepathapplication.ruchad.todo.data.ToDoContract;

/**
 * Created by ruchadeshmukh on 1/4/16.
 */
public class ToDoAddFragment extends Fragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener,
        AdapterView.OnItemSelectedListener{

    public ToDoAddFragment(){}

    private EditText mTitleTextView;
    private EditText mDescriptionTextView;
    private Spinner mCategorySpinnerView;
    private ToggleButton mReminder;
    private TextView mReminderDateView;
    private TextView mReminderTimeView;
    private Button mSaveView;
    private Button mCancelView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_todo_item, container, false);
        mTitleTextView = (EditText) rootView.findViewById(R.id.title_textView);
        mDescriptionTextView = (EditText)rootView.findViewById(R.id.description_textView);
        mCategorySpinnerView = (Spinner) rootView.findViewById(R.id.category_spinner);
        mCategorySpinnerView.setOnItemSelectedListener(this);

        Date dt = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        mReminderDateView = (TextView) rootView.findViewById(R.id.reminderDate_btnView);
        mReminderDateView.setText(simpleDateFormat.format(dt));
        mReminderDateView.setEnabled(false);
        mReminderDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFramgent datePickerFramgent = new DatePickerFramgent();
                Bundle args = new Bundle();
                args.putString("type", "add");
                datePickerFramgent.setArguments(args);
                datePickerFramgent.show(getFragmentManager(), "datePicker");
            }
        });


        Calendar cal = Calendar.getInstance();
        mReminderTimeView = (TextView)rootView.findViewById(R.id.reminderTime_btnView);
        mReminderTimeView.setText(cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
        mReminderTimeView.setEnabled(false);
        mReminderTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                Bundle args = new Bundle();
                args.putString("type", "add");
                timePickerFragment.setArguments(args);
                timePickerFragment.show(getFragmentManager(), "timePicker");
            }
        });

        mReminder = (ToggleButton) rootView.findViewById(R.id.reminder_toggleView);
        mReminder.setBackgroundColor(Color.parseColor("#44146f"));
        mReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mReminder.getText().toString().equalsIgnoreCase("off")) {
                    mReminderDateView.setEnabled(false);
                    mReminderTimeView.setEnabled(false);
                } else {
                    mReminderDateView.setEnabled(true);
                    mReminderTimeView.setEnabled(true);
                }

            }
        });

        mSaveView = (Button) rootView.findViewById(R.id.saveView);
        mSaveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = mTitleTextView.getText().toString();
                if(Title.matches("")){
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
                    return;
                }

                String Description = mDescriptionTextView.getText().toString();
                String Category = mCategorySpinnerView.getSelectedItem().toString();
                String reminder = mReminder.getText().toString();
                String reminderDate = mReminderDateView.getText().toString();
                String reminderTime = mReminderTimeView.getText().toString();

                ContentValues values = new ContentValues();
                values.put(ToDoContract.ToDoEntry.COLUMN_TODO_TITLE, Title);
                values.put(ToDoContract.ToDoEntry.COLUMN_TODO_DESCRIPTION, Description);
                values.put(ToDoContract.ToDoEntry.COLUMN_TODO_CATEGORY, Category);
                values.put(ToDoContract.ToDoEntry.COLUMN_TODO_REMINDER, reminder);
                values.put(ToDoContract.ToDoEntry.COLUMN_TODO_REMINDER_DATE, reminderDate + " " + reminderTime);

                if(reminder.equalsIgnoreCase("on")){
                    try{
                        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm");
                        long time = formatter.parse(reminderDate +" " + reminderTime).getTime();
                        AlarmManager alarmManager = (AlarmManager) getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(getActivity().getApplicationContext(), ReminderAlarmService.class);
                        intent.putExtra("Title", Title);
                        intent.putExtra("Description", Description);
                        final int _id = (int) System.currentTimeMillis();
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
                    }catch (ParseException e){
                        e.printStackTrace();
                    }
                }

                getActivity().getContentResolver().insert(ToDoContract.ToDoEntry.CONTENT_URI, values);
                getActivity().finish();
            }
        });

        mCancelView = (Button)rootView.findViewById(R.id.cancelView);
        mCancelView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return rootView;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String time = hourOfDay + ":" + minute;
        mReminderTimeView.setText(time);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String date = (monthOfYear+1) + "/" + dayOfMonth + "/" + year;
        mReminderDateView.setText(date);
    }
}
