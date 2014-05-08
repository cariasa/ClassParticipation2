package edu.unitec.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Ariel on 12-17-13.
 */
public class AboutDialog extends DialogFragment
{
    AboutDialog()
    {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_about, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setTitle("About");
        builder.setView(view);

        builder.setNegativeButton("Close", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                AboutDialog.this.getDialog().cancel();
            }
        });

        return builder.create();
    }
}
