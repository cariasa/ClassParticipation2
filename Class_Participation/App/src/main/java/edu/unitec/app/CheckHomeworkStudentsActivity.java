package edu.unitec.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by NivxB on 02-02-15.
 */
public class CheckHomeworkStudentsActivity extends Activity {
    private Homework homework;
    private String UUID;
    private int SectionId;
    private List<Student> studentList;
    private ArrayAdapter<Student> studentArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_homework);
        Intent intent = getIntent();
        homework = (Homework)intent.getSerializableExtra("Homework");
        UUID = intent.getStringExtra("UUID");
        SectionId = intent.getIntExtra("SectionId",0);
        try {
            DatabaseHandler db = new DatabaseHandler(this.getApplicationContext());
            studentList = db.getStudentsBySectionId(SectionId,UUID);

            db.close();
        }catch (Exception E){

        }
        ListView listViewStudent = (ListView) findViewById(R.id.listViewCheckHomework);
        studentArrayAdapter = new MyListAdapter();
        listViewStudent.setAdapter(studentArrayAdapter);
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
                intent.putExtra("Homework", homework);
                intent.putExtra("studentId",studentList.get(position).get_StudentId());
                intent.putExtra("UUID",UUID);
                startActivity(intent);

            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        try {
            studentArrayAdapter.notifyDataSetChanged();
        }catch(Exception e){

        }
    }

    public class MyListAdapter extends ArrayAdapter<Student>{
        public MyListAdapter(){
            super( CheckHomeworkStudentsActivity.this, R.layout.item_listview_homework,studentList);
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
                String HomeworkName = String.format("%-15.15s",studentList.get(position).get_StudentName());

                item_name.setText(HomeworkName);

                //Percentage view
                TextView  txtPercentage= (TextView)itemView.findViewById(R.id.item_homework_percentage);
                txtPercentage.setTypeface(Typeface.MONOSPACE);

//                Log.e(CheckHomework.class.toString(), "NameAndGrade="+nameAndGrade);

                String Percentage = String.format("%-4s","   ");
                txtPercentage.setText(Percentage);

            }catch(Exception e){
            }

            return itemView;
        }
    }


}
