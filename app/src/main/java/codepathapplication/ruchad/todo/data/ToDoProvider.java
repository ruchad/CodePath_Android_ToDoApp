package codepathapplication.ruchad.todo.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import codepathapplication.ruchad.todo.R;

/**
 * Created by ruchadeshmukh on 1/1/16.
 */
public class ToDoProvider extends ContentProvider{

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private ToDoDBHelper mToDoDBHelper;

    static final int ToDo = 100;
    static final int ToDo_ITEM = 200;

    static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ToDoContract.CONTENT_AUTHORITY, ToDoContract.PATH_TODOLIST, ToDo);
        uriMatcher.addURI(ToDoContract.CONTENT_AUTHORITY, ToDoContract.PATH_TODOLIST + "/#", ToDo_ITEM);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mToDoDBHelper = new ToDoDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case ToDo:
                return ToDoContract.ToDoEntry.CONTENT_TYPE;
            case ToDo_ITEM:
                return  ToDoContract.ToDoEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("unkown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)){
            case ToDo:
                Context context = getContext();
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
                if(pref.getString(context.getString(R.string.pref_sort_key), context.getString(R.string.pref_sort_default_value)).equalsIgnoreCase(context.getString(R.string.pref_sort_value_category)))
                    retCursor = mToDoDBHelper.getReadableDatabase().query(ToDoContract.ToDoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, ToDoContract.ToDoEntry.COLUMN_TODO_CATEGORY + " ASC");
                else
                    retCursor = mToDoDBHelper.getReadableDatabase().query(ToDoContract.ToDoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;
            case ToDo_ITEM:
                retCursor = mToDoDBHelper.getReadableDatabase().query(ToDoContract.ToDoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Unkown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mToDoDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch(match){
            case ToDo: {
                long _id = db.insert(ToDoContract.ToDoEntry.TABLE_NAME, null, values);

                if (_id > 0)
                    returnUri = ToDoContract.ToDoEntry.buildToDoListItemUri(_id);
                else
                    throw new SQLException("Failed to insert row");
                break;
            }

            default:
                throw new UnsupportedOperationException("Unkown uri" + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mToDoDBHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        if(null==selection)
            selection= "1";

        int rowsDeleted;

        switch (match){
            case ToDo:
                rowsDeleted = db.delete(ToDoContract.ToDoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unkown URI: " + uri);
        }

        if(rowsDeleted!=0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mToDoDBHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        if(null==selection)
            selection= "1";

        int rowsUpdated;

        switch (match){
            case ToDo:
                rowsUpdated = db.update(ToDoContract.ToDoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unkown URI: " + uri);
        }

        if(rowsUpdated!=0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }
}
