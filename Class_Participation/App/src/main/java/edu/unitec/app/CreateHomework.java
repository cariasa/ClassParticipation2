package edu.unitec.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class CreateHomework extends Activity {


    public Homework homework;
    private Section currentSection = new Section();
    private ArrayAdapter arrayAdapter;
    private List<String> listCriteria;
    private  ListView criteriaListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_homework);
        Intent intent = getIntent();
        currentSection = (Section)intent.getSerializableExtra("Section");
        setTitle("CreateHomework");
        showCreateHomeworkDialog();
        criteriaListView = (ListView) findViewById(R.id.listView_criteria);
        listCriteria = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,listCriteria);
        criteriaListView.setAdapter(arrayAdapter);
        ClickCallback();
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
        if (id == R.id.item_addCriteria) {
            AddCriteriaDialog dialog = new AddCriteriaDialog(homework,arrayAdapter,listCriteria);
            dialog.show(getFragmentManager(), "dialog_create_homework");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void showCreateHomeworkDialog(){
        CreateHomeworkDialog dialog = new CreateHomeworkDialog(currentSection);
        dialog.show(getFragmentManager(), "dialog_create_homework");
    }
    private void ClickCallback()
    {
        ListView listview = (ListView) findViewById(R.id.listView_criteria);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                ShowCriteriaDialog dialog = new ShowCriteriaDialog(homework, listCriteria.get(position));
                dialog.show(getFragmentManager(), "dialog_show_criteria");
            }
        });
    }
}
