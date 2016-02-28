package edu.unitec.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
    EditText studentEmail;

    String UUID;

    StudentItemAdapter arrayAdapter;
    List<StudentItem> listViewStudentNameList;

    AddStudentDialog(Section currentSection, StudentItemAdapter arrayAdapter, List<StudentItem> list,String UUID)
    {
        this.currentSection = currentSection;
        this.arrayAdapter = arrayAdapter;
        this.listViewStudentNameList = list;
        this.UUID = UUID;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_addstudent, null);

        studentId = (EditText)view.findViewById(R.id.editTextStudentId);
        studentName = (EditText)view.findViewById(R.id.editTextStudentName);
        studentMajor = (EditText)view.findViewById(R.id.editTextStudentMajor);
        studentEmail = (EditText)view.findViewById(R.id.editTextEmail);


        studentId.requestFocus();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Student");
        builder.setView(view);

        builder.setNegativeButton("Close", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                AddStudentDialog.this.getDialog().cancel();
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
                    boolean studentSectionExist = false;

                    try{
                        DatabaseHandler db = new DatabaseHandler(v.getContext());
                        studentSectionExist = db.studentSectionExist(currentSection, Integer.parseInt(studentId.getText().toString()),UUID);
                        db.close();
                    }catch (Exception e){

                    }

                    if(studentId.getText().toString().isEmpty()){
                        studentId.requestFocus();
                    }else if(studentSectionExist){
                        studentId.requestFocus();
                    }else if ( studentName.getText().toString().isEmpty() ){
                        studentName.requestFocus();
                    }else{
                        Student student = new Student(Integer.parseInt(studentId.getText().toString()),
                                                      studentName.getText().toString().replaceAll("'", "''"),
                                                      studentEmail.getText().toString());

                        try{
                            DatabaseHandler db = new DatabaseHandler(v.getContext());
                            if(!db.studentExist(studentId.getText().toString() + studentName.getText().toString().replaceAll("'","''"))){
                                if(db.tableStudentIsEmpty(currentSection.get_SectionId())) {
                                    //if table student is empty show the invisible menuItems
                                    MenuItem item_statistics = ((StudentActivity) getActivity()).menu.findItem(R.id.item_statistics);
                                    MenuItem item_student = ((StudentActivity) getActivity()).menu.findItem(R.id.item_Student);
                                    MenuItem save_student = ((StudentActivity) getActivity()).menu.findItem(R.id.item_addStudent);
                                    MenuItem save_students = ((StudentActivity) getActivity()).menu.findItem(R.id.save_students);
                                    MenuItem delete_student = ((StudentActivity) getActivity()).menu.findItem(R.id.delete_students);
                                    MenuItem newAssignment = ((StudentActivity) getActivity()).menu.findItem(R.id.item_newAssignment);
                                    MenuItem newHomework = ((StudentActivity) getActivity()).menu.findItem(R.id.item_newHomework);
                                    MenuItem newParticipation = ((StudentActivity) getActivity()).menu.findItem(R.id.item_newParticipation);
                                    item_statistics.setVisible(true);
                                    item_student.setVisible(true);
                                    save_student.setVisible(true);
                                    save_students.setVisible(false);
                                    newAssignment.setVisible(true);
                                    newHomework.setVisible(true);
                                    newParticipation.setVisible(true);
                                    delete_student.setVisible(true);
                                }
                                db.addStudent(student);
                                db.addStudentTable(student, currentSection);
                            }
                            db.close();

                            //Update the listView of student activity
                            listViewStudentNameList.add(new StudentItem(student.get_StudentName()));
                            arrayAdapter.notifyDataSetChanged();

                            studentId.setText("");
                            studentName.setText("");
                            studentMajor.setText("");

                            studentId.requestFocus();
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
