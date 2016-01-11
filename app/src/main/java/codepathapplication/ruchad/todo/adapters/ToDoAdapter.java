package codepathapplication.ruchad.todo.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import codepathapplication.ruchad.todo.R;
import codepathapplication.ruchad.todo.fragments.ToDoMainFragment;

/**
 * Created by ruchadeshmukh on 1/1/16.
 */
public class ToDoAdapter extends CursorAdapter{

    public ToDoAdapter(Context context, Cursor c, int flags){
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layoutId = R.layout.list_item_todo;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.titleView.setText(cursor.getString(ToDoMainFragment.COL_ToDo_TITLE));
        viewHolder.descriptionView.setText(cursor.getString(ToDoMainFragment.COL_ToDo_DESCRIPTION));
        viewHolder.categoryView.setText(cursor.getString(ToDoMainFragment.COL_ToDO_CATEGORY));
    }

    public static class ViewHolder {
        public final TextView titleView;
        public final TextView descriptionView;
        public final TextView categoryView;

        public ViewHolder(View view){
            titleView = (TextView) view.findViewById(R.id.list_item_todo_title);
            descriptionView = (TextView) view.findViewById(R.id.list_item_todo_description);
            categoryView = (TextView) view.findViewById(R.id.list_item_todo_category);
        }
    }
}
