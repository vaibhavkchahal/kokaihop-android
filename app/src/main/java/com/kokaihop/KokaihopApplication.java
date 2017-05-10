package com.kokaihop;

import android.app.Application;
import android.content.Context;

import com.kokaihop.utility.CustomFontFamily;

import io.realm.Realm;

/**
 * Created by Rajendra Singh on 4/5/17.
 */

public class KokaihopApplication extends Application {

    private static Context context;
    CustomFontFamily customFontFamily;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        context = this;
        customFontFamily=CustomFontFamily.getInstance();

        customFontFamily.addFont("RS-Bold", "fonts/RobotoSlab-Bold.ttf");
        customFontFamily.addFont("RS-Light", "fonts/RobotoSlab-Light.ttf");
        customFontFamily.addFont("RS-Regular", "fonts/RobotoSlab-Regular.ttf");
        customFontFamily.addFont("RS-Thin", "fonts/RobotoSlab-Thin.ttf");
        customFontFamily.addFont("SS-ProBlack", "fonts/source-sans-pro.black.ttf");
        customFontFamily.addFont("SS-ProBlackItalic", "fonts/source-sans-pro.black-italic.ttf");
        customFontFamily.addFont("SS-ProBold", "fonts/source-sans-pro.bold.ttf");
        customFontFamily.addFont("SS-ProBoldItalic", "fonts/source-sans-pro.bold-italic.ttf");
        customFontFamily.addFont("SS-ProExtraLight", "fonts/source-sans-pro.extralight.ttf");
        customFontFamily.addFont("SS-ProExtraLightItalic", "fonts/source-sans-pro.extralight-italic.ttf");
        customFontFamily.addFont("SS-ProItalic", "fonts/source-sans-pro.italic.ttf");
        customFontFamily.addFont("SS-ProLight", "fonts/source-sans-pro.light.ttf");
        customFontFamily.addFont("SS-ProRegular", "fonts/source-sans-pro.regular.ttf");
        customFontFamily.addFont("SS-ProSemiBold", "fonts/source-sans-pro.semibold.ttf");
        customFontFamily.addFont("SS-ProSemiBoldItalic", "fonts/source-sans-pro.semibold-italic.ttf");
    }

    public static Context getContext() {
        return context;
    }
}
