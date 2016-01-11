package codepathapplication.ruchad.todo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import codepathapplication.ruchad.todo.R;
import codepathapplication.ruchad.todo.fragments.ToDoEditFragment;

/**
 * Created by ruchadeshmukh on 1/9/16.
 */
public class ToDoEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo_item);

        Bundle arguments = new Bundle();
        arguments.putParcelable(ToDoEditFragment.EDIT_URI, getIntent().getParcelableExtra("URI"));
        ToDoEditFragment fragment = new ToDoEditFragment();
        fragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.todo_add_container, fragment)
                .commit();
    }

}
