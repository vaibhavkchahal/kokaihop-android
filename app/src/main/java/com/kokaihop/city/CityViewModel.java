package com.kokaihop.city;

import android.content.Context;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.network.IApiRequestComplete;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public class CityViewModel extends BaseViewModel {
    private ArrayList<CityDetails> cityList = new ArrayList<>();
    private CityInterface cityInterface;
    Context context;

    public CityViewModel(CityInterface cityInterface) {
        getCities();
        this.cityInterface = cityInterface;
        this.context = (Context) cityInterface;
    }

    public ArrayList<CityDetails> getCityList() {
        return cityList;
    }

    public void setCityList(ArrayList<CityDetails> cityList) {
        this.cityList = cityList;
    }

    public void setCities(ArrayList<CityDetails> cityList) {
        this.cityList = cityList;
        cityInterface.setCitiesOnRecyclerView();
        //Select City
    }

    public void getCities() {
        setProgressVisible(true);
        new CityApiHelper().getCities(new IApiRequestComplete<CitiesApiResponse>() {
            @Override
            public void onSuccess(CitiesApiResponse response) {
                setProgressVisible(false);
                if (response != null) {
                    setCities(response.getCityDetailsList());
                }
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);
                Toast.makeText(context,context.getString(R.string.check_intenet_connection),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(CitiesApiResponse response) {
                setProgressVisible(false);
                Toast.makeText(context,context.getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void destroy() {

    }

    public interface CityInterface {
        void citySelected(CityDetails selectedCity);

        void setCitiesOnRecyclerView();
    }

}

