package codepathapplication.ruchad.todo.fragments;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
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

import codepathapplication.ruchad.todo.R;
import codepathapplication.ruchad.todo.receivers.ReminderAlarmService;
import codepathapplication.ruchad.todo.activities.ToDoMainActivity;
import codepathapplication.ruchad.todo.data.ToDoContract;

/**
 * Created by ruchadeshmukh on 1/9/16.
 */
public class ToDoEditFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener,
        AdapterView.OnItemSelectedListener{

    public ToDoEditFragment(){}

    public static final int EDIT_LOADER = 0;
    public static final String EDIT_URI = "URI";
    private Uri mUri;

    private static final String[] TODO_COLUMNS = {
            ToDoContract.ToDoEntry.TABLE_NAME + "." + ToDoContract.ToDoEntry._ID,
            ToDoContract.ToDoEntry.COLUMN_TODO_TITLE,
            ToDoContract.ToDoEntry.COLUMN_TODO_DESCRIPTION,
            ToDoContract.ToDoEntry.COLUMN_TODO_CATEGORY,
            ToDoContract.ToDoEntry.COLUMN_TODO_REMINDER,
            ToDoContract.ToDoEntry.COLUMN_TODO_REMINDER_DATE
    };


    private EditText mTitleTextView;
    private EditText mDescriptionTextView;
    private Spinner mCategorySpinnerView;
    private ToggleButton mReminder;
    private TextView mReminderDateView;
    private TextView mReminderTimeView;
    private Button mSaveView;
    private Button mCancelView;

    static final int COL_ToDo_ID=0;
    static final int COL_ToDo_TITLE=1;
    static final int COL_ToDo_DESCRIPTION=2;
    static final int COL_ToDO_CATEGORY=3;
    static final int COL_ToDo_REMINDER=4;
    static final int COL_ToDo_REMINDERDATE=5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if(arguments!=null){
            mUri = arguments.getParcelable(ToDoDetailFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_add_todo_item, container, false);

        mTitleTextView = (EditText) rootView.findViewById(R.id.title_textView);
        mDescriptionTextView = (EditText)rootView.findViewById(R.id.description_textView);
        mCategorySpinnerView = (Spinner) rootView.findViewById(R.id.category_spinner);
        mCategorySpinnerView.setOnItemSelectedListener(this);
        mReminderDateView = (TextView) rootView.findViewById(R.id.reminderDate_btnView);
        mReminderDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFramgent datePickerFramgent = new DatePickerFramgent();
                Bundle args = new Bundle();
                args.putString("type", "edit");
                datePickerFramgent.setArguments(args);
                datePickerFramgent.show(getFragmentManager(), "EditDatePicker");
            }
        });

        mReminderTimeView = (TextView)rootView.findViewById(R.id.reminderTime_btnView);
        mReminderTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                Bundle args = new Bundle();
                args.putString("type", "edit");
                timePickerFragment.setArguments(args);
                timePickerFragment.show(getFragmentManager(), "EditTimePicker");
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
                getActivity().getContentResolver().update(ToDoContract.ToDoEntry.CONTENT_URI, values, "_id="+ mUri.getLastPathSegment(), null );
                Intent i = new Intent(getActivity(), ToDoMainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(EDIT_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(mUri!=null){
            return new CursorLoader(getActivity(),
                    mUri,
                    TODO_COLUMNS,
                    null,
                    null,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        String id = mUri.getLastPathSegment();
        while(data.moveToNext()){
            if(data.getInt(COL_ToDo_ID)==Integer.parseInt(id)){
                mTitleTextView.setText(data.getString(COL_ToDo_TITLE));
                mDescriptionTextView.setText(data.getString(COL_ToDo_DESCRIPTION));

                int index = getIndex(mCategorySpinnerView, data.getString(COL_ToDO_CATEGORY));
                mCategorySpinnerView.setSelection(index);

                mReminder.setText(data.getString(COL_ToDo_REMINDER));
                if(data.getString(COL_ToDo_REMINDER).equalsIgnoreCase("on")){
                    String datetime = data.getString(COL_ToDo_REMINDERDATE);
                    String[] val=datetime.split(" ");
                    mReminderDateView.setText(val[0]);
                    mReminderTimeView.setText(val[1]);
                }else {
                    mReminderDateView.setText("");
                    mReminderTimeView.setText("");
                }
            }
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        getLoaderManager().restartLoader(EDIT_LOADER, null, this);
    }

    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }
}
