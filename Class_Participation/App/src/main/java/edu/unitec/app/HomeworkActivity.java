package edu.unitec.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class HomeworkActivity extends Activity {


    public Homework homework;
    private Section currentSection = new Section();
    private int studentId;
    private ArrayAdapter arrayAdapter;
    private List<String> listCriteria;
    private  ListView criteriaListView;
    private boolean isCreating;

    public void setHomework(Homework homework) {
        this.homework = homework;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        Intent intent = getIntent();
        currentSection = (Section)intent.getSerializableExtra("Section");
        isCreating=intent.getBooleanExtra("isCreating",false);
        studentId=intent.getIntExtra("studentId",0);
        criteriaListView = (ListView) findViewById(R.id.listView_criteria);
        listCriteria = new ArrayList<String>();
        if(isCreating){
            setTitle("CreateHomework");
            showCreateHomeworkDialog();
        }else{
            homework=(Homework)intent.getSerializableExtra("Homework");
            setTitle(homework.getHomeworkName());
            List<Criteria> criteriaList=getCurrentCriteriaList();
            for(int i=0;i<criteriaList.size();i++){
                listCriteria.add(criteriaList.get(i).getCriteriaName());
            }
        }
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,listCriteria);
        criteriaListView.setAdapter(arrayAdapter);
        ClickCallback();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_homework, menu);
        MenuItem addCriteriaItem = menu.findItem(R.id.item_addCriteria);
        if(isCreating){
            addCriteriaItem.setVisible(true);
        }else{
            addCriteriaItem.setVisible(false);
        }
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
                if(isCreating){
                    ShowCriteriaDialog dialog = new ShowCriteriaDialog(homework, listCriteria.get(position));
                    dialog.show(getFragmentManager(), "dialog_show_criteria");
                }else{

                    CheckCriteriaDialog dialog = new CheckCriteriaDialog(studentId,getCurrentCriteriaList().get(position));
                    dialog.show(getFragmentManager(), "dialog_check_criteria");
                }

            }
        });
    }
    public List<Criteria> getCurrentCriteriaList(){
        DatabaseHandler db=new DatabaseHandler(this);
        return db.getAllCriteriaByHomework(homework.getHomeworkId());
    }
}
