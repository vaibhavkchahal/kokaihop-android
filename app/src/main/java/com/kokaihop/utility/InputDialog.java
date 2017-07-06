package com.kokaihop.utility;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;

/**
 * Created by Rajendra Singh on 5/7/17.
 */

public class InputDialog extends Dialog {

    public InputDialog(@NonNull Context context) {
        super(context);
    }

    public InputDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected InputDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public Dialog setupDialog(String title, String hint, String positiveButtonText, String negativeButtonText) {
        this.setContentView(R.layout.dialog_new_cookbook);
        ((TextView)this.findViewById(R.id.dialog_title)).setText(title);
        ((EditText)this.findViewById(R.id.dialog_text)).setHint(hint);
        ((TextView)this.findViewById(R.id.positive)).setText(positiveButtonText);
        ((TextView)this.findViewById(R.id.negative)).setText(negativeButtonText);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return this;
    }
    public Dialog setupDialog(String title, String hint) {
        this.setContentView(R.layout.dialog_new_cookbook);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return this;
    }
}
