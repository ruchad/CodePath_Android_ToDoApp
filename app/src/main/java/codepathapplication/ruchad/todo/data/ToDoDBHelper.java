package codepathapplication.ruchad.todo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import codepathapplication.ruchad.todo.ToDoMainActivity;

/**
 * Created by ruchadeshmukh on 1/1/16.
 */
public class ToDoDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="todo_list.db";
    private static final int DATABASE_VERSION = 1;

    public ToDoDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + ToDoContract.ToDoEntry.TABLE_NAME + " (" +
                ToDoContract.ToDoEntry._ID + " INTEGER PRIMARY KEY, " +
                ToDoContract.ToDoEntry.COLUMN_TODO_TITLE + " TEXT NOT NULL, " +
                ToDoContract.ToDoEntry.COLUMN_TODO_DESCRIPTION + " TEXT NOT NULL, " +
                ToDoContract.ToDoEntry.COLUMN_TODO_CATEGORY  + " TEXT NOT NULL, " +
                ToDoContract.ToDoEntry.COLUMN_TODO_REMINDER + " TEXT, " +
                ToDoContract.ToDoEntry.COLUMN_TODO_REMINDER_DATE + " TEXT);";
        Log.d(ToDoMainActivity.logTag, SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
