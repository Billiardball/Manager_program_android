package com.food.oder.Activity;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.food.oder.Adapter.NotificationAdapter;
import com.food.oder.Fragment.IndexFragment;
import com.food.oder.Fragment.SaleDetailFragment;
import com.food.oder.Fragment.UpdatePasswordFragment;
import com.food.oder.Model.NotificationModel;
import com.food.oder.Model.SaleModel;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.food.oder.Base.BaseActivity;
import com.food.oder.Common.Common;
import com.food.oder.Common.Constant;
import com.food.oder.Common.Preference;
import com.food.oder.Fragment.CameraDetailFragment;
import com.food.oder.Fragment.CameraFragment;
import com.food.oder.Fragment.ContactUsFragment;
import com.food.oder.Fragment.GalleryFragment;
import com.food.oder.Fragment.JobsFragment;
import com.food.oder.Fragment.NewsFragment;
import com.food.oder.Fragment.PortDriversFragment;
import com.food.oder.Fragment.PortLiveFragment;
import com.food.oder.Fragment.PortReportDetailFragment;
import com.food.oder.Fragment.PortReportsFragment;
import com.food.oder.Fragment.ProfileFragment;
import com.food.oder.Fragment.RateFragment;
import com.food.oder.Fragment.MyTruckFragment;
import com.food.oder.Fragment.SaleFragment;
import com.food.oder.Fragment.VideosFragment;
import com.food.oder.Model.PortReportModel;
import com.food.oder.R;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    Fragment currentFragment;

    DrawerLayout drawerLayout;
    NavigationView nvMenu;
    View menuHeader;
    RoundedImageView imvAvatar, imvUserPhoto;
    TextView txvUserName, txvAppName;
    ImageView imvPortRate, imvPortCamera, imvMyReport, imvPortJob, imvSetting, imvNotification, imvChat;
    LinearLayout lytLinkButtons;

    ArrayList<NotificationModel> allNotifications =  new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        closeUnnecessaryActivities();

        loadLayout();
    }

    private void loadLayout() {
        txvAppName = (TextView)findViewById(R.id.txvAppName); txvAppName.setOnClickListener(this);
        lytLinkButtons = (LinearLayout)findViewById(R.id.lytLinkButtons);
        imvChat = (ImageView)findViewById(R.id.imvChat); imvChat.setOnClickListener(this);
        imvNotification = (ImageView)findViewById(R.id.imvNotification); imvNotification.setOnClickListener(this);
        imvSetting = (ImageView)findViewById(R.id.imvSetting); imvSetting.setOnClickListener(this);
        imvPortRate = (ImageView)findViewById(R.id.imvPortRate); imvPortRate.setOnClickListener(this);
        imvPortCamera = (ImageView)findViewById(R.id.imvPortCamera) ; imvPortCamera.setOnClickListener(this);
        imvMyReport = (ImageView)findViewById(R.id.imvMyReport); imvMyReport.setOnClickListener(this);
        imvPortJob = (ImageView)findViewById(R.id.imvPortJob); imvPortJob.setOnClickListener(this);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);

        nvMenu = (NavigationView)findViewById(R.id.nvMenu); nvMenu.setNavigationItemSelectedListener(this);

        menuHeader = nvMenu.getHeaderView(0);
        imvUserPhoto = (RoundedImageView)menuHeader.findViewById(R.id.imvUserPhoto);
        txvUserName = (TextView)menuHeader.findViewById(R.id.txvUserName);


        imvAvatar = (RoundedImageView)findViewById(R.id.imvAvatar); imvAvatar.setOnClickListener(this);
        if (Common.userModel.getProfile().length() > 0){
            Picasso.get().load(Constant.USER_PHOTO_BASE_URL + Common.userModel.getProfile()).into(imvAvatar);
        }

        refreshUserInfo();

        currentFragment = new IndexFragment(this); openFragment(currentFragment);
    }

    public void showNavMenu(View view){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void showProfilePage(View view){
        currentFragment = new ProfileFragment(this); openFragment(currentFragment);
    }

    public void showSaleDetailsFragment(SaleModel saleModel){
        SaleDetailFragment frm = new SaleDetailFragment(this, saleModel);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frmContainer, frm);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void showCameraDetailFragment(int portId){
        CameraDetailFragment frm = new CameraDetailFragment(String.valueOf(portId)); openFragment(frm);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frmContainer, frm);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showPortReportDetailFragment(int reportId, ArrayList<PortReportModel> allData){

        PortReportDetailFragment frm = new PortReportDetailFragment(reportId, allData); openFragment(frm);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frmContainer, frm);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void refreshUserInfo(){
        if (Common.userModel.getProfile().length() > 0){
            Picasso.get().load(Constant.USER_PHOTO_BASE_URL + Common.userModel.getProfile()).into(imvAvatar);
            Picasso.get().load(Constant.USER_PHOTO_BASE_URL + Common.userModel.getProfile()).into(imvUserPhoto);
        }

        txvUserName.setText(Common.userModel.getFirstName() +  " " + Common.userModel.getLastName());
    }


    public void openFragment(Fragment frm){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frmContainer, frm);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {

        drawerLayout.closeDrawers();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showFragment(menuItem);
            }
        }, 300);

        return true;
    }

    private void showFragment(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.navCamera:
                currentFragment = CameraFragment.newInstance(); openFragment(currentFragment);
                break;
            case R.id.navNews:
                currentFragment = NewsFragment.newInstance(); openFragment(currentFragment);
                break;
            case R.id.navJobs:
                currentFragment = JobsFragment.newInstance(); openFragment(currentFragment);
                break;
            case R.id.navGallery:
                currentFragment = GalleryFragment.newInstance(); openFragment(currentFragment);
                break;
            case R.id.navVideos:
                currentFragment = VideosFragment.newInstance(); openFragment(currentFragment);
                break;
            case R.id.navRate:
                currentFragment = RateFragment.newInstance(); openFragment(currentFragment);
                break;
            case R.id.navSale:
                currentFragment = SaleFragment.newInstance(); openFragment(currentFragment);
                break;
            case R.id.contactUs:
                currentFragment = ContactUsFragment.newInstance(); openFragment(currentFragment);
                break;
            case R.id.portLive:
                currentFragment = PortLiveFragment.newInstance(); openFragment(currentFragment);
                break;
            case R.id.portDrivers:
                currentFragment = PortDriversFragment.newInstance(); openFragment(currentFragment);
                break;
            case R.id.portReport:
                currentFragment = PortReportsFragment.newInstance(); openFragment(currentFragment);
                break;
            case R.id.navLogout:
                Preference.getInstance().put(this, Constant.EMAIL, "");
                Preference.getInstance().put(this, Constant.PWD, "");
                startActivity(new Intent(this, LoginSignupActivity.class)); finish();
                break;
        }
    }

    @SuppressLint("WrongConstant")
    private void showNotifications(View v) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_notifications, null);

        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, 1600);
        popupWindow.setOutsideTouchable(true);

        NotificationAdapter adapter = new NotificationAdapter(this);
        RecyclerView rvNotifications = (RecyclerView)popupView.findViewById(R.id.rvNotifications);
        rvNotifications.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvNotifications.setAdapter(adapter);
        adapter.setData(NotificationModel.allData);

        LinearLayout lytContainer = (LinearLayout)popupView.findViewById(R.id.lytContainer);
        lytContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAsDropDown(v);
    }

    @SuppressLint("RestrictedApi")

    private void showPopupMenu_1() {
        PopupMenu popup = new PopupMenu(this, imvSetting);
        popup.getMenuInflater().inflate(R.menu.menu_setting, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuMyReport:
                        currentFragment = new MyTruckFragment(MainActivity.this); openFragment(currentFragment);
                        break;
                    case R.id.menuMyPassword:
                        currentFragment = new UpdatePasswordFragment(MainActivity.this); openFragment(currentFragment);
                        break;
                }
                return true;
            }
        });

        popup.show();
    }

    public void changeVisibleStateOfLinkButtons(int state){
        lytLinkButtons.setVisibility(state);
    }

    private void showPopupMenu_2() {
        PopupMenu popup = new PopupMenu(this, imvSetting);
        popup.getMenuInflater().inflate(R.menu.menu_profile, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuMyProfile:
                        currentFragment = new ProfileFragment(MainActivity.this); openFragment(currentFragment);
                        break;
                    case R.id.menuMyTruck:
                        currentFragment = new MyTruckFragment(MainActivity.this); openFragment(currentFragment);
                        break;
                    case R.id.menuLogout:
                        Preference.getInstance().put(MainActivity.this, Constant.EMAIL, "");
                        Preference.getInstance().put(MainActivity.this, Constant.PWD, "");
                        startActivity(new Intent(MainActivity.this, LoginSignupActivity.class)); finish();
                        break;
                }
                return true;
            }
        });

        popup.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imvPortRate:
                currentFragment = RateFragment.newInstance(); openFragment(currentFragment);
                break;
            case R.id.imvPortCamera:
                currentFragment = CameraFragment.newInstance(); openFragment(currentFragment);
                break;
            case R.id.imvMyReport:
                currentFragment = new MyTruckFragment(this); openFragment(currentFragment);
                break;
            case R.id.imvPortJob:
                currentFragment = JobsFragment.newInstance(); openFragment(currentFragment);
                break;
            case R.id.imvSetting:
                showPopupMenu_1();
                break;
            case R.id.imvNotification:
                showNotifications(imvNotification);
                break;
            case R.id.imvChat:
                currentFragment = PortLiveFragment.newInstance(); openFragment(currentFragment);
                break;
            case R.id.imvAvatar:
                showPopupMenu_2();
                break;
            case R.id.txvAppName:
                currentFragment = new IndexFragment(this); openFragment(currentFragment);
                break;
        }
    }
}
