package edu.unitec.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Henry on 12-08-13.
 */
public class StudentActivity extends Activity{

    public Menu menu;
    final int ACTIVITY_CHOOSE_FILE = 1;
    private Section currentSection = new Section();
    private String UUID;
    private boolean Previous;

    public Section getCurrentSection() {
        return currentSection;
    }

    StudentItemAdapter arrayAdapter;
    //List<String> listViewStudentNameList;
    ArrayList<StudentItem> arrayStudentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        Intent intent = getIntent();
        currentSection = (Section)intent.getSerializableExtra("Section");
        String course_name = intent.getStringExtra("Course");
        UUID = intent.getStringExtra("UUID");
        Previous = intent.getBooleanExtra("PREVIOUS",false);


        setTitle(course_name);

        //------------------Connect ArrayAdapter with a List for the ListView----------------------

        /*listViewStudentNameList = getCurrentStudentNamesList();

        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listViewStudentNameList);

        ((ListView)findViewById(R.id.listView_student)).setAdapter(arrayAdapter);*/

        ListView studentsList = (ListView) findViewById(R.id.listView_student);
        arrayStudentItem = new ArrayList<StudentItem>();
        for(int i=0;i<getCurrentStudentNamesList().size();i++){
            StudentItem studentitem = new StudentItem (getCurrentStudentNamesList().get(i));
            arrayStudentItem.add(studentitem);
        }
        arrayAdapter = new StudentItemAdapter(this,arrayStudentItem,UUID);
        studentsList.setAdapter(arrayAdapter);
        //------------------------------------------------------------------------------------------

        actionBar();
        ClickCallback();
    }

    public void actionBar(){
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student, menu);
        this.menu=menu;
        MenuItem item_statistics = menu.findItem(R.id.item_statistics);
        MenuItem item_student = menu.findItem(R.id.item_Student);
        MenuItem save_student = menu.findItem(R.id.item_addStudent);
        MenuItem save_students = menu.findItem(R.id.save_students);
        MenuItem delete_student = menu.findItem(R.id.delete_students);
        MenuItem newAssignment = menu.findItem(R.id.item_newAssignment);
        MenuItem newHomework = menu.findItem(R.id.item_newHomework);
        MenuItem newParticipation = menu.findItem(R.id.item_newParticipation);
        if(!getCurrentStudentNamesList().isEmpty() ){
            item_statistics.setVisible(true);
            if (!Previous) {
                item_student.setVisible(true);
                save_student.setVisible(true);
                save_students.setVisible(false);
                newAssignment.setVisible(true);
                newHomework.setVisible(true);
                newParticipation.setVisible(true);
                delete_student.setVisible(true);
            }
        }else{
            item_statistics.setVisible(false);
            if (!Previous) {
                item_student.setVisible(true);
                save_student.setVisible(true);
                save_students.setVisible(true);
                newAssignment.setVisible(false);
                newHomework.setVisible(false);
                newParticipation.setVisible(false);
                delete_student.setVisible(false);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //startActivity(new Intent(this,MainActivity.class));
        switch (item.getItemId()) {
            case R.id.item_addStudent:
                showAddStudentDialog();
                return true;
            case  R.id.save_students:
                Intent chooseFile;
                Intent intent;
                chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("file/*");
                intent = Intent.createChooser(chooseFile, "Choose File");
                startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
                return true;
            case R.id.item_newParticipation:
                showParticipationDialog();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.delete_students:
                showDeleteStudentDialog();
                return true;
            case  R.id.export_students:
                try {
                    ReadWriteFileManager fm = new ReadWriteFileManager();
                    DatabaseHandler db = new DatabaseHandler(this);
                    List<String> grades = db.exportStudentGrades(UUID);
                    String filepath = fm.exportStudentGrades(grades);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setType("file/*");
                    sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filepath)));
                    startActivity(Intent.createChooser(sendIntent, "Export"));
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            case R.id.item_newHomework:
                Intent intentHomework = new Intent(this, HomeworkActivity.class);
                intentHomework.putExtra("Section", currentSection);
                intentHomework.putExtra("isCreating",true);
                intentHomework.putExtra("UUID",UUID);
                startActivity(intentHomework);
                return true;
            case R.id.item_statistics:
                showStatisticsDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    /*public void onclickItem(MenuItem item) {

    }*/

    public void update(){
        this.recreate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case ACTIVITY_CHOOSE_FILE:
                if (resultCode == RESULT_OK){
                    try{
                        Uri uri = data.getData();
                        String filePath = uri.getPath();
                        Log.i("path", filePath);

                        //reading the path of the file
                        ReadWriteFileManager file = new ReadWriteFileManager();
                        List<Student> student = file.readFromFile(this, filePath);

                       /* List<Integer> list1 = getAllStudentIDList();
                        List<Integer> list2 = getCurrentStudentIdList();*/

                        DatabaseHandler bd = new DatabaseHandler(this);
                        //validate
                        if( getCurrentStudentNamesList().isEmpty() ){

                           // if( !list1.containsAll( list2 ) ){
                                for (Student aStudent : student) {
                                    bd.addStudent(aStudent);
                                    bd.addStudentTable(aStudent, currentSection);
                                }
                                this.recreate();
                           /* }else{
                                for (Student aStudent : student) {
                                    bd.addStudentTable(aStudent, currentSection);
                                }
                                this.recreate();
                                Log.i("student","students list already exist");
                            }*/
                        }
                    }catch(Exception ignored){
                    }
                }
                break;
        }
    }
