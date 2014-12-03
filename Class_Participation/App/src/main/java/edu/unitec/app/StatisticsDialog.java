package edu.unitec.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class StatisticsDialog extends DialogFragment
{
    String courseName;
    int sectionId;
    String UUID;
    StatisticsDialog(String courseName,int sectionId, String UUID){
        this.courseName=courseName;
        this.sectionId=sectionId;
        this.UUID = UUID;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_statistics, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(courseName);
        builder.setView(view);
        builder.setPositiveButton("OK", null);
        DatabaseHandler db=new DatabaseHandler(getActivity());
        //------------------------Participations--------------------------------------------------------
        TextView txtParticipations=(TextView)view.findViewById(R.id.textViewParticipations);
        TextView txtAverageSection=(TextView)view.findViewById(R.id.textViewAverageSection);
        TextView txtHighestAverage=(TextView)view.findViewById(R.id.textViewHighestAverage);
        TextView txtLowestAverage=(TextView)view.findViewById(R.id.textViewLowestAverage);
        double avgSection=0,countParticipation=0,acumParticipation=0,avgMax=0,avgMin=0;
        List<Participation> participationList=db.getParticipationList(sectionId,UUID);
        countParticipation=participationList.size();
        //set de quantity of Participations
        txtParticipations.setText(""+Math.round(countParticipation));
        //get the average of the section
        for(int i=0;i<countParticipation;i++){
            acumParticipation+=participationList.get(i).get_ParticipationGrade();
        }
        avgSection=acumParticipation/countParticipation;
        txtAverageSection.setText(Math.round(avgSection)+"%");
        //get the highest average
        txtHighestAverage.setText(db.getMaxAverageStudentParticipation(sectionId,UUID));
        //get the lowest average
        txtLowestAverage.setText(db.getMinAverageStudentParticipation(sectionId,UUID));


        //------------------------Homeworks-------------------------------------------------------------
        TextView txtHomeworks=(TextView)view.findViewById(R.id.textViewHomeworks);
        TextView txtAverageSectionHW=(TextView)view.findViewById(R.id.textViewAverageSectionHW);
        TextView txtHighestAverageHW=(TextView)view.findViewById(R.id.textViewHighestAverageHW);
        TextView txtLowestAverageHW=(TextView)view.findViewById(R.id.textViewLowestAverageHW);
        double avgSectionHW=0,countHomework=0,acumHomework=0,avgMaxHW=0,avgMinHW=0;
        List<Homework> homeworkList=db.getAll_Homework(sectionId,UUID);
        countHomework=homeworkList.size();
        //set the quantity of homeworks
        txtHomeworks.setText(""+Math.round(countHomework));
        //internal class to get the studentName and average
        class StudentAndAverage{
            String name;
            Double average;
            StudentAndAverage(String name,Double avg){
                this.name=name;
                this.average=avg;
            }
            StudentAndAverage(){
                name="";
                average=0.0;
            }
        }
        double homeworksByStudent=0;
        int quantityOfHomeworksByStudent=0;
        List<StudentAndAverage> nameAndAverageList=new ArrayList<StudentAndAverage>();
        //get the average of the section
        List<Integer> studentIdList=db.getStudentIdListBySectionId(sectionId,UUID);
        for(int i=0;i<studentIdList.size();i++){
            List<String> homeworkNameAndGradeList=db.getHomeworkNameAndGrade(studentIdList.get(i),sectionId,UUID);
            homeworksByStudent=0;
            quantityOfHomeworksByStudent=0;
            for(int j=0;j<homeworkNameAndGradeList.size();j++){
                acumHomework+=Double.parseDouble(homeworkNameAndGradeList.get(j).split("HOLAHELLO")[1]);
                homeworksByStudent+=Double.parseDouble(homeworkNameAndGradeList.get(j).split("HOLAHELLO")[1]);
                quantityOfHomeworksByStudent++;
            }
            nameAndAverageList.add(new StudentAndAverage(db.getStudentName(studentIdList.get(i)),(homeworksByStudent/quantityOfHomeworksByStudent)));
        }
        //set the average of the section
        avgSectionHW=Math.round(acumHomework/(studentIdList.size()*countHomework));
        txtAverageSectionHW.setText(avgSectionHW+"%");

        //get the highest average with nameAndAverageList
        if(nameAndAverageList.size()>0) {
            double max = 0;
            String maxName = nameAndAverageList.get(0).name;
            for (int i = 0; i < nameAndAverageList.size(); i++) {
                if (nameAndAverageList.get(i).average > max) {
                    max = nameAndAverageList.get(i).average;
                    maxName = nameAndAverageList.get(i).name;
                }
            }
            txtHighestAverageHW.setText(maxName + "\t" + Math.round(max) + "%");
        }else{
            txtHighestAverageHW.setText("N/A");
        }
        //get the Lowest average with nameAndAverageList
        if(nameAndAverageList.size()>0) {
            double min = nameAndAverageList.get(0).average;
            String minName = nameAndAverageList.get(0).name;
            for (int i = 0; i < nameAndAverageList.size(); i++) {
                if (nameAndAverageList.get(i).average < min) {
                    min = nameAndAverageList.get(i).average;
                    minName = nameAndAverageList.get(i).name;
                }
            }
            txtLowestAverageHW.setText(minName + "\t" + Math.round(min) + "%");
        }else{
            txtLowestAverageHW.setText("N/A");
        }
        db.close();
        return builder.create();
    }
}
