package edu.unitec.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nivx on 12/7/14.
 */
public class ReportActivity extends Activity {
    private String UUID;
    private String course_name;
    private Section currentSection;

   // private List<String> listReport;
    private ListView ListReport;
    private ArrayAdapter<String> arrayAdapter;

    private List<String> listHomeworkGrades;
    private List<String> listParticipationGrades;
    private List<String> setVal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Intent intent = getIntent();

        currentSection = (Section)intent.getSerializableExtra("currentSection");
        UUID = intent.getStringExtra("UUID");
        course_name = intent.getStringExtra("coursename");

        DatabaseHandler db=new DatabaseHandler(this);

        listHomeworkGrades=db.getTotalHomeworkGrades(UUID,currentSection.get_SectionId());
        ListReport = (ListView)findViewById(R.id.listviewReport);
        listParticipationGrades=db.getTotalParticipationGrades(UUID,currentSection.get_SectionId());

        setVal = new ArrayList();
        for (int i = 0 ; i < listHomeworkGrades.size() ; i ++){
            String addVal = listHomeworkGrades.get(i).split("SEPARATOR")[0]+"SEPARATOR"+listHomeworkGrades.get(i).split("SEPARATOR")[1]+listHomeworkGrades.get(i).split("SEPARATOR")[2]
                    +"SEPARATOR"+listParticipationGrades.get(i).split("SEPARATOR")[4];

            setVal.add(addVal);

        }


        arrayAdapter = new MyListAdapter();
        ListReport.setAdapter(arrayAdapter);

        setTitle(course_name);

        ClickCallback();

    }

    private void ClickCallback() {
        ListView listview = (ListView) findViewById(R.id.listviewReport);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
            //(List<Participation> list, String studentName, double finalGrade, List<String> homework,double percentageParticipation, double percentageHomework
                DatabaseHandler db = new DatabaseHandler(view.getContext());
                String STUDENTIR = setVal.get(position).split("SEPARATOR")[0];
                List<String> FinalParticipations = db.getTotalParticipationGrades(UUID,currentSection.get_SectionId(),STUDENTIR);
                List<String> FinalHomeworks = db.getTotalHomeworkGrades(UUID,currentSection.get_SectionId(),STUDENTIR);

                List<String> AllHomeworks  = db.getHomeworkNameAndGrade(Integer.parseInt(STUDENTIR),currentSection.get_SectionId(),UUID);


            }
        });
    }


    public class MyListAdapter extends ArrayAdapter<String> {

        public MyListAdapter(){
            super( ReportActivity.this, R.layout.item_listview_report, setVal);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent ){
            View itemView = convertView;
            //if itemView is null we create a new one
            if(itemView == null ){
                itemView = getLayoutInflater().inflate(R.layout.item_listview_report, parent, false);
            }
            //find the course to work with and the section
            try{
                //homework name view
                String STUDENTIR = setVal.get(position).split("SEPARATOR")[0];
                String NAME = setVal.get(position).split("SEPARATOR")[1];
                String HOMEWORK = setVal.get(position).split("SEPARATOR")[2];
                String PARTICIPATION = setVal.get(position).split("SEPARATOR")[3];


                TextView StudentName = (TextView)itemView.findViewById(R.id.StudentNameReport);
                StudentName.setText(NAME);

                //Percentage view
                TextView  ParticipationPercentage= (TextView)itemView.findViewById(R.id.ParticipationReport);
                ParticipationPercentage.setText(HOMEWORK);

                TextView  HomeworkPercentage= (TextView)itemView.findViewById(R.id.HomeworkReport);
                HomeworkPercentage.setText(PARTICIPATION);

            }catch(Exception e){
            }

            return itemView;
        }
    }

}
