package com.driver.porttrucker.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.driver.porttrucker.API.UpdateUserInfo;
import com.driver.porttrucker.Activity.MainActivity;
import com.driver.porttrucker.Base.BaseFragment;
import com.driver.porttrucker.Common.Common;
import com.driver.porttrucker.Common.Constant;
import com.driver.porttrucker.Common.ReqConst;
import com.driver.porttrucker.MyApp;
import com.driver.porttrucker.R;
import com.driver.porttrucker.Utils.LogUtil;
import com.driver.porttrucker.Utils.MultiPartRequest;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends BaseFragment{

    Context context;
    View fragment;

    ProfileFragment instance;

    Spinner spCities;
    RoundedImageView imvUserPhoto;
    EditText edtFirstName, edtLastName, edtEmail, edtPhone;
    RadioButton rbDriverType1, rbDriverType2;
    ImageView imvSelectPhoto;
    Button btnUpdateUserInfo;

    String[] cities;
    File userPhoto;
    String firstName, lastName, email, phone, driverType, state;
    int cityIndex;

    EasyImage easyImage;

    public ProfileFragment(Context context) {
        this.context = context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasyImage.configuration(getContext())
                .setCopyTakenPhotosToPublicGalleryAppFolder(false)
                .setCopyPickedImagesToPublicGalleryAppFolder(false)
                .setAllowMultiplePickInGallery(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        fragment = inflater.inflate(R.layout.frm_profile, parent, false);

        spCities = (Spinner)fragment.findViewById(R.id.spCities);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.item_city, getResources().getStringArray(R.array.cities));
        spCities.setAdapter(adapter);
        spCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityIndex = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cities = getResources().getStringArray(R.array.cities);
        for (int i = 0; i < cities.length; i++){
            if (cities[i].equals(Common.userModel.getState())){
                spCities.setSelection(i);
                cityIndex = i;
            }
        }

        imvUserPhoto = (RoundedImageView)fragment.findViewById(R.id.imvUserPhoto);
        if (Common.userModel.getProfile().length() > 0){
            Picasso.get().load(Constant.USER_PHOTO_BASE_URL + Common.userModel.getProfile()).into(imvUserPhoto);
        }

        edtFirstName = (EditText)fragment.findViewById(R.id.edtFirstName); edtFirstName.setText(Common.userModel.getFirstName());
        edtLastName = (EditText)fragment.findViewById(R.id.edtLastName); edtLastName.setText(Common.userModel.getLastName());
        edtEmail = (EditText)fragment.findViewById(R.id.edtEmail); edtEmail.setText(Common.userModel.getEmail());
        edtPhone = (EditText)fragment.findViewById(R.id.edtPhone); edtPhone.setText(Common.userModel.getPhone());

        rbDriverType1 = (RadioButton)fragment.findViewById(R.id.rbDriverType1);
        rbDriverType2 = (RadioButton)fragment.findViewById(R.id.rbDriverType2);

        if (Common.userModel.getDriverType().equals(Constant.DRIVER_TYPE_1)){
            rbDriverType1.setChecked(true);
        }else if (Common.userModel.getDriverType().equals(Constant.DRIVER_TYPE_2)){
            rbDriverType2.setChecked(true);
        }

        imvSelectPhoto = (ImageView)fragment.findViewById(R.id.imvSelectPhoto); imvSelectPhoto.setOnClickListener(this);
        btnUpdateUserInfo = (Button)fragment.findViewById(R.id.btnUpdateUserInfo); btnUpdateUserInfo.setOnClickListener(this);

        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(List<File> files, EasyImage.ImageSource source, int type) {
                openCropActivity(files.get(0));
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                if (source == EasyImage.ImageSource.CAMERA_IMAGE) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(getContext());
                    if (photoFile != null) photoFile.delete();
                }
            }
        });

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                userPhoto = new File(resultUri.getPath());
                uploadUserPhoto();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void uploadUserPhoto() {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(Common.userModel.getId()));

            String url = ReqConst.SERVER_URL + "mobile_upload_image.php";

            MultiPartRequest reqMultiPart = new MultiPartRequest(url, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    ((MainActivity)getContext()).closeProgress();
                    ((MainActivity)getActivity()).showAlertDialog("File upload failed");
                }
            }, new Response.Listener<String>() {

                @Override
                public void onResponse(String json) {
                    try {
                        LogUtil.e("upload photo json == " + json);
                        JSONObject response = new JSONObject(json);
                        if (response.getBoolean(Constant.STATUS)){
                            Common.userModel.profile = response.getString("photo");
                            Picasso.get().load(Constant.USER_PHOTO_BASE_URL +  Common.userModel.profile).into(imvUserPhoto);
                            ((MainActivity)getActivity()).refreshUserInfo();
                            ((MainActivity)getActivity()).showToast("Photo Uploaded");
                        }else {
                            ((MainActivity)getActivity()).showAlertDialog(response.getString("message"));
                        }
                    }catch (JSONException e){
                        ((MainActivity)getActivity()).closeProgress();
                        ((MainActivity)getActivity()).showAlertDialog(getString(R.string.serverFailed));
                    }
                }
            }, userPhoto, "fileToUpload", params);

            reqMultiPart.setRetryPolicy(new DefaultRetryPolicy(ReqConst.TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MyApp.getInstance().addToRequestQueue(reqMultiPart, url);

        } catch (Exception e) {
            e.printStackTrace();
            ((MainActivity)getActivity()).closeProgress();
            ((MainActivity)getActivity()).showAlertDialog("File upload failed");
        }
    }

    private void openCropActivity(File file){
        CropImage.activity(Uri.fromFile(file)).start(getContext(), this);
    }

    public void updateUserInfo(){
        firstName = edtFirstName.getText().toString().trim();
        lastName = edtLastName.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
        phone = edtPhone.getText().toString().trim();
        if (rbDriverType1.isChecked()){
            driverType = Constant.DRIVER_TYPE_1;
        }else if (rbDriverType2.isChecked()){
            driverType = Constant.DRIVER_TYPE_2;
        }else {
            driverType = "";
        }
        state = cities[cityIndex];

        if (firstName.length() == 0) {showToast("Input First name"); return;}
        if (lastName.length() == 0) {showToast("Input Last name"); return;}
        if (phone.length() == 0) {showToast("Input Phone"); return;}
        if (state.length() == 0) {showToast("Select State"); return;}

        final Response.Listener<String> res = new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                closeProgress();
                try{
                    JSONObject res = new JSONObject(json);
                    if (res.getBoolean(Constant.STATUS)){
                        Common.userModel.firstName = firstName;
                        Common.userModel.lastName = lastName;
                        Common.userModel.phone = phone;
                        Common.userModel.state = state;
                        Common.userModel.driverType = driverType;

                        showToast("User Profile Updated");

                        ((MainActivity)context).refreshUserInfo();

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
        UpdateUserInfo req = new UpdateUserInfo(String.valueOf(Common.userModel.getId()), firstName, lastName, phone, driverType, state, res, error);
        req.setRetryPolicy(new DefaultRetryPolicy(ReqConst.TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue request = Volley.newRequestQueue(getContext());
        request.add(req);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imvSelectPhoto:
                EasyImage.openChooserWithGallery(this, "Select Image", 0);
                break;
            case R.id.btnUpdateUserInfo:
                updateUserInfo();
                break;
        }
    }
}
