package com.kokaihop.editprofile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.altaworks.kokaihop.ui.R;
import com.facebook.login.LoginManager;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.home.HomeActivity;
import com.kokaihop.userprofile.HistoryDataManager;
import com.kokaihop.userprofile.ProfileDataManager;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import static com.kokaihop.KokaihopApplication.getContext;

/**
 * Created by Rajendra Singh on 10/7/17.
 */

public class SettingsViewModel extends BaseViewModel {
    Context context;

    public SettingsViewModel(Context context) {
        this.context = context;
    }

    @Override
    protected void destroy() {
        ((Activity) context).finish();
    }

    //    Display dialogbox to confirm the user for logout process.
    public void logout() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
        dialog.setTitle(R.string.logout);
        dialog.setMessage(R.string.logout_now);
        dialog.setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                GoogleAnalyticsHelper.trackEventAction(context.getString(R.string.user_category), context.getString(R.string.user_logout_action));
                setProgressVisible(true);
                dialog.dismiss();
                Intent intent = new Intent(getContext(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                User.removeInstance();
                SharedPrefUtils.setSharedPrefStringData(getContext(), Constants.ACCESS_TOKEN, null);
                SharedPrefUtils.setSharedPrefStringData(getContext(), Constants.USER_ID, null);
                SharedPrefUtils.setSharedPrefStringData(getContext(), Constants.FRIENDLY_URL, null);
                ProfileDataManager profileDataManager = new ProfileDataManager();
                profileDataManager.updateIsFavoriteForAllRecipe();
                profileDataManager.updateLastUpdatedTimeForAllRecipe();
                new HistoryDataManager().deleteHistory();
                if (Constants.FACEBOOK_LOGIN.equals(SharedPrefUtils.getSharedPrefStringData(context, Constants.LOGIN_TYPE)))
                    LoginManager.getInstance().logOut();
                setProgressVisible(false);
                destroy();
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
