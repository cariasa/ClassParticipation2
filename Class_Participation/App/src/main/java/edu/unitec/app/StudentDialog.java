package edu.unitec.app;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ariel on 12-14-13.
 */
public class StudentDialog extends DialogFragment
{
    List<Participation> studentParticipationList;
    String studentName;
    double finalGrade;

    StudentDialog(List<Participation> list, String studentName, double finalGrade)
    {
        studentParticipationList = list;
        this.studentName = studentName;
        this.finalGrade = finalGrade/list.size();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_student, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(studentName + " (" + Double.toString((double)Math.round(finalGrade*100)/100) + "%)");
        builder.setView(view);
        builder.setPositiveButton("OK", null);

        //-----------------------------------------------------------------------------------------

        TableLayout tableLayout = (TableLayout)view.findViewById(R.id.tableLayout);

        TableRow tableRowHead = new TableRow(view.getContext());

        TextView textViewGrade = new TextView(view.getContext());
        textViewGrade.setText("Grade");
        textViewGrade.setPadding(5, 5, 35, 5);
        tableRowHead.addView(textViewGrade);

        TextView textViewDate = new TextView(view.getContext());
        textViewDate.setText("Date");
        textViewDate.setPadding(5, 5, 30, 5);
        tableRowHead.addView(textViewDate);

        TextView textViewComment = new TextView(view.getContext());
        textViewComment.setText("Comment");
        textViewComment.setPadding(30, 5, 5, 5);
        tableRowHead.addView(textViewComment);

        tableLayout.addView(tableRowHead);

        for (int a = 0; a < studentParticipationList.size(); a++)
        {
            TableRow tableRow = new TableRow(view.getContext());

            TextView textViewGrade2 = new TextView(view.getContext());
            textViewGrade2.setText(Double.toString(studentParticipationList.get(a).get_ParticipationGrade()));
            textViewGrade2.setPadding(5, 10, 35, 10);
            tableRow.addView(textViewGrade2);

            TextView textViewDate2 = new TextView(view.getContext());
            textViewDate2.setText(studentParticipationList.get(a).get_ParticipationDate());
            textViewDate2.setPadding(5, 10, 30, 10);
            tableRow.addView(textViewDate2);

            TextView textViewComment2 = new TextView(view.getContext());
            textViewComment2.setText(studentParticipationList.get(a).get_ParticipationComment());
            textViewComment2.setPadding(30, 10, 5, 10);
            tableRow.addView(textViewComment2);

            tableLayout.addView(tableRow);
        }

        return builder.create();
    }
}
