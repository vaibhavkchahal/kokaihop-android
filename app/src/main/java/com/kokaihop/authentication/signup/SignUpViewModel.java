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
import com.kokaihop.authentication.FacebookAuthRequest;
import com.kokaihop.authentication.login.LoginActivity;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.city.CityActivity;
import com.kokaihop.city.CityLocation;
import com.kokaihop.city.SignUpRequest;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.FacebookAuthentication;
import com.kokaihop.utility.SharedPrefUtils;
import com.kokaihop.utility.ValidationUtils;

import static com.kokaihop.utility.Constants.EXTRA_FROM;


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
        signUpRequest = new SignUpRequest(cityLocation, userName, name, password, signUpSettings, "");
        new AuthenticationApiHelper(view.getContext()).signup(signUpRequest, new IApiRequestComplete<AuthenticationApiResponse>() {
            @Override
            public void onSuccess(AuthenticationApiResponse response) {
                setProgressVisible(false);
                SharedPrefUtils.setSharedPrefStringData(context, Constants.ACCESS_TOKEN, response.getToken());
                Toast.makeText(context, R.string.signup_success, Toast.LENGTH_SHORT).show();
//                boolean isComingFromLike = ((SignUpActivity) context).getIntent().getBooleanExtra("isComingFromLike", false);
                String from = ((SignUpActivity) context).getIntent().getStringExtra(EXTRA_FROM);

                if (from!=null && from.equals("loginRequired")) {
                    Toast.makeText(context, R.string.welcome_text, Toast.LENGTH_SHORT).show();
                    ((SignUpActivity) context).setResult(Activity.RESULT_OK);
                    ((SignUpActivity) context).finish();
                } else {
                    AppUtility.showHomeScreen(context);
                }
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(AuthenticationApiResponse response) {
                setProgressVisible(false);
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
        if (userName.isEmpty() || !ValidationUtils.isValidEmail(userName)) {
            Toast.makeText(context, R.string.invalid_email, Toast.LENGTH_SHORT).show();
            return true;
        }
        if (password.isEmpty() || password.length() < Constants.PASSWORD_MIN_LENGTH) {
            Toast.makeText(context, R.string.password_validation_msg, Toast.LENGTH_SHORT).show();
            return true;
        }
        if (city.isEmpty() && signUpRequest == null) {
            Toast.makeText(context, R.string.city_validation_msg, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public void closeSignup(View view) {
        ((Activity) view.getContext()).finish();
    }

    public void openLogin(View view) {
        ((Activity) view.getContext()).startActivity(new Intent(view.getContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }


    public void openCityScreen(View view) {
        Activity context = (Activity) view.getContext();
        ((Activity) view.getContext()).startActivityForResult(new Intent(context, CityActivity.class), REQUEST_CODE);
    }


    public void signUpWithFacebook(View view) {
        final Context context = view.getContext();
        FacebookAuthentication authentication = new FacebookAuthentication();
        authentication.facebookLogin(view, new FacebookAuthentication.FacebookResponseCallback() {
            @Override
            public void onSuccess(FacebookAuthRequest facebookAuthRequest) {
                setProgressVisible(true);
                new AuthenticationApiHelper(context).facebookloginSignup(facebookAuthRequest, new IApiRequestComplete<AuthenticationApiResponse>() {
                    @Override
                    public void onSuccess(AuthenticationApiResponse response) {
                        SharedPrefUtils.setSharedPrefStringData(context, Constants.ACCESS_TOKEN, response.getToken());
                        setProgressVisible(false);
                        Toast.makeText(context, R.string.signup_success, Toast.LENGTH_SHORT).show();
//                        boolean isComingFromLike = ((SignUpActivity) context).getIntent().getBooleanExtra("isComingFromLike", false);
                        String from = ((SignUpActivity) context).getIntent().getStringExtra(EXTRA_FROM);

                        if (from!=null && from.equals("loginRequired")) {
                            ((SignUpActivity) context).setResult(Activity.RESULT_OK);
                            ((SignUpActivity) context).finish();
                            Toast.makeText(context, R.string.welcome_text, Toast.LENGTH_SHORT).show();
                        } else {
                            AppUtility.showHomeScreen(context);
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        setProgressVisible(false);
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(AuthenticationApiResponse response) {
                        setProgressVisible(false);
                        String message = response.getErrorEmail().getDetail().getMessage();
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onfailure(String error) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            }
        });

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

    @Override
    protected void destroy() {
    }
}
