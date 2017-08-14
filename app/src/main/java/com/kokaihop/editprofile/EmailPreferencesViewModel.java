package com.kokaihop.editprofile;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
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
    private EmailPreferences emailPreferences;
    User user = User.getInstance();

    public EmailPreferencesViewModel(Context context) {
        this.context = context;
        emailPreferences = new EmailPreferences();
        if (user.getSettings() != null) {
            emailPreferences.getSettings().setSuggestionsOfTheDay(user.getSettings().isSuggestionsOfTheDay());
            emailPreferences.getSettings().setNewsletters(user.getSettings().isNewsletters());
            emailPreferences.getSettings().setNoEmails(user.getSettings().isNoEmails());
        }
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
            case R.id.rl_email_pref_no_email:
                emailPreferences.getSettings().setNoEmails(!emailPreferences.getSettings().isNoEmails());
                emailPreferences.getSettings().setSuggestionsOfTheDay(!emailPreferences.getSettings().isNoEmails());
                emailPreferences.getSettings().setNewsletters(!emailPreferences.getSettings().isNoEmails());
                break;

            case R.id.rl_email_pref_recipe_motion:
                emailPreferences.getSettings().setSuggestionsOfTheDay(!emailPreferences.getSettings().isSuggestionsOfTheDay());
                break;

            case R.id.rl_email_pref_drinking_tips:
                emailPreferences.getSettings().setNewsletters(!emailPreferences.getSettings().isNewsletters());
                break;
        }

        if (emailPreferences.getSettings().isSuggestionsOfTheDay() || emailPreferences.getSettings().isNewsletters()) {
            emailPreferences.getSettings().setNoEmails(false);
        } else {
            emailPreferences.getSettings().setNoEmails(true);
        }
    }

    public void updatePreferences() {
        String accessToken = Constants.AUTHORIZATION_BEARER +
                SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
        String userId = SharedPrefUtils.getSharedPrefStringData(context, Constants.USER_ID);
        new SettingsApiHelper().changePreferences(accessToken, userId, emailPreferences, new IApiRequestComplete<SettingsResponse>() {
            @Override
            public void onSuccess(SettingsResponse response) {
                if (response.isSuccess()) {
                    Toast.makeText(context, context.getString(R.string.settings_updated), Toast.LENGTH_SHORT).show();
                    user.getSettings().setSuggestionsOfTheDay(emailPreferences.getSettings().isSuggestionsOfTheDay());
                    user.getSettings().setNewsletters(emailPreferences.getSettings().isNewsletters());
                    user.getSettings().setNoEmails(emailPreferences.getSettings().isNoEmails());
                    ((Activity) context).finish();
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, context.getString(R.string.check_intenet_connection), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SettingsResponse response) {
                Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
