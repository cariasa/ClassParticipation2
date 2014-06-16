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
    public StudentItemAdapter(Context activity, ArrayList<StudentItem> items) {
        this.activity = activity;
        this.items = items;
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
        buttonParticipation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( activity instanceof StudentActivity){
                    ((StudentActivity)activity).showParticipationDialog(position);
                }
            }
        });
        //Override onClick method from buttonHomework
        Button buttonHomework = (Button) customView.findViewById(R.id.button_student_homework);
        buttonHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( activity instanceof StudentActivity){
                    ListView listview = (ListView) ((StudentActivity) activity).findViewById(R.id.listViewCheckHomework);
                    Intent intent = new Intent(activity.getApplicationContext(), CheckHomework.class);
                    intent.putExtra("Section", (int)(((StudentActivity)activity).getCurrentSection().get_SectionId()));
                    intent.putExtra("StudentName",((StudentActivity)activity).getCurrentStudentNamesList().get(position));
                    intent.putExtra("studentId",((StudentActivity)activity).getCurrentStudentIdList().get(position));
                    activity.startActivity(intent);
                }
            }
        });
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
                        currentStudentParticipationList = db.getStudentParticipationList(currentStudentSectionId);
                        finalGrade = db.getFinalGrade(currentStudentSectionId);
                        Log.d("adaasdsda", Integer.toString(((StudentActivity)activity).getCurrentStudentIdList().get(position)));
                        currentStudentHomeworks = db.getHomeworkNameAndGrade(((StudentActivity)activity).getCurrentStudentIdList().get(position),currentStudentSectionId);
                        StudentDialog dialog = new StudentDialog(currentStudentParticipationList, ((StudentActivity)activity).getCurrentStudentNamesList().get(position), finalGrade, currentStudentHomeworks);
                        dialog.show(((StudentActivity)activity).getFragmentManager(), "dialog_student");
                        db.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        return customView;
    }

}
