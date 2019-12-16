package com.food.oder.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.food.oder.Base.BaseFragment;
import com.food.oder.R;

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
