package com.kokaihop.authentication.signup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.BR;
import com.altaworks.kokaihop.ui.R;
import com.kokaihop.authentication.AuthenticationApiHelper;
import com.kokaihop.authentication.login.LoginActivity;
import com.kokaihop.authentication.login.LoginApiResponse;
import com.kokaihop.city.SelectCityActivity;
import com.kokaihop.city.SignUpCityLocation;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.BaseViewModel;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.FacebookAuthentication;


public class SignUpViewModel extends BaseViewModel {

    private String name;
    private String userName;
    private String password;
    private String city;
    private SignUpCityLocation cityLocation;
    private SignUpSettings signUpSettings;
    private int newsletter;
    private int suggestion;
    public static final int REQUEST_CODE = 10;

    @Bindable
    public int getNewsletter() {
        return newsletter;
    }

    public void setNewsletter(int newsletterSelected) {
        newsletter = newsletterSelected;
        notifyPropertyChanged(BR.newsletter);
    }

    @Bindable
    public int getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(int suggestionSelected) {
        suggestion = suggestionSelected;
        notifyPropertyChanged(BR.suggestion);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        notifyPropertyChanged(BR.city);
    }

    public SignUpCityLocation getCityLocation() {
        return cityLocation;
    }

    public void setCityLocation(SignUpCityLocation cityLocation) {
        this.cityLocation = cityLocation;
    }

    public SignUpSettings getSignUpSettings() {
        return signUpSettings;
    }

    public void setSignUpSettings(SignUpSettings signUpSettings) {
        this.signUpSettings = signUpSettings;
    }

    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        this.notifyPropertyChanged(BR.userName);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.notifyPropertyChanged(BR.password);
    }

    public void signup(View view) {
        final Context context = view.getContext();

        if (cityLocation!=null){
            setCity(cityLocation.getCityLocation().getCityDetails().getName());
        }

        if (signUpValidations(context)) return;
        setProgressVisible(true);
        signUpSettings = new SignUpSettings(newsletter, suggestion);
        new AuthenticationApiHelper(view.getContext()).signup(name, userName, password, cityLocation, signUpSettings, new IApiRequestComplete<LoginApiResponse>() {
            @Override
            public void onSuccess(LoginApiResponse response) {
                setProgressVisible(false);
                Toast.makeText(context, R.string.sucess_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
        view.getContext().startActivity(new Intent(context, LoginActivity.class));
    }

    private boolean signUpValidations(Context context) {
        if (name.isEmpty()) {
            Toast.makeText(context, R.string.name_required, Toast.LENGTH_SHORT).show();
            return true;
        }
        if (userName.isEmpty() || !AppUtility.isValidEmail(userName)) {
            Toast.makeText(context, R.string.invalid_email, Toast.LENGTH_SHORT).show();
            return true;
        }
        if (password.isEmpty() || password.length() < Constants.PASSWORD_MAX_LENGTH) {
            Toast.makeText(context, R.string.password_validation_msg, Toast.LENGTH_SHORT).show();
            return true;
        }
        if (city.isEmpty() && cityLocation == null) {
            Toast.makeText(context, R.string.city_validation_msg, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public void openLoginScreen(View view) {
        Context context = view.getContext();
        view.getContext().startActivity(new Intent(context, LoginActivity.class));
    }

    public void openCityScreen(View view) {
        Activity context = (Activity) view.getContext();
        ((Activity) view.getContext()).startActivityForResult(new Intent(context, SelectCityActivity.class), REQUEST_CODE);
    }

    public void signUpWithFacebook(final View view) {
        FacebookAuthentication authentication = new FacebookAuthentication();
        authentication.facebookLogin(view);
    }

    public void onCheckChangeNewsletter(CheckBox checkBox) {
        if (checkBox.isChecked()) {
            setNewsletter(1);
        } else {
            setNewsletter(0);
        }
    }

    public void onCheckChangeSuggestion(CheckBox checkBox) {
        if (checkBox.isChecked()) {
            setSuggestion(1);
        } else {
            setSuggestion(0);
        }
    }
}
