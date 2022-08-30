package myweatherApplication.example.MyWeatherApplication.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;

import com.example.myWeatherApplication.R;

import cn.pedant.SweetAlert.SweetAlertDialog;
import myweatherApplication.example.MyWeatherApplication.ui.activity.MainActivity;

public class DialogManagment {




    public static void exitBackPressed(Activity context){

        AlertDialog.Builder alert = new android.app.AlertDialog.Builder(context);
        alert.setTitle(context.getString(R.string.exit));
        alert.setMessage(R.string.exite_message);
        alert.setIcon(android.R.drawable.ic_delete);
        

       alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {

           }
       });

       alert.setPositiveButton(R.string.yes , new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               context.finishAffinity();

           }
       });
       alert.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {

           }
       });
       alert.show();
    }



}
