package codepathapplication.ruchad.todo;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class ToDoMainActivity extends AppCompatActivity implements ToDoMainFragment.Callback{

    public final static String logTag = "codepath_todo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0f);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purple)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id==R.id.action_settings){
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }

        if(id==R.id.menu_item_add){
            Intent i = new Intent(this, ToDoAddActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(Uri detailUri) {
        Intent intent = new Intent(this, ToDoDetailActivity.class).setData(detailUri);
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
        ToDoMainFragment toDoFragment = (ToDoMainFragment) getSupportFragmentManager().findFragmentById(R.id.todo_list_container);
        toDoFragment.onSortByChanged();
    }
}
