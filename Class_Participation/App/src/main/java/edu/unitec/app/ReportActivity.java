package edu.unitec.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
   // private List<String> listParticipationGrades;
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

        ListReport = (ListView)findViewById(R.id.listviewReport);

        setVal = db.getSectionGrades(currentSection.get_SectionId(),UUID);

        if (setVal == null){
            setVal = new ArrayList();
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
                String STUDENTNAME = setVal.get(position).split("SEPARATOR")[1];
                double ParticipationPercentage = Double.parseDouble(setVal.get(position).split("SEPARATOR")[2]);
                double HomeworkPercentage = Double.parseDouble(setVal.get(position).split("SEPARATOR")[2]);

                List<String> AllHomeworks  = db.getHomeworkNameAndGrade(Integer.parseInt(STUDENTIR),currentSection.get_SectionId(),UUID);
                List<Participation> AllParticipation = db.getParticipationStudent(UUID,STUDENTIR,currentSection.get_SectionId()+"");

                /*
                List<Participation> list, String studentName, double finalGrade, List<String> homework,double percentageParticipation, double percentageHomework
                 */

                StudentDialog Statistics = new StudentDialog(AllParticipation,STUDENTNAME,0,AllHomeworks,ParticipationPercentage,HomeworkPercentage);
                Statistics.show(getFragmentManager(), "dialog_student");
                db.close();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.export_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch ( item.getItemId() ) {
            case R.id.exportcsv:
                ReadWriteFileManager FM = new ReadWriteFileManager();
                if (FM.exportReport(currentSection.get_SectionId()+"",setVal)){
                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("plain/text");
                    /*emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]
                            {"me@gmail.com"});
                            */
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                            "Report Generated from Class Participation");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                            "Report of Section " + currentSection.get_SectionCode());

                    String fileLocation = Environment.getExternalStorageDirectory()+ "/Report_SectionId" + currentSection.get_SectionId() + ".csv";

                    emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+fileLocation));
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));


                    Toast.makeText(getApplicationContext(), "Export successful",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Error exporting",
                            Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

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
                if (!((position % 2) == 0)){
                    itemView.setBackgroundColor(Color.rgb(205,201,201));
                }
            }
            //find the course to work with and the section
            try{
                //homework name view
                String STUDENTIR = setVal.get(position).split("SEPARATOR")[0];
                String NAME = setVal.get(position).split("SEPARATOR")[1];
                String HOMEWORK = setVal.get(position).split("SEPARATOR")[3];
                String PARTICIPATION = setVal.get(position).split("SEPARATOR")[2];
                /*
                DatabaseHandler db = new DatabaseHandler(this.getContext());
                List<String> listParticipationGrades=db.getTotalParticipationGrades(UUID,currentSection.get_SectionId(),STUDENTIR);
                String PARTICIPATION = "0";
                
                if (listParticipationGrades != null){
                    if (listParticipationGrades.size() > 0){
                        PARTICIPATION = listParticipationGrades.get(0).split("SEPARATOR")[4];
                    }
                }

*/
                TextView StudentId = (TextView)itemView.findViewById(R.id.StudentIdReport);
                StudentId.setText(STUDENTIR);

                TextView StudentName = (TextView)itemView.findViewById(R.id.StudentNameReport);
                StudentName.setText(NAME);

                //Percentage view
                TextView  ParticipationPercentage= (TextView)itemView.findViewById(R.id.ParticipationReport);
                ParticipationPercentage.setText(PARTICIPATION+"%");

                TextView  HomeworkPercentage= (TextView)itemView.findViewById(R.id.HomeworkReport);
                HomeworkPercentage.setText(HOMEWORK+"%");

            }catch(Exception e){
            }

            return itemView;
        }
    }

}
