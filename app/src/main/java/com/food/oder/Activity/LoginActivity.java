package com.food.oder.Activity;

import androidx.annotation.Nullable;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.food.oder.API.Login;
import com.food.oder.API.LoginWithSocial;
import com.food.oder.Base.BaseActivity;
import com.food.oder.Common.Common;
import com.food.oder.Common.Constant;
import com.food.oder.Common.Preference;
import com.food.oder.Common.ReqConst;
import com.food.oder.Model.UserModel;
import com.food.oder.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends BaseActivity {

    CallbackManager callbackManager;
    TwitterAuthClient client;

    EditText edtEmail, edtPassword;
    CheckBox cbKeepMeLogin;

    String email = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        addActivity(this);

        email = Preference.getInstance().getValue(this, Constant.EMAIL, "");

        printHashKey();

        initSDK();

        loadLayout();
    }

    private void loadLayout() {
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtPassword = (EditText)findViewById(R.id.edtPassword);

        cbKeepMeLogin = (CheckBox)findViewById(R.id.cbKeepMeLogin);

        if (email.length() != 0){
            edtEmail.setText(email);
            edtPassword.setText(Preference.getInstance().getValue(this, Constant.PWD, ""));
            cbKeepMeLogin.setChecked(true);
        }
    }

    public void printHashKey() {
        final String TAG = "facebook_key_hash ==";
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    private void initSDK() {
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {@Override
                                public void onCompleted(JSONObject object,
                                                        GraphResponse response) {
                                    try {
                                        String id = object.getString("id");
                                        try {
                                            URL profile_pic = new URL(
                                                    "http://graph.facebook.com/" + id + "/picture?type=large");

                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }
                                        String name = object.getString("name");
                                        email = object.getString("email");

                                        String firstName = (name.split("0"))[0];
                                        String lastName = (name.split(" "))[1];
                                        loginWithSocial(firstName, lastName, email, id);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields",
                                "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        showToast("Login Cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        showToast(exception.getMessage());
                    }
                });

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret)))
                .debug(true)//enable debug mode
                .build();

        //finally initialize twitter with created configs
        Twitter.initialize(config);
        client = new TwitterAuthClient();
    }

    private void loginWithSocial(String firstName, String lastName, final String email, final String password) {
        final Response.Listener<String> res = new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                closeProgress();
                try{
                    JSONObject res = new JSONObject(json);
                    if (res.getBoolean(Constant.STATUS)){
                        Common.userModel = new UserModel(
                                res.getInt("id"),
                                res.getString("firstName"),
                                res.getString("lastName"),
                                res.getString("email"),
                                res.getString("profile"),
                                res.getString("phone"),
                                res.getString("driverType"),
                                res.getString("state"));

                        Preference.getInstance().put(LoginActivity.this, Constant.EMAIL, email);
                        Preference.getInstance().put(LoginActivity.this, Constant.PWD, password);

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else {
                        showToast(res.getString("message"));
                    }
                }catch (JSONException e){
                    showAlertDialog(e.getMessage());
                }
            }
        };
        final Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeProgress();
                showAlertDialog(getString(R.string.serverFailed));
            }
        };

        showProgress();
        LoginWithSocial req = new LoginWithSocial(firstName, lastName, email, password, res, error);
        req.setRetryPolicy(new DefaultRetryPolicy(ReqConst.TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue request = Volley.newRequestQueue(this);
        request.add(req);
    }

    public void loginWithFacebook(View view){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
    }

    public void loginWithTwitter(View view){
        client.authorize(this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                Toast.makeText(LoginActivity.this, "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(TwitterException e) {
                Toast.makeText(LoginActivity.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        client.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isValid() {
        email = edtEmail.getText().toString().trim();
        password = edtPassword.getText().toString().trim();

        if (email.length() == 0){ showToast("Input email"); return  false;}
        if (password.length() == 0) {showToast("Input password"); return  false;}

        return true;
    }

    public void login(View view){
        if (!isValid()) return;

        final Response.Listener<String> res = new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                closeProgress();
                try{
                    JSONObject res = new JSONObject(json);
                    if (res.getBoolean(Constant.STATUS)){
                        Common.userModel = new UserModel(
                                res.getInt("id"),
                                res.getString("firstName"),
                                res.getString("lastName"),
                                res.getString("email"),
                                res.getString("profile"),
                                res.getString("phone"),
                                res.getString("driverType"),
                                res.getString("state"));

                        if (cbKeepMeLogin.isChecked()){
                            Preference.getInstance().put(LoginActivity.this, Constant.EMAIL, email);
                            Preference.getInstance().put(LoginActivity.this, Constant.PWD, password);
                        }else {
                            Preference.getInstance().put(LoginActivity.this, Constant.EMAIL, "");
                            Preference.getInstance().put(LoginActivity.this, Constant.PWD, "");
                        }

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else {
                        showToast(res.getString("message"));
                    }
                }catch (JSONException e){
                    showAlertDialog(e.getMessage());
                }
            }
        };
        final Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeProgress();
                showAlertDialog(getString(R.string.serverFailed));
            }
        };

        showProgress();
        Login req = new Login(email, password, res, error);
        req.setRetryPolicy(new DefaultRetryPolicy(ReqConst.TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue request = Volley.newRequestQueue(this);
        request.add(req);
    }

    public void havingTrouble(View view){

    }

    public void gotoSignUp(View view){
        startActivity(new Intent(this, SignupActivity.class));
    }
}
