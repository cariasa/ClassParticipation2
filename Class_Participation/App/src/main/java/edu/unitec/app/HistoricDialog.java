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

    SemesterQuarter actual;

    Spinner AvailableSemester;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_changesemester, null);

        DatabaseHandler DB = new DatabaseHandler(view.getContext());
        List<String> LIST = DB.getAvailableSemester();
        DB.close();
        if (LIST != null) {
            String CheckCurrent = LIST.get(LIST.size() - 1);
            String[] CheckArray = CheckCurrent.split(" ");

            if (Integer.parseInt(CheckArray[1]) == SemesterQuarter.CURRENT_QUARTER && Integer.parseInt(CheckArray[3]) == SemesterQuarter.CURRENT_SEMESTER && Integer.parseInt(CheckArray[5]) == SemesterQuarter.CURRENT_YEAR) {

            } else {
                String currentSemester = "Quarter: " + SemesterQuarter.CURRENT_QUARTER + " Semester: " + SemesterQuarter.CURRENT_SEMESTER + " Year: " + SemesterQuarter.CURRENT_YEAR;
                LIST.add(currentSemester);
            }
            //0        1 2         3 4     5
            //Quarter: 4 Semester: 5 Year: 2014

        } else {
            LIST = new ArrayList();
            String currentSemester = "Quarter: " + SemesterQuarter.CURRENT_QUARTER + " Semester: " + SemesterQuarter.CURRENT_SEMESTER + " Year: " + SemesterQuarter.CURRENT_YEAR;
            LIST.add(currentSemester);
        }

        AvailableSemester = (Spinner) view.findViewById(R.id.AvailSemester);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, LIST);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AvailableSemester.setAdapter(dataAdapter);


        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Change Semester and Quarter");
        builder.setView(view);

        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                HistoricDialog.this.getDialog().cancel();
            }
        });

        builder.setPositiveButton("Change",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing here because we override this button later to change the close behaviour.
                        //However, we still need this because on older versions of Android unless we
                        //pass a handler the button doesn't get instantiated
                    }
                }
        );

        return builder.create();

    }


    @Override
    public void onStart() {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        AlertDialog dialog = (AlertDialog) getDialog();

        if (dialog != null) {
            Button positiveButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);

            assert positiveButton != null;
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String SELECTVAL = (String) AvailableSemester.getSelectedItem();
                    String[] SELECTD = SELECTVAL.split(" ");

                    actual.setQuarter(Integer.parseInt(SELECTD[1]));
                    actual.setSemester(Integer.parseInt(SELECTD[3]));
                    actual.setYear(Integer.parseInt(SELECTD[5]));

                    getActivity().recreate();

                    dismiss();
                    //0        1 2         3 4     5
                    //Quarter: 4 Semester: 5 Year: 2014
                }
            });
        }
    }

    public HistoricDialog(SemesterQuarter actual) {
        this.actual = actual;
    }

    public SemesterQuarter getActual() {
        return actual;
    }

    public void setActual(SemesterQuarter actual) {
        this.actual = actual;
    }
}


