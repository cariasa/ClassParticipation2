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
import android.widget.EditText;

import java.util.List;

/**
 * Created by Ariel on 12-15-13.
 */
public class AddCriteriaDialog extends DialogFragment
{
    Homework currentHomework;

    EditText txtCriteriaName;
    EditText txtCriteriaWeight;

    ArrayAdapter arrayAdapter;
    List<String> listViewCriteriaNameList;

    public AddCriteriaDialog(Homework currenthomework, ArrayAdapter arrayAdapter, List<String> list)
    {
        this.currentHomework = currenthomework;
        this.arrayAdapter = arrayAdapter;
        this.listViewCriteriaNameList = list;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_addcriteria, null);

        txtCriteriaName = (EditText)view.findViewById(R.id.editTextCriteriaName);
        txtCriteriaWeight = (EditText)view.findViewById(R.id.editTextCriteriaWeight);


        txtCriteriaName.requestFocus();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Criteria");
        builder.setView(view);

        builder.setNegativeButton("Close", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                AddCriteriaDialog.this.getDialog().cancel();
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
            positiveButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Boolean wantToCloseDialog = false;
                    boolean criteriaExist = false;

                    try{
                        DatabaseHandler db = new DatabaseHandler(v.getContext());
                        criteriaExist = db.criteriaExist(txtCriteriaName.getText().toString(),currentHomework.getHomeworkId());
                        db.close();
                    }catch (Exception e){

                    }

                    if(txtCriteriaName.getText().toString().isEmpty()){
                        txtCriteriaName.requestFocus();
                    }else if(criteriaExist){
                        txtCriteriaWeight.requestFocus();
                    }else if ( txtCriteriaWeight.getText().toString().isEmpty() ){
                        txtCriteriaWeight.requestFocus();
                    }else{
                        Criteria criteria = new Criteria(
                                txtCriteriaName.getText().toString(),
                                Double.parseDouble(txtCriteriaWeight.getText().toString()),
                                currentHomework.getHomeworkId());

                        try{
                            DatabaseHandler db = new DatabaseHandler(v.getContext());
                            if(!criteriaExist){
                                db.addCriteria(criteria);
                            }
                            db.close();

                            //Update the listView
                            listViewCriteriaNameList.add(txtCriteriaName.getText().toString());
                            arrayAdapter.notifyDataSetChanged();

                            txtCriteriaName.setText("");
                            txtCriteriaWeight.setText("");

                            txtCriteriaName.requestFocus();
                        }catch (Exception e){

                        }
                        wantToCloseDialog = true;
                    }

                    if(wantToCloseDialog){
                        dismiss();
                    }
                }
            });
        }

    }
}
