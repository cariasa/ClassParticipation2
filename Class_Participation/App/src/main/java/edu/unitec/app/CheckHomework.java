package edu.unitec.app;

import android.app.Activity;
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


public class CheckHomework extends Activity {
    int sectionId;
    int studentId;
    List<Homework> currentHomeworkList;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_homework);
        Intent intent=getIntent();
        String student_name = intent.getStringExtra("StudentName");
        studentId=intent.getIntExtra("studentId",0);
        setTitle(student_name+" Homeworks");
        sectionId = intent.getIntExtra("Section",0);
        try
        {
            DatabaseHandler db = new DatabaseHandler(this.getApplicationContext());
            currentHomeworkList = db.getAll_Homework(sectionId);
            db.close();
        }

        catch (Exception e)
        {
        }
        ListView listViewHomework = (ListView) findViewById(R.id.listViewCheckHomework);
        ArrayList arrayHomework = new ArrayList<String>();
        for(int i=0;i<currentHomeworkList.size();i++){
            arrayHomework.add(((Homework) currentHomeworkList.get(i)).getHomeworkName());
        }
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayHomework);
        listViewHomework.setAdapter(arrayAdapter);
        ClickCallback();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.check_homework, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }
    private void ClickCallback(){
        ListView listview = (ListView) findViewById(R.id.listViewCheckHomework);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                Intent intent = new Intent(view.getContext(), HomeworkActivity.class);
                intent.putExtra("Homework", currentHomeworkList.get(position));
                intent.putExtra("studentId",studentId);
                startActivity(intent);
            }
        });
    }
}
