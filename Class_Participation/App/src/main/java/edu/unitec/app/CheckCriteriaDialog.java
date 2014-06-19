package edu.unitec.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Alberto on 15/06/2014.
 */
public class CheckCriteriaDialog extends DialogFragment {
    int studentId;
    Criteria currentCriteria;
    ArrayAdapter arrayAdapter;

    public CheckCriteriaDialog(int studentId, Criteria currentCriteria,ArrayAdapter arrayAdapter) {
        this.studentId = studentId;
        this.currentCriteria = currentCriteria;
        this.arrayAdapter =arrayAdapter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_checkcriteria, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(currentCriteria.getCriteriaName());
        builder.setView(view);

        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                double grade = 0;

                if ( ((RatingBar)view.findViewById(R.id.ratingBarCriteria)).getRating() == 1 )
                {
                    grade = 20;
                }

                else if ( ((RatingBar)view.findViewById(R.id.ratingBarCriteria)).getRating() == 2 )
                {
                    grade = 40;
                }

                else if ( ((RatingBar)view.findViewById(R.id.ratingBarCriteria)).getRating() == 3 )
                {
                    grade = 60;
                }

                else if ( ((RatingBar)view.findViewById(R.id.ratingBarCriteria)).getRating() == 4 )
                {
                    grade = 80;
                }

                else if ( ((RatingBar)view.findViewById(R.id.ratingBarCriteria)).getRating() == 5 )
                {
                    grade = 100;
                }


                try
                {
                    //-------------------------Save the homework & update final note-----------------------------------
                    //Database
                    DatabaseHandler db = new DatabaseHandler(view.getContext());
                    //if already exist
                    if(db.homeworkStudentExist(currentCriteria.getCriteriaId(),studentId)){
                        //get the ID of the table
                        int homeworkStudentId=db.getHomeworkStudentId(currentCriteria.getCriteriaId(),studentId);
                        //update the Grade
                        db.updateHomeworkStudent(homeworkStudentId,grade);
                    }else{//if doesn't exist
                        //add
                        db.addHomeworkStudent(grade, currentCriteria.getCriteriaId(), studentId);
                        db.close();
                    }
                    arrayAdapter.notifyDataSetChanged();

                }

                catch (Exception e)
                {
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                CheckCriteriaDialog.this.getDialog().cancel();
            }
        });

        return builder.create();
    }
}
