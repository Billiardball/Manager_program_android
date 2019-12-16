package com.food.oder.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.food.oder.API.GetActualProfileAPI;
import com.food.oder.CustomView.RecursiveRadioGroup;
import com.food.oder.Model.TruckerModel;
import com.food.oder.Utils.LogUtil;
import com.jaredrummler.android.colorpicker.ColorPanelView;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorPickerView;
import com.squareup.picasso.Picasso;
import com.food.oder.API.UpdateTruckInfoAPI;
import com.food.oder.Activity.MainActivity;
import com.food.oder.Base.BaseFragment;
import com.food.oder.Common.Common;
import com.food.oder.Common.Constant;
import com.food.oder.Common.ReqConst;
import com.food.oder.MyApp;
import com.food.oder.R;
import com.food.oder.Utils.MultiPartRequest;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static android.app.Activity.RESULT_OK;

public class MyTruckFragment extends BaseFragment {

    Context context;

    View fragment;
    Button btnUpdateInfo;
    ImageView img, imvImg1, imvImg2, imvImg3, imvImg4;
    EditText edtMake, edtYear, edtEngineType, edtMileage, edtHorsePower, edtTransmitionMake, edtRearAxleCapacity, edtPrice;
    TextView txvTransmitionSpeed;
    View viewColor;
    LinearLayout lytTransmitionSpeed;
    CheckBox chbWantSell, chbContactByPhone, chbContactByEmail;

    String imgName = "", imgName1 = "", imgName2 = "", imgName3 = "", imgName4 = "", make = "", year = "",
            engineType = "", mileage = "", horsePower = "", transmitionMake = "", transmitionSpeed = "", rearAxleCapacity = "", price = "", newColorValue = "",
            wantSell = "", contactByPhone = "", contactByEmail = "";
    Uri resultUri;

    TruckerModel model;

    File image;

    public MyTruckFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        fragment = inflater.inflate(R.layout.frm_my_truck, parent, false);

        txvTransmitionSpeed = (TextView)fragment.findViewById(R.id.txvTransmitionSpeed);
        chbContactByEmail = (CheckBox)fragment.findViewById(R.id.chbContactByEmail);
        chbContactByPhone = (CheckBox)fragment.findViewById(R.id.chbContactByPhone);
        chbWantSell = (CheckBox)fragment.findViewById(R.id.chbWantSell);
        lytTransmitionSpeed = (LinearLayout)fragment.findViewById(R.id.lytTransmitionSpeed); lytTransmitionSpeed.setOnClickListener(this);
        viewColor = (View)fragment.findViewById(R.id.viewColor); viewColor.setOnClickListener(this);
        edtMake = (EditText)fragment.findViewById(R.id.edtMake);
        edtYear = (EditText)fragment.findViewById(R.id.edtYear);
        edtEngineType = (EditText)fragment.findViewById(R.id.edtEngineType);
        edtMileage = (EditText)fragment.findViewById(R.id.edtMileage);
        edtHorsePower = (EditText)fragment.findViewById(R.id.edtHorsePower);
        edtTransmitionMake = (EditText)fragment.findViewById(R.id.edtTransmitionMake);
        edtRearAxleCapacity = (EditText)fragment.findViewById(R.id.edtRearAxleCapacity);
        edtPrice = (EditText)fragment.findViewById(R.id.edtPrice);

        imvImg1 = (ImageView)fragment.findViewById(R.id.imvImg1); imvImg1.setOnClickListener(this);
        imvImg2 = (ImageView)fragment.findViewById(R.id.imvImg2); imvImg2.setOnClickListener(this);
        imvImg3 = (ImageView)fragment.findViewById(R.id.imvImg3); imvImg3.setOnClickListener(this);
        imvImg4 = (ImageView)fragment.findViewById(R.id.imvImg4); imvImg4.setOnClickListener(this);

        btnUpdateInfo = (Button)fragment.findViewById(R.id.btnUpdateInfo); btnUpdateInfo.setOnClickListener(this);

        init();

        getActualProfile();

