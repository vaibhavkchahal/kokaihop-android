package com.kokaihop.utility;

import android.graphics.Typeface;
import android.util.Log;

import com.kokaihop.KokaihopApplication;

import java.util.HashMap;


public class CustomFontFamily {
    static CustomFontFamily customFontFamily=null;
    HashMap<String,Typeface> fontCache=new HashMap<>();
    HashMap<String,String> fontMap=new HashMap<>();

    private CustomFontFamily() {
    }

    public static CustomFontFamily getInstance(){
        if(customFontFamily==null){
            customFontFamily=new CustomFontFamily();
        }
        return customFontFamily;
    }

    public void addFont(String alias, String fontName){
        fontMap.put(alias,fontName);
    }
    public Typeface getFont(String alias)
    {
        String fontFilename = fontMap.get(alias);
        if (fontFilename == null) {
            Log.e("", "Font not available with name " + alias);
            return null;
        }
        if(fontCache.containsKey(alias))
            return fontCache.get(alias);
        else
        {
            Typeface typeface = Typeface.createFromAsset(KokaihopApplication.getContext().getAssets(), fontFilename);
            fontCache.put(fontFilename, typeface);
            return typeface;
        }
    }
}
