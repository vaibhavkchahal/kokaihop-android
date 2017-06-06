package com.kokaihop.editprofile;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentChangePasswordBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment{

    FragmentChangePasswordBinding passwordBinding;
    ChangePasswordViewModel viewModel;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        passwordBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_password,container,false);
        viewModel = new ChangePasswordViewModel(this,passwordBinding);
        passwordBinding.setViewModel(viewModel);
        return passwordBinding.getRoot();
    }

}
