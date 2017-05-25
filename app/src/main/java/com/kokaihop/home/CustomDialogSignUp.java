package com.kokaihop.home;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.DialogSignupLoginBinding;
import com.kokaihop.authentication.login.LoginActivity;
import com.kokaihop.authentication.signup.SignUpActivity;

/**
 * Created by Rajendra Singh on 24/5/17.
 */

public class CustomDialogSignUp extends DialogFragment implements View.OnClickListener{
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

        signupLoginBinding.dialogCancel.setOnClickListener(this);
        signupLoginBinding.dialogLogIn.setOnClickListener(this);
        signupLoginBinding.dialogSignUp.setOnClickListener(this);

        return dialog;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_cancel:
                break;
            case R.id.dialog_sign_up:
                startActivity(new Intent(getActivity(), SignUpActivity.class));
                break;
            case R.id.dialog_log_in:
                startActivity(new Intent(getActivity(), LoginActivity.class).putExtra("profile",true));
                break;
        }
        this.dismiss();
    }
}
