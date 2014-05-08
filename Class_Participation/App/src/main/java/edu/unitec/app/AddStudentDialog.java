package edu.unitec.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Ariel on 12-15-13.
 */
public class AddStudentDialog extends DialogFragment
{
    Section currentSection;

    EditText studentId;
    EditText studentName;
    EditText studentMajor;

    ArrayAdapter<String> arrayAdapter;
    List<String> listViewStudentNameList;

    AddStudentDialog(Section currentSection, ArrayAdapter<String> arrayAdapter, List<String> list)
    {
        this.currentSection = currentSection;

        this.arrayAdapter = arrayAdapter;
        this.listViewStudentNameList = list;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_addstudent, null);

        studentId = (EditText)view.findViewById(R.id.editTextStudentId);
        studentName = (EditText)view.findViewById(R.id.editTextStudentName);
        studentMajor = (EditText)view.findViewById(R.id.editTextStudentMajor);


        studentId.requestFocus();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Student");
        builder.setView(view);

        builder.setNegativeButton("Close", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                AddStudentDialog.this.getDialog().cancel();
            }
        });

        builder.setPositiveButton("Add",
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
    public void onStart()
    {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        AlertDialog dialog = (AlertDialog)getDialog();

        if(dialog != null)
        {
            Button positiveButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);

            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    Boolean wantToCloseDialog = false;
                    boolean studentSectionExist = false;

                    try
                    {
                        DatabaseHandler db = new DatabaseHandler(v.getContext());
                        studentSectionExist = db.studentSectionExist(currentSection, Integer.parseInt(studentId.getText().toString()));
                        db.close();
                    }

                    catch (Exception e)
                    {
                    }

                    if ( studentId.getText().toString().isEmpty() )
                    {
                        studentId.requestFocus();
                    }

                    else if ( studentSectionExist )
                    {
                        studentId.requestFocus();
                    }

                    else if ( studentName.getText().toString().isEmpty() )
                    {
                        studentName.requestFocus();
                    }

                    else
                    {
                        Student student = new Student(Integer.parseInt(studentId.getText().toString()),
                                                      studentName.getText().toString(),
                                                      studentMajor.getText().toString());

                        try
                        {
                            DatabaseHandler db = new DatabaseHandler(v.getContext());

                            if ( !db.studentExist(Integer.parseInt(studentId.getText().toString())) )
                            {
                                db.addStudent(student);
                            }

                            db.addStudentTable(student, currentSection);
                            db.close();

                            //Update the listView of student activity
                            listViewStudentNameList.add(student.get_StudentName());
                            arrayAdapter.notifyDataSetChanged();

                            studentId.setText("");
                            studentName.setText("");
                            studentMajor.setText("");

                            studentId.requestFocus();
                        }

                        catch (Exception e)
                        {
                        }

                        //wantToCloseDialog = true;
                    }

                    if(wantToCloseDialog)
                    {
                        dismiss();
                    }
                }
            });
        }

    }
}
