package codepathapplication.ruchad.todo;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import codepathapplication.ruchad.todo.data.ToDoContract;

/**
 * Created by ruchadeshmukh on 1/4/16.
 */
public class ToDoDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final int DETAIL_LOADER = 0;
    public static final String DETAIL_URI = "URI";
    private Uri mUri;

    private static final String[] TODO_COLUMNS = {
            ToDoContract.ToDoEntry.TABLE_NAME + "." + ToDoContract.ToDoEntry._ID,
            ToDoContract.ToDoEntry.COLUMN_TODO_TITLE,
            ToDoContract.ToDoEntry.COLUMN_TODO_DESCRIPTION,
            ToDoContract.ToDoEntry.COLUMN_TODO_CATEGORY,
            ToDoContract.ToDoEntry.COLUMN_TODO_REMINDER,
            ToDoContract.ToDoEntry.COLUMN_TODO_REMINDER_DATE
    };


    private TextView mTitleTextView;
    private TextView mDescriptionTextView;
    private TextView mCategoryView;
    private TextView mReminderView;
    private TextView mReminderDateView;

    static final int COL_ToDo_ID=0;
    static final int COL_ToDo_TITLE=1;
    static final int COL_ToDo_DESCRIPTION=2;
    static final int COL_ToDO_CATEGORY=3;
    static final int COL_ToDo_REMINDER=4;
    static final int COL_ToDo_REMINDERDATE=5;

    public ToDoDetailFragment(){
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if(arguments!=null){
            mUri = arguments.getParcelable(ToDoDetailFragment.DETAIL_URI);
        }
        View rootView = inflater.inflate(R.layout.fragment_todo_detail, container, false);
        mTitleTextView = (TextView) rootView.findViewById(R.id.title_textView);
        mDescriptionTextView = (TextView)rootView.findViewById(R.id.description_textView);
        mCategoryView = (TextView) rootView.findViewById(R.id.category_textView);
        mReminderView = (TextView) rootView.findViewById(R.id.reminder_toggleView);
        mReminderDateView = (TextView) rootView.findViewById(R.id.remindertime_textView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        String id = mUri.getLastPathSegment();
        while(data.moveToNext()){
            if(data.getInt(COL_ToDo_ID)==Integer.parseInt(id)){
                mTitleTextView.setText(data.getString(COL_ToDo_TITLE));
                mDescriptionTextView.setText(data.getString(COL_ToDo_DESCRIPTION));
                mCategoryView.setText(data.getString(COL_ToDO_CATEGORY));
                mReminderView.setText(data.getString(COL_ToDo_REMINDER));
                if(data.getString(COL_ToDo_REMINDER).equalsIgnoreCase("on")){
                    mReminderDateView.setText(data.getString(COL_ToDo_REMINDERDATE));
                }else
                    mReminderDateView.setText("");
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
    }
}
