package edu.unitec.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nivx on 12/5/14.
 */
public class HistoricDialog extends DialogFragment {

    int year;
    int semester;
    int quarter;

    Spinner AvailableSemester;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_changesemester, null);

        DatabaseHandler DB = new DatabaseHandler(view.getContext());
        List<String> LIST = DB.getAvailableSemester();

        DB.close();

        AvailableSemester = (Spinner)view.findViewById(R.id.AvailSemester);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, LIST);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AvailableSemester.setAdapter(dataAdapter);


        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Change Semester and Quarter");
        builder.setView(view);

        builder.setNegativeButton("Close", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                HistoricDialog.this.getDialog().cancel();
            }
        });

        builder.setPositiveButton("Add",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        //Do nothing here because we override this button later to change the close behaviour.
                        //However, we still need this because on older versions of Android unless we
                        //pass a handler the button doesn't get instantiated
                    }
                });

        return builder.create();

    }


    @Override
    public void onStart(){
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        AlertDialog dialog = (AlertDialog)getDialog();

        if(dialog != null){
            Button positiveButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);

            assert positiveButton != null;
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String SELECTVAL = (String)AvailableSemester.getSelectedItem();
                    String[] SELECTD = SELECTVAL.split(" ");

                    quarter = Integer.parseInt(SELECTD[1]);
                    semester = Integer.parseInt(SELECTD[3]);
                    year = Integer.parseInt(SELECTD[5]);

                    dismiss();
                    //0        1 2         3 4     5
                    //Quarter: 4 Semester: 5 Year: 2014
                }
            });
        }
    }

    public HistoricDialog(int year, int semester, int quarter) {
        this.year = year;
        this.semester = semester;
        this.quarter = quarter;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }
}
