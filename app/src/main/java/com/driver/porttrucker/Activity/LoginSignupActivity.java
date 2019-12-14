package com.driver.porttrucker.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.driver.porttrucker.Base.BaseActivity;
import com.driver.porttrucker.R;
import com.driver.porttrucker.Service.GpsService;

public class LoginSignupActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        addActivity(this);

        loadLayout();

        runGpsService();
    }

    private void loadLayout() {

    }

    private void runGpsService() {
        startService(new Intent(this, GpsService.class));
    }

    public void login(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void signup(View view){
        startActivity(new Intent(this, SignupActivity.class));
    }
}
