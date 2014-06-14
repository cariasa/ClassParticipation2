package edu.unitec.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 13/06/2014.
 */
public class ShowCriteriaDialog extends DialogFragment {
    Homework currentHomework;

    String criteriaName;
    public ShowCriteriaDialog(Homework currenthomework,String criteriaName)
    {
        this.currentHomework = currenthomework;
        this.criteriaName=criteriaName;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_showcriteria, null);

        TextView txtCriteriaWeight;
        txtCriteriaWeight = (TextView)view.findViewById(R.id.textViewCriteriaWeight);

        DatabaseHandler db=new DatabaseHandler(this.getActivity());
        double weight=db.getCriteriaWeight(criteriaName,currentHomework.getHomeworkId());
        txtCriteriaWeight.setText(""+weight);

        List<Double> weightlist;
        weightlist=db.getAllCriteria_Weights(currentHomework.getHomeworkId());
        double acum=0;
        for(int i=0;i<weightlist.size();i++){
            acum+=weightlist.get(i);
        }

        TextView txtCriteriaPercentage=(TextView)view.findViewById(R.id.textViewCriteriaPercentage);
        double percentage=(weight/acum)*100;
        txtCriteriaPercentage.setText("Size: "+weightlist.size()+"\nTotal: "+acum+"\nPercentage: "+percentage);
        db.close();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(criteriaName);
        builder.setView(view);
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ShowCriteriaDialog.this.getDialog().cancel();
            }
        });

        return builder.create();
    }

    @Override
    public void onStart(){
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
    }
}
