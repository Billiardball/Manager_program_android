package com.driver.porttrucker.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.driver.porttrucker.API.SendEmail;
import com.driver.porttrucker.Activity.MainActivity;
import com.driver.porttrucker.Common.Common;
import com.driver.porttrucker.Common.Constant;
import com.driver.porttrucker.Common.ReqConst;
import com.driver.porttrucker.Model.SaleModel;
import com.driver.porttrucker.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SaleAdapter extends BaseAdapter {

    ArrayList<SaleModel> allData =  new ArrayList<>();
    Context context;

    public SaleAdapter(Context context, ArrayList<SaleModel> tempData) {
        this.context = context;
        this.allData = tempData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return allData.size();
    }

    @Override
    public SaleModel getItem(int i) {
        return allData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CustomHolder holder;

        if (view == null){
            holder = new CustomHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_sale, viewGroup, false);
            view.setTag(holder);
        }else {
            holder = (CustomHolder)view.getTag();
        }

        holder.setId(view);
        holder.loadData(allData.get(i));

        return view;
    }

    private class CustomHolder implements View.OnClickListener{

        ImageView imvTruckPhoto;
        TextView txvMaker, txvEngine, txvHorsePower, txvMileage, txvTransSpeed, txvPrice;
        Button btnCall, btnEmail;
        SaleModel model;

        private void setId(View item){
            imvTruckPhoto = (ImageView)item.findViewById(R.id.imvTruckPhoto); imvTruckPhoto.setOnClickListener(this);
            txvMaker = (TextView)item.findViewById(R.id.txvMaker);
            txvEngine = (TextView)item.findViewById(R.id.txvEngine);
            txvHorsePower = (TextView)item.findViewById(R.id.txvHorsePower);
            txvMileage = (TextView)item.findViewById(R.id.txvMileage);
            txvTransSpeed = (TextView)item.findViewById(R.id.txvTransSpeed);
            txvPrice = (TextView)item.findViewById(R.id.txvPrice);
            btnCall = (Button)item.findViewById(R.id.btnCall); btnCall.setOnClickListener(this);
            btnEmail = (Button)item.findViewById(R.id.btnEmail); btnEmail.setOnClickListener(this);
        }

        private void loadData(SaleModel model){
            this.model = model;

            Picasso.get().load("https://porttrucker.app/images/trucks/" + model.getTruck_1()).into(imvTruckPhoto);
            txvMaker.setText(model.getYear() + " " + model.getMake());
            txvEngine.setText("Engine: " + model.getEngine_type());
            txvHorsePower.setText("Horse Power: " + model.getHorses());
            txvMileage.setText("Mileage: " + model.getMileage());
            txvTransSpeed.setText("Trans Speed: " + model.getMany_gears());

            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String price = formatter.format(Integer.valueOf(model.getPrice()));
            txvPrice.setText(price);

            if (model.getPhone() == "null"){
                btnCall.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnCall:
                    showCallAlertDialog();
                    break;
                case R.id.btnEmail:
                    showSendMessageDialog();
                    break;
                case R.id.imvTruckPhoto:
                    ((MainActivity)context).showSaleDetailsFragment(model);
                    break;
            }
        }

        private void showCallAlertDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            final View alertDialog = LayoutInflater.from(context).inflate(R.layout.alert_phone_call, null);

            builder.setView(alertDialog);
            final AlertDialog dialog = builder.create();

            Button btnClose = (Button) alertDialog.findViewById(R.id.btnClose);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });

            ImageView imvClose = (ImageView)alertDialog.findViewById(R.id.imvClose);
            imvClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });

            TextView txvPhoneNumber = (TextView)alertDialog.findViewById(R.id.txvPhoneNumber);
            String phoneNumber = model.getPhone().substring(0, 3) + "-"
                    + model.getPhone().substring(3, 6) + "-" +
                    model.getPhone().substring(6, model.getPhone().length());
            txvPhoneNumber.setText(phoneNumber);

            txvPhoneNumber.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("MissingPermission")
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + model.getPhone()));
                    context.startActivity(intent);
                }
            });

            ImageView imvUserPhoto = (ImageView)alertDialog.findViewById(R.id.imvUserPhoto);
            Picasso.get().load(Constant.USER_PHOTO_BASE_URL + model.getPhoto()).into(imvUserPhoto);

            TextView txvUserName = (TextView)alertDialog.findViewById(R.id.txvUserName);
            txvUserName.setText(model.getFirst_name() + " " + model.getLast_name());

            dialog.show();
        }

        private void showSendMessageDialog(){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            final View alertDialog = LayoutInflater.from(context).inflate(R.layout.alert_send_email, null);

            builder.setView(alertDialog);
            final AlertDialog dialog = builder.create();

            RoundedImageView imvUserPhoto = (RoundedImageView) alertDialog.findViewById(R.id.imvUserPhoto);
            Picasso.get().load(Constant.USER_PHOTO_BASE_URL + model.getPhoto()).into(imvUserPhoto);

            TextView txvUserName = (TextView)alertDialog.findViewById(R.id.txvUserName);
            txvUserName.setText(model.getFirst_name() + " " + model.getLast_name());

            TextView txvMail = (TextView)alertDialog.findViewById(R.id.txvMail);
            txvMail.setText(Common.userModel.email);

            TextView txvPhoneNumber = (TextView)alertDialog.findViewById(R.id.txvPhoneNumber);
            String phoneNumber = Common.userModel.phone.substring(0, 3) + "-"
                    + Common.userModel.phone.substring(3, 6) + "-" +
                    Common.userModel.phone.substring(6, Common.userModel.phone.length());
            txvPhoneNumber.setText(phoneNumber);

            final EditText edtMessage = (EditText)alertDialog.findViewById(R.id.edtMessage);

            ImageView imvClose = (ImageView)alertDialog.findViewById(R.id.imvClose);
            imvClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });

            Button btnSend = (Button)alertDialog.findViewById(R.id.btnSend);
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendEmail(edtMessage.getText().toString().trim());
                    dialog.cancel();
                }
            });

            dialog.show();
        }

        private void sendEmail(String message) {
            final Response.Listener<String> res = new Response.Listener<String>() {
                @Override
                public void onResponse(String json) {
                    ((MainActivity)context).closeProgress();
                    try{
                        JSONObject res = new JSONObject(json);
                        if (res.getBoolean(Constant.STATUS)){
                            ((MainActivity)context).showToast("Mail Sent");
                        }else {
                            ((MainActivity)context).showToast(res.getString("message"));
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
            SendEmail req = new SendEmail(Common.userModel.email, Common.userModel.phone, model.getEmail(), message, res, error);
            req.setRetryPolicy(new DefaultRetryPolicy(ReqConst.TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue request = Volley.newRequestQueue(context);
            request.add(req);
        }
    }
}
