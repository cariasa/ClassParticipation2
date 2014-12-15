package edu.unitec.app;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.facebook.Session;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity{
    private static final int REQUEST_CODE = 2;
    private static final String STATE_YEAR = "YEAR";
    private static final String STATE_SEMESTER = "SEMESTER";
    private static final String STATE_QUARTER = "QUARTER";

    String UUID = "ID1";
    String Name = "name";
    SemesterQuarter Actual;
    RetainDataFragment RetainData;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        UUID = intent.getStringExtra("UUID");
        Name = intent.getStringExtra("Name");
        FragmentManager FragmentM = getFragmentManager();
        RetainData = (RetainDataFragment)FragmentM.findFragmentByTag("RData");
        if (RetainData == null){
            RetainData = new RetainDataFragment();
            FragmentM.beginTransaction().add(RetainData,"RData").commit();
            Actual = new SemesterQuarter();
            RetainData.setData(Actual);
        }
        Actual = RetainData.getData();
        getFragmentManager().beginTransaction()
                .add(R.id.container, new PlaceholderFragment())
                .commit();
        ClickCallback();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        RetainData.setData(Actual);
    }
    @Override
    public void onResume() {
        super.onResume();
        populateListView();
    }

    public void insertTeacher(){
        SQLiteDatabase db = openOrCreateDatabase("Participation", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        final String insertTeacher = "INSERT into Teacher(TeacherUUID, TeacherName, SyncState) VALUES ('" +
                UUID + "', '"+Name+"',1)";
        db.execSQL(insertTeacher);
        db.close();
    }
    public List<Section> getCurrentSectionsList(){
        int year = Actual.getYear();
        int semester = Actual.getSemester();
        int quarter = Actual.getQuarter();
        List<Section> sectionsList = new ArrayList<Section>();
        try{
            SQLiteDatabase db = openOrCreateDatabase("Participation", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            Cursor cursorSectionIdAndCourseId = db.rawQuery("SELECT SectionId, CourseId, SectionCode FROM section WHERE SectionQuarter = " +
                    quarter + " AND SectionYear = " + year + " " +
                    " AND TeacherUUID = '"+UUID+"' AND SyncState <> 3"+
                    " ORDER BY SectionId ASC", null);
            if ( cursorSectionIdAndCourseId.moveToFirst() ){
                do{
                    Section section = new Section(cursorSectionIdAndCourseId.getInt(0), cursorSectionIdAndCourseId.getInt(1),
                            quarter, semester, year, cursorSectionIdAndCourseId.getString(2),UUID);
                    sectionsList.add(section);
                } while ( cursorSectionIdAndCourseId.moveToNext() );
            }
            cursorSectionIdAndCourseId.close();
            db.close();
        }catch (Exception ignored){
        }
        return sectionsList;
    }
    public List<String> getCurrentCoursesNamesList()    {
        List<Section> sectionsList = getCurrentSectionsList();
        List<String> coursesNamesList = new ArrayList<String>();
        SQLiteDatabase db = openOrCreateDatabase("Participation", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        for (Section aSectionsList : sectionsList) {
            Cursor cursorCourseName = db.rawQuery("SELECT CourseName FROM course WHERE CourseId = " +
                    aSectionsList.get_CourseId() + " " +
                    " AND TeacherUUID = '" +UUID +"' AND SyncState <> 3 "+
                    "ORDER BY CourseId ASC", null);
            if (cursorCourseName.moveToFirst()) {
                coursesNamesList.add(cursorCourseName.getString(0));
            }
            cursorCourseName.close();
        }
        db.close();
        return coursesNamesList;
    }
    //creating the listView
    private void populateListView(){
        ArrayAdapter<String> adapter = new MyListAdapter();
        ListView listview = (ListView) findViewById(R.id.listView);
        listview.setAdapter(adapter);
    }
    //event clicking on one item of the list view
    private void ClickCallback(){
        ListView listview = (ListView) findViewById(R.id.listView);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                ListView listview = (ListView) findViewById(R.id.listView);
                Intent intent = new Intent(view.getContext(), StudentActivity.class);
                intent.putExtra("Section", getCurrentSectionsList().get(position));
                intent.putExtra("Course", listview.getItemAtPosition(position).toString());
                intent.putExtra("UUID",UUID);
                intent.putExtra("ACTUAL",Actual);
                startActivity(intent);
            }
        });
    }
    //class myAdapter for my personal style listView
    public class MyListAdapter extends ArrayAdapter<String>{
        public MyListAdapter(){
            super( MainActivity.this, R.layout.item_listview, getCurrentCoursesNamesList());
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent ){
            View itemView = convertView;
            //if itemView is null we create a new one
            if(itemView == null ){
                itemView = getLayoutInflater().inflate(R.layout.item_listview, parent, false);
            }
        //find the course to work with and the section
            try{
                List<Section> sectionsList = getCurrentSectionsList();
            //course name view
                TextView item_name = (TextView)itemView.findViewById(R.id.item_course_name);
                item_name.setText("" + getCurrentCoursesNamesList().get(position));
            //section year view
                TextView item_year = (TextView)itemView.findViewById(R.id.item_year);
                item_year.setText("" + sectionsList.get(position).get_SectionYear());
            //section quarter view
                TextView item_quarter = (TextView)itemView.findViewById(R.id.item_quarter);
                item_quarter.setText("Quarter: " + sectionsList.get(position).get_SectionQuarter());
            //section id view
                TextView item_IdSection = (TextView)itemView.findViewById(R.id.item_idSection);
                item_IdSection.setText("Section Code: " + sectionsList.get(position).get_SectionCode());
            }catch(Exception e){
            }
            return itemView;
        }
    }
    /*public void onclickItem(MenuItem item) {
    switch (item.getItemId()) {
    case R.id.course:
    startActivity(new Intent(this,CourseActivity.class));
    break;
    case R.id.section:
    startActivityForResult(new Intent(this, SectionActivity.class), REQUEST_CODE);
    break;
    case R.id.about:
    AboutDialog dialog = new AboutDialog();
    dialog.show(getFragmentManager(), "dialog_about");
    break;
    }
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE) {
            this.recreate();
        }
            //fb.authorizeCallback(requestCode, resultCode, intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            // getMenuInflater().inflate(R.menu.main, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
        switch ( item.getItemId() ) {
            case R.id.course:
                Intent intent = new Intent(this, CourseActivity.class);
                intent.putExtra("UUID",UUID);
                startActivity(intent);
                return true;
            case R.id.section:
                //startActivityForResult(new Intent(this, SectionActivity.class), REQUEST_CODE);
                Intent intents = new Intent(this, SectionActivity.class);
                intents.putExtra("UUID",UUID);
                startActivity(intents);
                return true;
            case R.id.changesemester:
                HistoricDialog dialogHistoric = new HistoricDialog(Actual);
                dialogHistoric.show(getFragmentManager(),"dialog_changesemester");
                return true;
            case R.id.about:
                AboutDialog dialog = new AboutDialog();
                dialog.show(getFragmentManager(), "dialog_about");
                return true;
            case R.id.logout:
                callFacebookLogout(getBaseContext());
                this.finish();
                //startActivity(new Intent(this, LoginActivity.class));
                return true;
            case R.id.refresh:
                syncSQLiteMySQLDB();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        public PlaceholderFragment() {
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
    public static void callFacebookLogout(Context context) {
        Session session = Session.getActiveSession();
        if (session != null) {
            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
                //set string null;
            }
        } else {
            session = new Session(context);
            Session.setActiveSession(session);
            session.closeAndClearTokenInformation();
        }
    }

    public void syncSQLiteMySQLDB(){
        syncTeacher();
        /*syncCourse();
        syncSection();
        syncStudent();
        syncStudentSection();
        syncParticipationStudent();
        syncHomework();
        syncCriteria();
        syncHomeworkStudent();
        Toast.makeText(getApplicationContext(), "DB Sync completed!", Toast.LENGTH_LONG).show();*/
    }

    public void syncTeacher(){
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        DatabaseHandler controller = new DatabaseHandler(this);
        try{
            controller.forceAddTeacher(UUID, Name);
        }catch(Exception e){

        }
        ArrayList<HashMap<String, String>> teacherList =  controller.getAllTeachers(1);
        if(teacherList.size()!=0){
            params.put("teachersJSON", controller.composeJSONfromSQLiteTeacher(1));
            client.post("http://fia.unitec.edu:8085/part/insertteacher.php", params ,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                   // System.out.println(response);

                    syncCourse();
                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                    // TODO Auto-generated method stub
                    if(statusCode == 404){
                        Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                    }else if(statusCode == 500){
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "No Teacher data to perform Sync action", Toast.LENGTH_LONG).show();
        }
        controller.close();
    }

    public void syncCourse(){
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        DatabaseHandler controller = new DatabaseHandler(this);
        ArrayList<HashMap<String, String>> courseList =  controller.getAllCourse(1);
        if(courseList.size()!=0){
            params.put("coursesJSON", controller.composeJSONfromSQLiteCourse(1));
            client.post("http://fia.unitec.edu:8085/part/insertcourse.php", params ,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                   // System.out.println(response);
                    syncSection();
                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                    // TODO Auto-generated method stub
                    if(statusCode == 404){
                        Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                    }else if(statusCode == 500){
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            //Toast.makeText(getApplicationContext(), "No Course data to perform Sync action", Toast.LENGTH_LONG).show();
        }
        controller.close();
    }

    public void syncSection(){
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        DatabaseHandler controller = new DatabaseHandler(this);
        ArrayList<HashMap<String, String>> sectionList =  controller.getAllSection(1);
        if(sectionList.size()!=0){
            params.put("sectionsJSON", controller.composeJSONfromSQLiteSection(1));
            client.post("http://fia.unitec.edu:8085/part/insertsection.php", params ,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                   // System.out.println(response);
                    syncStudent();
                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                    // TODO Auto-generated method stub
                    if(statusCode == 404){
                        Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                    }else if(statusCode == 500){
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            //Toast.makeText(getApplicationContext(), "No Course data to perform Sync action", Toast.LENGTH_LONG).show();
        }
        controller.close();
    }

    public void syncStudent(){
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        DatabaseHandler controller = new DatabaseHandler(this);
        ArrayList<HashMap<String, String>> studentList =  controller.getAllStudents(1);
        if(studentList.size()!=0){
            params.put("studentsJSON", controller.composeJSONfromSQLiteStudent(1));
            client.post("http://fia.unitec.edu:8085/part/insertstudent.php", params ,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                   // System.out.println(response);

                    syncStudentSection();
                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                    // TODO Auto-generated method stub
                    if(statusCode == 404){
                        Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                    }else if(statusCode == 500){
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            //Toast.makeText(getApplicationContext(), "No Course data to perform Sync action", Toast.LENGTH_LONG).show();
        }
        controller.close();
    }

    public void syncStudentSection(){
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        DatabaseHandler controller = new DatabaseHandler(this);
        ArrayList<HashMap<String, String>> studentsectionList =  controller.getAllStudentSection(1);
        if(studentsectionList.size()!=0){
            params.put("studentSectionsJSON", controller.composeJSONfromSQLiteStudentSection(1));
            client.post("http://fia.unitec.edu:8085/part/insertstudentsection.php", params ,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    //System.out.println(response);

                    syncParticipationStudent();
                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                    // TODO Auto-generated method stub
                    if(statusCode == 404){
                        Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                    }else if(statusCode == 500){
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            //Toast.makeText(getApplicationContext(), "No Course data to perform Sync action", Toast.LENGTH_LONG).show();
        }
        controller.close();
    }

    public void syncParticipationStudent(){
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        DatabaseHandler controller = new DatabaseHandler(this);
        ArrayList<HashMap<String, String>> studentsectionList =  controller.getAllStudentParticipation(1);
        if(studentsectionList.size()!=0){
            params.put("studentparticipationJSON", controller.composeJSONfromSQLiteStudentParticipation(1));
            client.post("http://fia.unitec.edu:8085/part/insertstudentparticipation.php", params ,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    //System.out.println(response);

                    syncHomework();
                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                    // TODO Auto-generated method stub
                    if(statusCode == 404){
                        Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                    }else if(statusCode == 500){
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            //Toast.makeText(getApplicationContext(), "No Course data to perform Sync action", Toast.LENGTH_LONG).show();
        }
        controller.close();
    }

    public void syncHomework(){
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        DatabaseHandler controller = new DatabaseHandler(this);
        ArrayList<HashMap<String, String>> studentsectionList =  controller.getAllHomework(1);
        if(studentsectionList.size()!=0){
            params.put("homeworkJSON", controller.composeJSONfromSQLiteHomework(1));
            client.post("http://fia.unitec.edu:8085/part/inserthomework.php", params ,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    //System.out.println(response);

                    syncCriteria();
                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                    // TODO Auto-generated method stub
                    if(statusCode == 404){
                        Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                    }else if(statusCode == 500){
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            //Toast.makeText(getApplicationContext(), "No Course data to perform Sync action", Toast.LENGTH_LONG).show();
        }
        controller.close();
    }

    public void syncCriteria(){
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        DatabaseHandler controller = new DatabaseHandler(this);
        ArrayList<HashMap<String, String>> studentsectionList =  controller.getAllCriteria(1);
        if(studentsectionList.size()!=0){
            params.put("criteriaJSON", controller.composeJSONfromSQLiteCriteria(1));
            client.post("http://fia.unitec.edu:8085/part/insertcriteria.php", params ,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    //System.out.println(response);

                    syncHomeworkStudent();
                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                    // TODO Auto-generated method stub
                    if(statusCode == 404){
                        Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                    }else if(statusCode == 500){
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            //Toast.makeText(getApplicationContext(), "No Course data to perform Sync action", Toast.LENGTH_LONG).show();
        }
        controller.close();
    }

    public void syncHomeworkStudent(){
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        final DatabaseHandler controller = new DatabaseHandler(this);
        ArrayList<HashMap<String, String>> studentsectionList =  controller.getAllHomeWorkStudent(1);
        if(studentsectionList.size()!=0){
            params.put("homeworkstudentJSON", controller.composeJSONfromSQLiteHomeWorkStudent(1));
            client.post("http://fia.unitec.edu:8085/part/inserthomeworkstudent.php", params ,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    //System.out.println(response);
                    controller.clearSyncState();
                    Toast.makeText(getApplicationContext(), "DB Sync completed!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                    // TODO Auto-generated method stub
                    if(statusCode == 404){
                        Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                    }else if(statusCode == 500){
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            //Toast.makeText(getApplicationContext(), "No Course data to perform Sync action", Toast.LENGTH_LONG).show();
        }
        controller.close();
    }
}