package com.kokaihop.editprofile;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityEmailPreferencesBinding;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

/**
 * Created by Rajendra Singh on 7/6/17.
 */

public class EmailPreferencesViewModel extends BaseViewModel {
    private Context context;
    private ActivityEmailPreferencesBinding preferencesBinding;
    private EmailPreferences emailPreferences;
    User user = User.getInstance();

    public EmailPreferencesViewModel(Context context, ActivityEmailPreferencesBinding preferencesBinding) {
        this.context = context;
        this.preferencesBinding = preferencesBinding;
        emailPreferences = new EmailPreferences();
        emailPreferences.setTodayRecipe(user.getSettings().isSuggestionsOfTheDay());
        emailPreferences.setDrinkingTips(user.getSettings().isNewsletters());
        emailPreferences.setNoEmail(user.getSettings().isNoEmails());
    }

    @Override
    public void destroy() {
        ((Activity) context).finish();
    }

    public EmailPreferences getEmailPreferences() {
        return emailPreferences;
    }

    public void preferencesChanged(View view) {
        switch ((view.getId())) {
            case R.id.cb_email_pref_no_email:
                emailPreferences.setTodayRecipe(false);
                emailPreferences.setDrinkingTips(false);
                break;
            default:
                if(emailPreferences.isTodayRecipe() || emailPreferences.isDrinkingTips()){
                    emailPreferences.setNoEmail(false);
                }else{
                    emailPreferences.setNoEmail(true);
                }
        }
    }

    public void updatePreferences(){
        String accessToken = Constants.AUTHORIZATION_BEARER +
                SharedPrefUtils.getSharedPrefStringData(context,Constants.ACCESS_TOKEN);
        String userId = SharedPrefUtils.getSharedPrefStringData(context,Constants.USER_ID);
        new EditProfileApiHelper().changePreferences(accessToken, userId, emailPreferences, new IApiRequestComplete<EditProfileResponse>() {
            @Override
            public void onSuccess(EditProfileResponse response) {
                if(response.isSuccess()){
                    Toast.makeText(context,"Settings Updated!!!",Toast.LENGTH_SHORT).show();
                    ((Activity)context).finish();
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context,"Settings Can't be updated!!!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(EditProfileResponse response) {
                Toast.makeText(context,"Settings Can't be updated!!!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
