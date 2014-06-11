package edu.unitec.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class CreateHomework extends Activity {


    public Homework homework;
    private Section currentSection = new Section();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_homework);
        Intent intent = getIntent();
        currentSection = (Section)intent.getSerializableExtra("Section");
        setTitle("CreateHomework");
        showCreateHomeworkDialog();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_homework, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void showCreateHomeworkDialog(){
        CreateHomeworkDialog dialog = new CreateHomeworkDialog(currentSection);
        dialog.show(getFragmentManager(), "dialog_create_homework");
    }
}
