package com.kokaihop.utility;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;

/**
 * Created by Rajendra Singh on 5/7/17.
 */

public class ConfirmationDialog extends Dialog {

    public ConfirmationDialog(@NonNull Context context, String title, String msg, String positiveButtonText, String negativeButtonText) {
        super(context);
        this.setContentView(R.layout.dialog_confirm);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ((TextView) this.findViewById(R.id.dialog_confirm_title)).setText(title);
        ((TextView) this.findViewById(R.id.dialog_confirmation_msg)).setText(msg);
        ((TextView) this.findViewById(R.id.confirm_positive)).setText(positiveButtonText);
        ((TextView) this.findViewById(R.id.confirm_negative)).setText(negativeButtonText);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setupListener();
    }

    public ConfirmationDialog(@NonNull Context context, String title, String msg) {
        super(context);
        this.setContentView(R.layout.dialog_confirm);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ((TextView) this.findViewById(R.id.dialog_confirm_title)).setText(title);
        ((TextView) this.findViewById(R.id.dialog_confirmation_msg)).setHint(msg);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setupListener();
    }

    public void setupListener(){
        this.getWindow().findViewById(R.id.confirm_negative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public View  getConfirmPositive(){
        return this.findViewById(R.id.confirm_positive);
    }
}
