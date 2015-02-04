package edu.unitec.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class CheckHomeworksActivity extends Activity {
    int sectionId;
    String UUID = "";
    List<Homework> currentHomeworkList;
    ArrayAdapter<Homework> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_homework);
        Intent intent=getIntent();
        //String student_name = intent.getStringExtra("StudentName");
        //studentId=intent.getIntExtra("studentId",0);
        //setTitle(student_name+" Homeworks");
        sectionId = intent.getIntExtra("Section",0);
        UUID = intent.getStringExtra("UUID");
        try
        {
            DatabaseHandler db = new DatabaseHandler(this.getApplicationContext());
            currentHomeworkList = db.getAll_Homework(sectionId,UUID);
            db.close();
        }

        catch (Exception e)
        {
        }
        ListView listViewHomework = (ListView) findViewById(R.id.listViewCheckHomework);
        /*ArrayList arrayHomework = new ArrayList<String>();
        for(int i=0;i<currentHomeworkList.size();i++){
            arrayHomework.add(((Homework) currentHomeworkList.get(i)).getHomeworkName());
        }*/
        arrayAdapter = new MyListAdapter();
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
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                /*
                Intent intent = new Intent(view.getContext(), HomeworkActivity.class);
                intent.putExtra("Homework", currentHomeworkList.get(position));
                intent.putExtra("studentId",studentId);
                intent.putExtra("UUID",UUID);
                startActivity(intent);
                */

                Intent intent = new Intent(view.getContext(),CheckHomeworkStudentsActivity.class);
                intent.putExtra("Homework",currentHomeworkList.get(position));
                intent.putExtra("SectionId",sectionId);
                intent.putExtra("UUID",UUID);
                startActivity(intent);

            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        try {
            arrayAdapter.notifyDataSetChanged();
        }catch(Exception e){

        }
    }

    //class myAdapter for my personal style listView
    public class MyListAdapter extends ArrayAdapter<Homework>{
        public MyListAdapter(){
            super( CheckHomeworksActivity.this, R.layout.item_listview_homework,currentHomeworkList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent ){
            View itemView = convertView;
            //if itemView is null we create a new one
            if(itemView == null ){
                itemView = getLayoutInflater().inflate(R.layout.item_listview_homework, parent, false);
            }
            try{

                //homework name view
                TextView item_name = (TextView)itemView.findViewById(R.id.item_homework_name);

                item_name.setTypeface(Typeface.MONOSPACE);
                String HomeworkName = String.format("%-15.15s",currentHomeworkList.get(position).getHomeworkName());

                item_name.setText(HomeworkName);

                //Percentage view
                TextView  txtPercentage= (TextView)itemView.findViewById(R.id.item_homework_percentage);
                txtPercentage.setTypeface(Typeface.MONOSPACE);


                String Percentage = String.format("%-4s","    ");

                txtPercentage.setText(Percentage);

            }catch(Exception e){
            }

            return itemView;
        }
    }
}
