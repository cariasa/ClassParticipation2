package edu.unitec.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 08/06/2014.
 */
public class StudentItemAdapter extends BaseAdapter {

    protected Context activity;
    protected ArrayList<StudentItem> items;
    private String UUID;
    private boolean Previous;

    public StudentItemAdapter(Context activity, ArrayList<StudentItem> items,String UUID,boolean Previous) {
        this.activity = activity;
        this.items = items;
        this.UUID = UUID;
        this.Previous = Previous;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getStudentItemId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View customView = convertView;
        if(convertView == null){
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            customView = inf.inflate(R.layout.layout_item_student, null);
        }
        //Creates an object StudentItem
        StudentItem dir = items.get(position);
        //Fills the name
        final TextView StudentName = (TextView) customView.findViewById(R.id.item_studentName);
        StudentName.setText(dir.getStudentName());
        //Override the method onClick from buttonParticipation
        Button buttonParticipation = (Button) customView.findViewById(R.id.button_student_participation);
        Button buttonHomework = (Button) customView.findViewById(R.id.button_student_homework);
        if (!Previous) {
            buttonParticipation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity instanceof StudentActivity) {
                        ((StudentActivity) activity).showParticipationDialog(position);
                    }
                }
            });

            //Override onClick method from buttonHomework

            buttonHomework.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity instanceof StudentActivity) {
                        ListView listview = (ListView) ((StudentActivity) activity).findViewById(R.id.listViewCheckHomework);
                        Intent intent = new Intent(activity.getApplicationContext(), CheckHomework.class);
                        intent.putExtra("Section", (int) (((StudentActivity) activity).getCurrentSection().get_SectionId()));
                        intent.putExtra("StudentName", ((StudentActivity) activity).getCurrentStudentNamesList().get(position));
                        intent.putExtra("studentId", ((StudentActivity) activity).getCurrentStudentIdList().get(position));
                        intent.putExtra("UUID", UUID);
                        activity.startActivity(intent);
                    }
                }
            });
        }else{
            buttonParticipation.setVisibility(Button.INVISIBLE);
            buttonHomework.setVisibility(Button.INVISIBLE);
        }
        //Override onClick method from buttonStatistics
        Button buttonStatistics = (Button) customView.findViewById(R.id.button_student_statistics);
        buttonStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( activity instanceof StudentActivity){
                    List<Participation> currentStudentParticipationList = new ArrayList<Participation>();
                    List<String> currentStudentHomeworks = new ArrayList<String>();
                    double finalGrade = 0;
                    try{
                        int currentStudentSectionId = ((StudentActivity)activity).getCurrentStudentSectionIdList().get(position);
                        DatabaseHandler db = new DatabaseHandler(v.getContext());
                        currentStudentParticipationList = db.getStudentParticipationList(currentStudentSectionId,UUID);
                        finalGrade = db.getFinalGrade(currentStudentSectionId,UUID);
                        currentStudentHomeworks = db.getHomeworkNameAndGrade(((StudentActivity)activity).getCurrentStudentIdList().get(position),((StudentActivity)activity).getCurrentSection().get_SectionId(),UUID);

                        if (currentStudentHomeworks == null){
                            currentStudentHomeworks = new ArrayList<String>();
                        }

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


                        StudentDialog dialog = new StudentDialog(currentStudentParticipationList, ((StudentActivity)activity).getCurrentStudentNamesList().get(position), finalGrade, currentStudentHomeworks,percentageParticipations,percentageHomeworks);
                        dialog.show(((StudentActivity)activity).getFragmentManager(), "dialog_student");
                        db.close();
                    }catch (Exception e){
                    }
                }
            }
        });
        return customView;
    }

}
