package myweatherApplication.example.MyWeatherApplication.dialog;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class DialogManagement {

    public static void exitDialog(Activity context){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle("Exit");
        alert.setMessage("Are you sure?");
        alert.setIcon(android.R.drawable.ic_delete);

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                context.finishAffinity();

            }
        });
        alert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();


    }
}
