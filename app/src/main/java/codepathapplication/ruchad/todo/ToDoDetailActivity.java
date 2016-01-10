package codepathapplication.ruchad.todo;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by ruchadeshmukh on 12/30/15.
 */
public class ToDoDetailActivity extends AppCompatActivity {

    private Uri URI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);

        URI = getIntent().getData();
        if(savedInstanceState==null){
            Bundle arguments = new Bundle();
            arguments.putParcelable(ToDoDetailFragment.DETAIL_URI, URI);
            ToDoDetailFragment fragment = new ToDoDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.todo_detail_container, fragment)
                    .commit();
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purple)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todo_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id==R.id.action_settings){
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }

        if(id==R.id.action_edit){
            Intent i = new Intent(this, ToDoEditActivity.class);
            i.putExtra("URI", URI);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
