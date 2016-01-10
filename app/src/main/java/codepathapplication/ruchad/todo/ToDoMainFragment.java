package codepathapplication.ruchad.todo;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import codepathapplication.ruchad.todo.data.ToDoContract;

/**
 * Created by ruchadeshmukh on 12/30/15.
 */
public class ToDoMainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public ToDoMainFragment(){}

    ToDoAdapter mToDoAdapter;
    private ListView mListView;
    static final int TODO_LOADER = 0;

    private static final String[] TODO_COLUMNS = {
            ToDoContract.ToDoEntry.TABLE_NAME + "." + ToDoContract.ToDoEntry._ID,
            ToDoContract.ToDoEntry.COLUMN_TODO_TITLE,
            ToDoContract.ToDoEntry.COLUMN_TODO_DESCRIPTION,
            ToDoContract.ToDoEntry.COLUMN_TODO_CATEGORY
    };

    static final int COL_ToDo_ID=0;
    static final int COL_ToDo_TITLE=1;
    static final int COL_ToDo_DESCRIPTION=2;
    static final int COL_ToDO_CATEGORY=3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(TODO_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mToDoAdapter = new ToDoAdapter(getActivity(), null, 0);

        mListView = (ListView) rootView.findViewById(R.id.listview_todoFragment);
        mListView.setAdapter(mToDoAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = mToDoAdapter.getCursor();
                cursor.moveToPosition(position);
                if (cursor != null) {
                    ((Callback) getActivity()).onItemSelected(ToDoContract.ToDoEntry.buildToDoListItemUri(cursor.getInt(COL_ToDo_ID)));
                }
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = mToDoAdapter.getCursor();
                c.moveToPosition(position);
                long rowId = c.getLong(c.getColumnIndex(ToDoContract.ToDoEntry._ID));
                getActivity().getContentResolver().delete(ToDoContract.ToDoEntry.CONTENT_URI, ToDoContract.ToDoEntry._ID + "=" + rowId, null);
                mToDoAdapter.notifyDataSetChanged();
                return true;
            }
        });


        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri ToDoList = ToDoContract.ToDoEntry.buildToDoList();
        return new CursorLoader(getActivity(),
                ToDoList,
                TODO_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mToDoAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mToDoAdapter.swapCursor(null);
    }

    public interface Callback {
        public void onItemSelected(Uri detailUri);
    }

    void onSortByChanged(){
        getLoaderManager().restartLoader(TODO_LOADER, null, this);
    }
}
