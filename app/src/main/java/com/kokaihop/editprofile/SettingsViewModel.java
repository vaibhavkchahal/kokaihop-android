package com.kokaihop.editprofile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.home.HomeActivity;
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
        dialog.setTitle("Confirm Logout");
        dialog.setMessage("Do you really want to logout!!!");
        dialog.setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                setProgressVisible(true);
                Intent intent = new Intent(getContext(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                User.removeInstance();
                SharedPrefUtils.setSharedPrefStringData(getContext(), Constants.ACCESS_TOKEN, null);
                SharedPrefUtils.setSharedPrefBooleanData(getContext(), Constants.RECIPE_DETAIL_COACHMARK_VISIBILITY, false);
                SharedPrefUtils.setSharedPrefBooleanData(getContext(), Constants.SEARCH_COACHMARK_VISIBILITY, false);
                SharedPrefUtils.setSharedPrefBooleanData(getContext(), Constants.USERPROFILE_COACHMARK_VISIBILITY, false);
                SharedPrefUtils.setSharedPrefStringData(getContext(), Constants.USER_ID, null);
                SharedPrefUtils.setSharedPrefStringData(getContext(), Constants.FRIENDLY_URL, null);
                dialog.dismiss();
                ProfileDataManager profileDataManager = new ProfileDataManager();
                profileDataManager.updateIsFavoriteForAllRecipe();
                profileDataManager.updateLastUpdatedTimeForAllRecipe();
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
