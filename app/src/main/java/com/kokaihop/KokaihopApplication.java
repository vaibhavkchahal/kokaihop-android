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

        customFontFamily.addFont("RS-Bold", "RobotoSlab-Bold.ttf");
        customFontFamily.addFont("RS-Light", "RobotoSlab-Light.ttf");
        customFontFamily.addFont("RS-Regular", "RobotoSlab-Regular.ttf");
        customFontFamily.addFont("RS-Thin", "RobotoSlab-Thin.ttf");
        customFontFamily.addFont("SS-ProBlack", "source-sans-pro.black.ttf");
        customFontFamily.addFont("SS-ProBlackItalic", "source-sans-pro.black-italic.ttf");
        customFontFamily.addFont("SS-ProBold", "source-sans-pro.bold.ttf");
        customFontFamily.addFont("SS-ProBoldItalic", "source-sans-pro.bold-italic.ttf");
        customFontFamily.addFont("SS-ProExtraLight", "source-sans-pro.extralight.ttf");
        customFontFamily.addFont("SS-ProExtraLightItalic", "source-sans-pro.extralight-italic.ttf");
        customFontFamily.addFont("SS-ProItalic", "source-sans-pro.italic.ttf");
        customFontFamily.addFont("SS-ProLight", "source-sans-pro.light.ttf");
        customFontFamily.addFont("SS-ProRegular", "source-sans-pro.regular.ttf");
        customFontFamily.addFont("SS-ProSemiBold", "source-sans-pro.semibold.ttf");
        customFontFamily.addFont("SS-ProSemiBoldItalic", "source-sans-pro.semibold-italic.ttf");
    }

    public static Context getContext() {
        return context;
    }
}
