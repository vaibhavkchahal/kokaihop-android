package com.kokaihop;

import android.app.Application;
import android.content.Context;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.utility.CustomFontFamily;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.internal.IOException;

import static com.kokaihop.database.DBConstants.SCHEMA_VERSION;

/**
 * Created by Rajendra Singh on 4/5/17.
 */

public class KokaihopApplication extends Application {

    private static Context context;
    CustomFontFamily customFontFamily;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        //Set true to overwrite database - Optional
        boolean overwriteDatabase = false;

        if (overwriteDatabase) {
            copyBundledRealmFile(this.getResources().openRawResource(R.raw.kokaihop), Realm.DEFAULT_REALM_NAME);
        } else {
            //check if the db is already in place
            if (!fileFound(Realm.DEFAULT_REALM_NAME, this.getFilesDir())) {
                copyBundledRealmFile(this.getResources().openRawResource(R.raw.kokaihop), Realm.DEFAULT_REALM_NAME);
            }
        }
        //Config Realm for the application
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .schemaVersion(SCHEMA_VERSION)
                .name(Realm.DEFAULT_REALM_NAME)
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);

       /* Realm.init(this);

        copyBundledRealmFile(this.getResources().openRawResource(R.raw.kokaihop), Realm.DEFAULT_REALM_NAME); // more info about this is said later
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(DBConstants.SCHEMA_VERSION + 1)
                .deleteRealmIfMigrationNeeded()
                .build();
//        Realm.deleteRealm(config);
        Realm.setDefaultConfiguration(config);*/
        customFontFamily = CustomFontFamily.getInstance();

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


    private String copyBundledRealmFile(InputStream inputStream, String outFileName) {
        try {
            File file = new File(this.getFilesDir(), outFileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean fileFound(String name, File file) {
        File[] list = file.listFiles();
        if (list != null)
            for (File fil : list) {
                if (fil.isDirectory()) {
                    fileFound(name, fil);
                } else if (name.equalsIgnoreCase(fil.getName())) {
                    return true;
                }
            }
        return false;
    }
}
