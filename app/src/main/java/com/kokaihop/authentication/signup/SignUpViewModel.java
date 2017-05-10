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
import com.kokaihop.authentication.AuthenticationApiResponse;
import com.kokaihop.authentication.login.LoginActivity;
import com.kokaihop.city.CityLocation;
import com.kokaihop.city.SelectCityActivity;
import com.kokaihop.city.SignUpRequest;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.FacebookAuthentication;


public class SignUpViewModel extends BaseViewModel {

    private String name = "";
    private String userName = "";
    private String password = "";
    private String city = "";
    private SignUpRequest signUpRequest;
    private CityLocation cityLocation;
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

    public void setCityLocation(CityLocation cityLocation) {
        this.cityLocation = cityLocation;
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
        if (signUpValidations(context)) return;
        setProgressVisible(true);
        signUpSettings = new SignUpSettings(newsletter, suggestion);
        signUpRequest = new SignUpRequest(cityLocation, userName, name, password, signUpSettings);
        new AuthenticationApiHelper(view.getContext()).signup(signUpRequest, new IApiRequestComplete<AuthenticationApiResponse>() {
            @Override
            public void onSuccess(AuthenticationApiResponse response) {
                setProgressVisible(false);
                Toast.makeText(context, R.string.signup_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(AuthenticationApiResponse response) {
                String message = response.getErrorEmail().getDetail().getMessage();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
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
        if (city.isEmpty() && signUpRequest == null) {
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
