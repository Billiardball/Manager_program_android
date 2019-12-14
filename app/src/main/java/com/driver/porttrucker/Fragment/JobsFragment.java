package com.driver.porttrucker.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.driver.porttrucker.Base.BaseFragment;
import com.driver.porttrucker.R;

public class JobsFragment extends BaseFragment {

    View fragment;

    JobsFragment instance;

    public JobsFragment() { }

    public static JobsFragment newInstance(){
        JobsFragment newInstance = new JobsFragment();
        return  newInstance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        fragment = inflater.inflate(R.layout.frm_jobs, parent, false);
        return fragment;
    }
}
