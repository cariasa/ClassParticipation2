package edu.unitec.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Henry on 12-02-13.
 */
public class SectionActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        loadYears();
        loadCourses();
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }


    /*public void onclickItem(MenuItem item)
    {
    }*/

    public void loadYears()
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        List<String> yearList = new ArrayList<String>();
        for (int a = -3; a <= 3; a++)
        {
            yearList.add(Integer.toString(year + a));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner)findViewById(R.id.spinnerYear)).setAdapter(dataAdapter);
        ((Spinner)findViewById(R.id.spinnerYear)).setSelection(3);
    }

    public void loadCourses()
    {
        List<String> courseList = new DatabaseHandler(this).getAllName_Courses();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courseList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner)findViewById(R.id.spinnerCourse)).setAdapter(dataAdapter);
    }

    public boolean isValidQuarter()
    {
        if ( ((RadioButton)findViewById(R.id.radioButtonQuarter1)).isChecked() &&
             ((RadioButton)findViewById(R.id.radioButtonSemester1)).isChecked() )
        {
            return true;
        }

        if ( ((RadioButton)findViewById(R.id.radioButtonQuarter2)).isChecked() &&
             ((RadioButton)findViewById(R.id.radioButtonSemester1)).isChecked() )
        {
            return true;
        }

        if ( ((RadioButton)findViewById(R.id.radioButtonQuarter3)).isChecked() &&
             ((RadioButton)findViewById(R.id.radioButtonSemester2)).isChecked() )
        {
            return true;
        }

        if ( ((RadioButton)findViewById(R.id.radioButtonQuarter4)).isChecked() &&
             ((RadioButton)findViewById(R.id.radioButtonSemester2)).isChecked() )
        {
            return true;
        }

        return false;
    }

    public void saveSection()
    {
        //If there are no courses
        if ( ((Spinner)findViewById(R.id.spinnerCourse)).getSelectedItemPosition() < 0 )
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Empty Course");
            alert.setMessage("There are no courses");
            alert.setPositiveButton("OK", null);
            alert.show();
        }

        else if ( !isValidQuarter() )
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Invalid Quarter");
            alert.setMessage("The quarter is not valid");
            alert.setPositiveButton("OK", null);
            alert.show();
        }

        else
        {
            int quarter = 0, semester = 0;
            int year = Integer.parseInt(((Spinner)findViewById(R.id.spinnerYear)).getSelectedItem().toString());
            int courseId = ((Spinner)findViewById(R.id.spinnerCourse)).getSelectedItemPosition() + 1;

            //Checks which quarter was selected
            if ( ((RadioButton)findViewById(R.id.radioButtonQuarter1)).isChecked() )
            {
                quarter = 1;
            }

            else if ( ((RadioButton)findViewById(R.id.radioButtonQuarter2)).isChecked() )
            {
                quarter = 2;
            }

            else if ( ((RadioButton)findViewById(R.id.radioButtonQuarter3)).isChecked() )
            {
                quarter = 3;
            }

            else
            {
                quarter = 4;
            }

            //Checks which semester was selected
            if ( ((RadioButton)findViewById(R.id.radioButtonSemester1)).isChecked() )
            {
                semester = 1;
            }

            else
            {
                semester = 2;
            }

            //Database
            SQLiteDatabase db = openOrCreateDatabase("Participation", SQLiteDatabase.CREATE_IF_NECESSARY, null);

            db.execSQL("INSERT INTO section(CourseId, SectionQuarter, SectionSemester, SectionYear) VALUES(" +
            courseId + ", " + quarter + ", " + semester + ", " + year + ")");

            db.close();

            //Show a message
            CharSequence message = "Success!!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getApplicationContext(), message, duration);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.section, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
               onBackPressed();
                return true;
            case R.id.save:
                saveSection();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