        return fragment;
    }

    private void getActualProfile() {
        final Response.Listener<String> res = new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                ((MainActivity)context).closeProgress();
                try{
                    JSONObject res = new JSONObject(json);
                    if (res.getBoolean(Constant.STATUS)){
                        model = new TruckerModel(res.getJSONObject("actualProfile"));
                        Glide.with(context).load(Constant.TRUCK_PHOTO_BASE_URL + model.truck1).into(imvImg1);
                        Glide.with(context).load(Constant.TRUCK_PHOTO_BASE_URL + model.truck2).into(imvImg2);
                        Glide.with(context).load(Constant.TRUCK_PHOTO_BASE_URL + model.truck3).into(imvImg3);
                        Glide.with(context).load(Constant.TRUCK_PHOTO_BASE_URL + model.truck4).into(imvImg4);
                        edtMake.setText(model.make);
                        edtYear.setText(model.year);
                        if (model.color.length() > 0){
                            viewColor.setBackgroundColor(Color.parseColor(model.color));
                        }else {
                            viewColor.setBackgroundColor(Color.WHITE);
                        }
                        edtEngineType.setText(model.engineType);
                        edtMileage.setText(model.mileage);
                        edtHorsePower.setText(model.horses);
                        edtTransmitionMake.setText(model.transmisionMake);
                        edtRearAxleCapacity.setText(model.rearAxleCapacity);
                        chbWantSell.setChecked(isTrue(model.wantShell));
                        chbContactByPhone.setChecked(isTrue(model.contactedByPhone));
                        chbContactByEmail.setChecked(isTrue(model.contactByEmail));
                        edtPrice.setText(model.price);
                    }else {
                        showToast(res.getString("message"));
                    }
                }catch (JSONException e){
                    ((MainActivity)context).showAlertDialog(e.getMessage());
                }
            }
        };
        final Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((MainActivity)context).closeProgress();
                ((MainActivity)context).showAlertDialog(((MainActivity)context).getString(R.string.serverFailed));
            }
        };

        ((MainActivity)context).showProgress();
        GetActualProfileAPI req = new GetActualProfileAPI(String.valueOf(Common.userModel.id),res, error);
        req.setRetryPolicy(new DefaultRetryPolicy(ReqConst.TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue request = Volley.newRequestQueue(context);
        request.add(req);
    }

    private Boolean isTrue(String str){
        if (str.equals("1")){
            return true;
        }else {
            return false;
        }
    }

    private void init() {
        EasyImage.configuration(getContext())
                .setCopyTakenPhotosToPublicGalleryAppFolder(false)
                .setCopyPickedImagesToPublicGalleryAppFolder(false)
                .setAllowMultiplePickInGallery(true);
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
                resultUri = result.getUri();
                image = new File(resultUri.getPath());
//                Picasso.get().load(resultUri).into(img);
                uploadImage(image);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void openCropActivity(File file){
        CropImage.activity(Uri.fromFile(file)).start(getContext(), this);
    }

    private void uploadImage(File image){
        try {
            Map<String, String> params = new HashMap<>();

            String url = ReqConst.SERVER_URL + "mobile_upload_truck_image.php";

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
                        JSONObject res = new JSONObject(json);
                        if (res.getBoolean(Constant.STATUS)){
                            imgName = res.getString("fileName");
                            if (img == imvImg1){
                                imgName1 = res.getString("fileName");
                                Picasso.get().load(Constant.TRUCK_PHOTO_BASE_URL + imgName).into(imvImg1);
                            }else if (img == imvImg2){
                                imgName2 = res.getString("fileName");
                                Picasso.get().load(Constant.TRUCK_PHOTO_BASE_URL + imgName).into(imvImg2);
                            }else if (img == imvImg3){
                                imgName3 = res.getString("fileName");
                                Picasso.get().load(Constant.TRUCK_PHOTO_BASE_URL + imgName).into(imvImg3);
                            }else if (img == imvImg4){
                                imgName4 = res.getString("fileName");
                                Picasso.get().load(Constant.TRUCK_PHOTO_BASE_URL + imgName).into(imvImg4);
                            }
                        }else {
                            ((MainActivity)getActivity()).showAlertDialog(res.getString("message"));
                        }
                    }catch (JSONException e){
                        ((MainActivity)getActivity()).closeProgress();
                        ((MainActivity)getActivity()).showAlertDialog(getString(R.string.serverFailed));
                    }
                }
            }, image, "fileToUpload", params);

            reqMultiPart.setRetryPolicy(new DefaultRetryPolicy(ReqConst.TIME_OUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MyApp.getInstance().addToRequestQueue(reqMultiPart, url);

        } catch (Exception e) {
            e.printStackTrace();
            ((MainActivity)getActivity()).closeProgress();
            ((MainActivity)getActivity()).showAlertDialog("File upload failed");
        }
    }

    private void updateInfo() {

        make = edtMake.getText().toString().trim();
        year = edtYear.getText().toString().trim();
        engineType =  edtEngineType.getText().toString().trim();
        mileage = edtMileage.getText().toString().trim();
        horsePower = edtHorsePower.getText().toString().trim();
        transmitionMake = edtTransmitionMake.getText().toString().trim();
        transmitionSpeed = txvTransmitionSpeed.getText().toString().trim();
        rearAxleCapacity = edtRearAxleCapacity.getText().toString().trim();
        price = edtPrice.getText().toString().trim();
        wantSell = isChecked(chbWantSell);
        contactByPhone = isChecked(chbContactByPhone);
        contactByEmail = isChecked(chbContactByEmail);

        final Response.Listener<String> res = new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                closeProgress();
                try{
                    JSONObject res = new JSONObject(json);
                    if (res.getBoolean(Constant.STATUS)){
                        showToast("Success");
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
        UpdateTruckInfoAPI req = new UpdateTruckInfoAPI(String.valueOf(Common.userModel.id), make, year, newColorValue, mileage, engineType, horsePower,
                transmitionSpeed, rearAxleCapacity, transmitionMake, wantSell, price, contactByPhone, contactByEmail, imgName1, imgName2, imgName3, imgName4, res, error);
        req.setRetryPolicy(new DefaultRetryPolicy(ReqConst.TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue request = Volley.newRequestQueue(context);
        request.add(req);
    }

    private String getDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
        String datetime = dateformat.format(c.getTime());

        return datetime;
    }

    private String isChecked(CheckBox checkBox){
        if (checkBox.isChecked()){
            return "1";
        }else {
            return "0";
        }
    }
    private void showTransmitionSpeedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View alertDialog = LayoutInflater.from(context).inflate(R.layout.dialog_transmition_speed, null);
        builder.setView(alertDialog);
        final AlertDialog dialog = builder.create();

        RecursiveRadioGroup radioGroup = (RecursiveRadioGroup)alertDialog.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RecursiveRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RecursiveRadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rdb10:
                        txvTransmitionSpeed.setText("10");
                        break;
                    case R.id.rdb13:
                        txvTransmitionSpeed.setText("13");
                        break;
                    case R.id.rdb18:
                        txvTransmitionSpeed.setText("18");
                        break;
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void takeTruckColor1(){
        ColorPickerDialog dialog = ColorPickerDialog.newBuilder().setColor(Color.BLACK).create();
        dialog.setColorPickerDialogListener(new ColorPickerDialogListener() {
            @Override
            public void onColorSelected(int dialogId, int color) {
                String hexColor = String.format("#%06X", (0xFFFFFF & color));
                newColorValue = hexColor;
                viewColor.setBackgroundColor(Color.parseColor(newColorValue));
            }

            @Override
            public void onDialogDismissed(int dialogId) {

            }
        });
        dialog.show(getFragmentManager(), "color-picker-dialog");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imvImg1:
                img = imvImg1;
                EasyImage.openChooserWithGallery(this, "Select Image", 0);
                break;
            case R.id.imvImg2:
                img = imvImg2;
                EasyImage.openChooserWithGallery(this, "Select Image", 0);
                break;
            case R.id.imvImg3:
                img = imvImg3;
                EasyImage.openChooserWithGallery(this, "Select Image", 0);
                break;
            case R.id.imvImg4:
                img = imvImg4;
                EasyImage.openChooserWithGallery(this, "Select Image", 0);
                break;
            case R.id.btnUpdateInfo :
                updateInfo();
                break;
            case R.id.lytTransmitionSpeed:
                showTransmitionSpeedDialog();
                break;
            case R.id.viewColor:
                takeTruckColor1();
                break;
        }
    }
}
