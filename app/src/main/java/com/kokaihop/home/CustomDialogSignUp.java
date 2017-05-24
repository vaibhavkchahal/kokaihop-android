package com.kokaihop.home;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Window;
import android.widget.LinearLayout;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.DialogSignupLoginBinding;

/**
 * Created by Rajendra Singh on 24/5/17.
 */

public class CustomDialogSignUp extends DialogFragment {

    DialogSignupLoginBinding signupLoginBinding;

    public CustomDialogSignUp() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signupLoginBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(),R.layout.dialog_signup_login,new LinearLayout(getActivity()),false);

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(signupLoginBinding.getRoot());

        return dialog;
    }
}
