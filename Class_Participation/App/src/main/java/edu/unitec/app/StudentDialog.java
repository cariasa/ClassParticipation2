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

import java.util.ArrayList;
import java.util.List;


public class StudentDialog extends DialogFragment
{
    List<Participation> studentParticipationList;
    String studentName;
    double finalGrade;
    double percentageParticipation;
    double percentageHomework;
    List<String> homeworks;

    StudentDialog(List<Participation> list, String studentName, double finalGrade, List<String> homework,double percentageParticipation, double percentageHomework){
        if (list != null) {
            studentParticipationList = list;
        }else{
            studentParticipationList = new ArrayList();
        }
        this.studentName = studentName;
       // this.finalGrade = finalGrade/list.size();
        if (homework != null) {
            this.homeworks = homework;
        }else{
            this.homeworks = new ArrayList();
        }
        this.percentageHomework=percentageHomework;
        this.percentageParticipation=percentageParticipation;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_student, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(studentName);
        builder.setView(view);
        builder.setPositiveButton("OK", null);

        //------------------------Participations-------------------------------------------------------------

        TableLayout tableLayout = (TableLayout)view.findViewById(R.id.tableLayout);

        TableRow tableRowHead = new TableRow(view.getContext());
        //Set the average of participations
        TextView textViewParticipationsTitle=(TextView)view.findViewById(R.id.textViewParticipationsTitle);
        textViewParticipationsTitle.setText("Participations "+Math.round(percentageParticipation)+"%");

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

        for (int a = 0; a < studentParticipationList.size(); a++){
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

        //------------------------Homeworks-------------------------------------------------------------
        TableLayout tableLayout2 = (TableLayout)view.findViewById(R.id.tableLayout2);

        TableRow tableRowHeadHW = new TableRow(view.getContext());
        //set the average of Homeworks
        TextView textViewHomeworksTitle=(TextView)view.findViewById(R.id.textViewHomeworksTitle);
        textViewHomeworksTitle.setText("Homeworks "+Math.round(percentageHomework)+"%");

        TextView textViewGradeHW = new TextView(view.getContext());
        textViewGradeHW.setText("Grade");
        textViewGradeHW.setPadding(5, 5, 35, 5);
        tableRowHeadHW.addView(textViewGradeHW);


        TextView textViewNameHW = new TextView(view.getContext());
        textViewNameHW.setText("Name");
        textViewNameHW.setPadding(5, 5, 30, 5);
        tableRowHeadHW.addView(textViewNameHW);

        tableLayout2.addView(tableRowHeadHW);

        for (int a = 0; a < homeworks.size(); a++){

            String[] homework_parts = (homeworks.get(a)).split("HOLAHELLO");
            TableRow tableRowHW = new TableRow(view.getContext());

            TextView textViewGradeHW2 = new TextView(view.getContext());
            textViewGradeHW2.setText(Math.round(Double.parseDouble(homework_parts[1]))+"%");
            textViewGradeHW2.setPadding(5, 10, 35, 10);
            tableRowHW.addView(textViewGradeHW2);

            TextView textViewNameHW2 = new TextView(view.getContext());
            textViewNameHW2.setText(homework_parts[0]);
            textViewNameHW2.setPadding(5, 10, 30, 10);
            tableRowHW.addView(textViewNameHW2);

            tableLayout2.addView(tableRowHW);
        }

        return builder.create();
    }
}
