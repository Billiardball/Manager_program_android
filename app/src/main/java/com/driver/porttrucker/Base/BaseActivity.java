package com.driver.porttrucker.Base;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.driver.porttrucker.R;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    private static ArrayList<Activity> activityIds = new ArrayList<>();

    private KProgressHUD progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showProgress() {
        progressBar = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Please wait");
        progressBar.setCancellable(false);
        progressBar.show();
    }

    public void closeProgress() {
        if (progressBar == null) return;
        progressBar.dismiss();
        progressBar =  null;
    }

    public void addActivity(Activity intent){
        activityIds.add(intent);
    }

    public void closeUnnecessaryActivities(){
        for (Activity activity : activityIds){
            activity.finish();
        }
        activityIds.clear();
    }

    public void showToast(String msg){
        Toasty.info(this, msg, Toast.LENGTH_LONG, true).show();
    }

    public void showAlertDialog(String msg){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(msg);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),

                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {

    }
}
