package edu.unitec.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ariel on 12-14-13.
 */
public class ParticipationDialog extends DialogFragment
{
    int studentSectionId;
    String studentName;

    ParticipationDialog(int studentSectionId, String studentName)
    {
        this.studentSectionId = studentSectionId;
        this.studentName = studentName;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_participation, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(studentName);
        builder.setView(view);

        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                int grade = 0;

                if ( ((RatingBar)view.findViewById(R.id.ratingBar)).getRating() == 1 )
                {
                    grade = 20;
                }

                else if ( ((RatingBar)view.findViewById(R.id.ratingBar)).getRating() == 2 )
                {
                    grade = 40;
                }

                else if ( ((RatingBar)view.findViewById(R.id.ratingBar)).getRating() == 3 )
                {
                    grade = 60;
                }

                else if ( ((RatingBar)view.findViewById(R.id.ratingBar)).getRating() == 4 )
                {
                    grade = 80;
                }

                else if ( ((RatingBar)view.findViewById(R.id.ratingBar)).getRating() == 5 )
                {
                    grade = 100;
                }


                try
                {
                    //-------------------------Save the participation & update final note-----------------------------------

                    String date = new SimpleDateFormat("dd-MM-yyy").format(new Date());
                    String comment = ((EditText)view.findViewById(R.id.editTextComment)).getText().toString();

                    //Database
                    DatabaseHandler db = new DatabaseHandler(view.getContext());
                    db.addParticipation(new Participation(studentSectionId,grade,date,comment));
                    double studentSectionFinal = db.getFinalGrade(studentSectionId) + grade;
                    db.UpdateparticipationStudent(studentSectionId, studentSectionFinal);
                    db.close();
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
                ParticipationDialog.this.getDialog().cancel();
            }
        });

        return builder.create();
    }
}
