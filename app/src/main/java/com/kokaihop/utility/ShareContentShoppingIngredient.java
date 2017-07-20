package com.kokaihop.utility;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;

import com.kokaihop.database.IngredientsRealmObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajendra Singh on 14/6/17.
 */

public class ShareContentShoppingIngredient {
    private Context context;
    private List<IngredientsRealmObject> shoppingIngredientsList;
    private String emailStaticText = "Kokaihop.se är Sveriges största nätverk för hemmakockar med över 30 000 recept.Ladda ned vår app eller besök oss på Kokaihop.se så har du alltid tillgång till bra recept, helt gratis.";
    private String sharingTitle = "Inköpslista:";
    private String oneLinkText = "Ladda ned från: http://onelink.to/f6n497";

    public ShareContentShoppingIngredient(Context context) {
        this.context = context;

    }

    public void setShoppingIngredient(List<IngredientsRealmObject> shoppingIngredient) {
        this.shoppingIngredientsList = shoppingIngredient;
    }

    //    to share the picture with external applications
    public void share() {
        List<Intent> targetShareIntents = new ArrayList<Intent>();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        List<ResolveInfo> resInfos = context.getPackageManager().queryIntentActivities(shareIntent, 0);
        if (!resInfos.isEmpty()) {
            System.out.println("Have package");
            for (ResolveInfo resInfo : resInfos) {
                String packageName = resInfo.activityInfo.packageName;
                Log.i("Package Name", packageName);
                if (packageName.equals("com.android.mms") || packageName.equals("com.android.messaging") || packageName.equals("com.google.android.gm")) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    if (packageName.equals("com.android.mms") || packageName.equals("com.android.messaging")) {
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Inköpslista");
                        String message = getMessageContent();
                        intent.putExtra("sms_body", message);

                    } else if (packageName.equals("com.google.android.gm")) {
                        String emailContent = getEmailContent();
                        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(emailContent));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Inköpslista");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    intent.setPackage(packageName);
                    targetShareIntents.add(intent);
                }
            }
            if (!targetShareIntents.isEmpty()) {
                System.out.println("Have Intent");
                Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "Choose app to share");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
                context.startActivity(chooserIntent);
            } else {
                System.out.println("Do not Have Intent");
            }
        }
    }

    private String getMessageContent() {
        String listContent = getIngredientsString(false);
        String sms = sharingTitle + "\n" + "\n" + listContent + "\n" + oneLinkText;
        return sms;
    }

    @NonNull
    private String getIngredientsString(boolean isRequiredListForEmail) {
        String lineBreaker;
        if (isRequiredListForEmail) {
            lineBreaker = "<br/>";
        } else {
            lineBreaker = "\n";
        }
        String listContent = "";
        for (IngredientsRealmObject item : shoppingIngredientsList) {
            if (item.getAmount() > 0) {
                listContent = listContent + item.getAmount() + " " + item.getUnit().getName() + " " + item.getName() + lineBreaker;
            } else {
                listContent = listContent + item.getName() + lineBreaker;
            }
        }
        return listContent;
    }

    public String getEmailContent() {
        String emailContent = "<html><body>" + sharingTitle + "<br/>" + "<br/>" + getIngredientsString(true) + "<br/>" + emailStaticText +
                "<html><body>Ladda ned från: http://onelink.to/f6n497 </body></html>";
        return emailContent;
    }

}
