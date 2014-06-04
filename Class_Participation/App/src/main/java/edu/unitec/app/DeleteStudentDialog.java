package edu.unitec.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 03/06/2014.
 */
public class DeleteStudentDialog extends DialogFragment {
    ArrayAdapter<String> arrayAdapter;
    List<String> listViewStudentNameList;
    ArrayList<Integer> SelectedIdxToDelete;
    Section currentSection;

    DeleteStudentDialog(Section currentSection, ArrayAdapter<String> arrayAdapter, List<String> list)
    {
        this.currentSection = currentSection;
        this.arrayAdapter = arrayAdapter;
        this.listViewStudentNameList = list;
        this.SelectedIdxToDelete=new ArrayList<Integer>();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_deletestudent, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Students");
        builder.setView(view);
        String elements []=new String[listViewStudentNameList.size()];
        for(int i=0;i<listViewStudentNameList.size();i++){
            elements[i]=listViewStudentNameList.get(i);
        }
        builder.setMultiChoiceItems(elements,null,new DialogInterface.OnMultiChoiceClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                     if (isChecked) {
                         SelectedIdxToDelete.add(which);
                     }else if(SelectedIdxToDelete.contains(which)){
                         SelectedIdxToDelete.remove(Integer.valueOf(which));
                     }
            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                DeleteStudentDialog.this.getDialog().cancel();
            }
        });

        builder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Do nothing here because we override this button later to change the close behaviour.
                        //However, we still need this because on older versions of Android unless we
                        //pass a handler the button doesn't get instantiated
                    }
                });

        return builder.create();
    }
}
