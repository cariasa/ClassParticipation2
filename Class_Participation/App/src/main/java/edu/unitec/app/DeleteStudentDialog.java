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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 03/06/2014.
 */
public class DeleteStudentDialog extends DialogFragment {
    StudentItemAdapter arrayAdapter;
    List<StudentItem> listViewStudentNameList;
    List<Integer> list_StudentID;
    ArrayList<Integer> SelectedIdxToDelete;
    Section currentSection;
    String elements[];

    DeleteStudentDialog(Section currentSection, StudentItemAdapter arrayAdapter, List<StudentItem> list, List<Integer> list2)
    {
        this.currentSection = currentSection;
        this.arrayAdapter = arrayAdapter;
        this.listViewStudentNameList = list;
        this.SelectedIdxToDelete=new ArrayList<Integer>();
        this.list_StudentID=list2;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_deletestudent, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Students");
        builder.setView(view);
        elements=new String[listViewStudentNameList.size()];
        for(int i=0;i<listViewStudentNameList.size();i++){
            elements[i]=listViewStudentNameList.get(i).getStudentName();
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

    @Override
    public void onStart(){
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        AlertDialog dialog = (AlertDialog)getDialog();

        if(dialog != null){
            Button positiveButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);

            positiveButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    ArrayList<Integer> StudentID_toErase = new ArrayList<Integer>();
                    for(int i=0; i<SelectedIdxToDelete.size(); i++){
                        StudentID_toErase.add(list_StudentID.get(SelectedIdxToDelete.get(i)));
                    }
                    try{
                        DatabaseHandler db = new DatabaseHandler(v.getContext());
                        db.deleteStudent(StudentID_toErase);db.close();
                    }catch (Exception e){

                    }
                    for(int i=0; i<SelectedIdxToDelete.size(); i++){
                        if(listViewStudentNameList.size()==1){
                            listViewStudentNameList.remove(0);
                        }else {
                            listViewStudentNameList.remove((int) SelectedIdxToDelete.get(i));
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }

    }
}
