package edu.unitec.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Alberto on 11/06/2014.
 */
public class CreateHomeworkDialog extends DialogFragment {
    Section currentSection;
    EditText homeworkName;

    CreateHomeworkDialog(Section currentSection)
    {
        this.currentSection = currentSection;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_create_homework, null);

        homeworkName = (EditText)view.findViewById(R.id.editTextCreateHomeworkName);

        homeworkName.requestFocus();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Create Homework");
        builder.setView(view);

        builder.setNegativeButton("Close", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                CreateHomeworkDialog.this.getDialog().cancel();
                getActivity().finish();
            }
        });

        builder.setPositiveButton("Create",
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
        //disable the clicks outside the dialog
        Window window = this.getDialog().getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        //override the onKey method to close the activity when back is pressed
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode==event.KEYCODE_BACK){
                    dialog.dismiss();
                    getActivity().finish();
                }
                return false;
            }
        });
        if(dialog != null){
            Button positiveButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);

            assert positiveButton != null;
            positiveButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Boolean wantToCloseDialog = false;

                    if(homeworkName.getText().toString().isEmpty()){
                        homeworkName.requestFocus();
                    }else{
                        Homework homework = new Homework(homeworkName.getText().toString(),
                                currentSection.get_SectionId());

                        try{
                            DatabaseHandler db = new DatabaseHandler(v.getContext());
                            if(!db.homeworkExist(homework,currentSection.get_SectionId())){
                                db.addHomework(homework);
                                //send the homework from de database to the activity
                                homework=db.getLastHomework();
                                ((HomeworkActivity)getActivity()).setHomework(homework);
                                getActivity().setTitle(homeworkName.getText().toString());
                                db.close();
                            }else{
                                Toast.makeText(getActivity(),"Ya existe una tarea con ese nombre", Toast.LENGTH_LONG).show();
                                homeworkName.setText("");
                                homeworkName.requestFocus();
                                return;
                            }

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
