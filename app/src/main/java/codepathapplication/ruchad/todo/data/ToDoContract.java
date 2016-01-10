package codepathapplication.ruchad.todo.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ruchadeshmukh on 12/30/15.
 */
public class ToDoContract {
    public static final String CONTENT_AUTHORITY = "codepathapplication.ruchad";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_TODOLIST = "todo";
    public static final String PATH_TODOLISTITEM = "todo/#";

    public static final class ToDoEntry implements BaseColumns {
        public static final String TABLE_NAME = "todo";

        public static final String COLUMN_TODO_TITLE = "todo_title";
        public static final String COLUMN_TODO_DESCRIPTION = "todo_description";
        public static final String COLUMN_TODO_CATEGORY="todo_category";
        public static final String COLUMN_TODO_REMINDER = "todo_reminder";
        public static final String COLUMN_TODO_REMINDER_DATE = "todo_reminder_date";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TODOLIST).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TODOLIST;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TODOLISTITEM;

        public static Uri buildToDoList(){return CONTENT_URI.buildUpon().build();}

        public static Uri buildToDoListItemUri(long id){return ContentUris.withAppendedId(CONTENT_URI, id);}
    }
}