/*
    public List<Integer> getAllStudentIDList()
    {
        List<Integer> StudentList = new ArrayList<Integer>();
        try
        {
            SQLiteDatabase db = openOrCreateDatabase("Participation", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            Cursor cursorStudent = db.rawQuery("SELECT StudentId FROM student ORDER BY StudentId ASC", null);
            if ( cursorStudent.moveToFirst() )
            {
                do
                {
                    StudentList.add(cursorStudent.getInt(0));
                } while ( cursorStudent.moveToNext() );
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return StudentList;
    }
*/
    public List<Integer> getCurrentStudentSectionIdList(){
        List<Integer> StudentSectionIdList = new ArrayList<Integer>();

        try
        {
            SQLiteDatabase db = openOrCreateDatabase("Participation", SQLiteDatabase.CREATE_IF_NECESSARY, null);

            Cursor cursorStudentSectionId = db.rawQuery("SELECT StudentSectionId FROM studentSection " +
                    "WHERE SectionId = " +
                    currentSection.get_SectionId() + " " +
                    "AND TeacherUUID = '" + UUID +"'"+
                    " ORDER BY SectionId ASC", null);

            if ( cursorStudentSectionId.moveToFirst() ){
                do
                {
                    StudentSectionIdList.add(cursorStudentSectionId.getInt(0));

                } while ( cursorStudentSectionId.moveToNext() );
            }

            db.close();
        }

        catch(Exception e){
            e.printStackTrace();
        }

        return StudentSectionIdList;
    }

    public List<Integer> getCurrentStudentIdList()
    {
        List<Integer> StudentIdList = new ArrayList<Integer>();
        try
        {
            SQLiteDatabase db = openOrCreateDatabase("Participation", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            Cursor cursorSectionId = db.rawQuery("SELECT StudentId FROM studentSection WHERE SectionId = " +
                    currentSection.get_SectionId() + " " +
                    "AND TeacherUUID = '" + UUID +"'"+
                    " ORDER BY SectionId ASC", null);
            if ( cursorSectionId.moveToFirst() )
            {
                do
                {
                    StudentIdList.add(cursorSectionId.getInt(0));

                } while ( cursorSectionId.moveToNext() );
            }

            db.close();

        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
        return StudentIdList;
    }

    public boolean check_absence(int index_StudentSectionIDList){
        List<Integer> studentSectionIdList = getCurrentStudentSectionIdList();
        SQLiteDatabase db = openOrCreateDatabase("Participation", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        Cursor absent_check = db.rawQuery("SELECT ParticipationComment, ParticipationDate FROM participationStudent " +
                "WHERE StudentSectionId = "
                + studentSectionIdList.get(index_StudentSectionIDList) + " " +
                "AND TeacherUUID = '" + UUID +"'"+
                " ORDER BY ParticipationId DESC LIMIT 1", null);

        Date cDate = new Date();
        String fDate = new SimpleDateFormat("dd-MM-yyyy").format(cDate);

        absent_check.moveToFirst();
        if(absent_check.getCount()==0){
            return false;
        }
        if(absent_check.getString(0).equals("Absent") && absent_check.getString(1).equals(fDate)){
            return true;
        }
        return false;
    }

    public List<String> getCurrentStudentNamesList(){
        List<Integer> studentId = getCurrentStudentIdList();
        List<String> studentNamesList = new ArrayList<String>();
        SQLiteDatabase db = openOrCreateDatabase("Participation", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        for (Integer aStudentId : studentId) {
            Cursor cursorStudentName = db.rawQuery("SELECT StudentName FROM student WHERE StudentId = " +
                    aStudentId +
                    " ORDER BY StudentId ASC", null);

            if (cursorStudentName.moveToFirst()) {
                studentNamesList.add(cursorStudentName.getString(0));
            }
            cursorStudentName.close();
        }
        db.close();
        return studentNamesList;
    }

    public int getMinValueIndex(int[] array){
        int minValue = array[0];
        int index = 0;
        int i;

        for(i = 1; i < array.length; i++)
        {
            if(array[i] < minValue)
            {
                minValue = array[i];
                index = i;
            }
        }
        return index;
    }

    public int selectStudent(){
        List<Integer> studentSectionIdList = getCurrentStudentSectionIdList();
        int studentSectionIdCounters[] = new int[studentSectionIdList.size()];

        SQLiteDatabase db = openOrCreateDatabase("Participation", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        for (int a = 0; a < studentSectionIdList.size(); a++){
            Cursor cursor = db.rawQuery("SELECT * FROM participationStudent WHERE StudentSectionId = " +
                    studentSectionIdList.get(a)+
                    " AND TeacherUUID = '" + UUID +"'", null);
            studentSectionIdCounters[a] = cursor.getCount();
            cursor.close();
        }

        db.close();

        //---------------------------------------------------------------------------------------------

        int studentIndex = 0;

        Random random = new Random();
        int randomValue = random.nextInt(6 - 1) + 1;

        //Less student participation
        if ( ( randomValue == 1 ) || ( randomValue == 2 ) || ( randomValue == 3 ) ){
            studentIndex = getMinValueIndex(studentSectionIdCounters);
        }else{//Random student
            studentIndex = random.nextInt(studentSectionIdList.size());
        }
        return studentIndex;
    }
    public void showStatisticsDialog(){
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            String courseName=db.getCourseName(currentSection.get_CourseId(),UUID);
            StatisticsDialog dialog = new StatisticsDialog(courseName,currentSection.get_SectionId(),UUID);
            dialog.show(getFragmentManager(), "dialog_statistics");
    }

    public void showParticipationDialog(){
        int studentIndex;
        int student_quantity = getCurrentStudentNamesList().size();
        if (student_quantity > 0 ){
            int avoid_infinite_loop=0;
            do{
                studentIndex = selectStudent();
                avoid_infinite_loop++;
                if(avoid_infinite_loop == 100){
                    Context context = getApplicationContext();
                    CharSequence text = "All students absent!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                }
            }while(check_absence(studentIndex));

            ParticipationDialog dialog = new ParticipationDialog(getCurrentStudentSectionIdList().get(studentIndex),
                    getCurrentStudentNamesList().get(studentIndex),UUID);

            dialog.show(getFragmentManager(), "dialog_participation");
        }
    }
    //Participates using the index of the selected item
    public void showParticipationDialog(int listIndex){
        if ( getCurrentStudentNamesList().size() > 0 ){
            int studentIndex = listIndex;
            ParticipationDialog dialog = new ParticipationDialog(getCurrentStudentSectionIdList().get(studentIndex),
                    getCurrentStudentNamesList().get(studentIndex),UUID);
            dialog.show(getFragmentManager(), "dialog_participation");
        }
    }


    public void showAddStudentDialog(){
        AddStudentDialog dialog = new AddStudentDialog(currentSection, arrayAdapter, arrayStudentItem,UUID);
        dialog.show(getFragmentManager(), "dialog_addstudent");
    }

    public void showDeleteStudentDialog(){
        DeleteStudentDialog dialog = new DeleteStudentDialog(currentSection, arrayAdapter, arrayStudentItem,getCurrentStudentIdList());
        dialog.show(getFragmentManager(), "dialog_deletestudent");

    }

    //event clicking on one item of the list view
    private void ClickCallback(){
        ListView listview = (ListView) findViewById(R.id.listView_student);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                List<Participation> currentStudentParticipationList = new ArrayList<Participation>();
                List<String> currentStudentHomeworks = new ArrayList<String>();
                double finalGrade = 0;
                try{
                    int currentStudentSectionId = getCurrentStudentSectionIdList().get(position);
                    DatabaseHandler db = new DatabaseHandler(view.getContext());
                    currentStudentParticipationList = db.getStudentParticipationList(currentStudentSectionId,UUID);
                    finalGrade = db.getFinalGrade(currentStudentSectionId,UUID);
                    currentStudentHomeworks = db.getHomeworkNameAndGrade(getCurrentStudentIdList().get(position),currentStudentSectionId,UUID);
                    double percentageParticipations=0,percentageHomeworks=0, acumHomeworks=0, acumParticipations=0;

                    //get the average of the Homeworks
                    for(int i=0;i<currentStudentHomeworks.size();i++){
                        acumHomeworks+=Double.parseDouble(currentStudentHomeworks.get(i).split("HOLAHELLO")[1]);
                    }
                    percentageHomeworks=acumHomeworks/currentStudentHomeworks.size();

                    //get the average of participations
                    for(int i=0;i<currentStudentParticipationList.size();i++){
                        acumParticipations+=currentStudentParticipationList.get(i).get_ParticipationGrade();
                    }
                    percentageParticipations=acumParticipations/currentStudentParticipationList.size();

                    db.close();
                    StudentDialog dialog = new StudentDialog(currentStudentParticipationList, getCurrentStudentNamesList().get(position), finalGrade, currentStudentHomeworks,percentageParticipations,percentageHomeworks);
                    dialog.show(getFragmentManager(), "dialog_student");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
