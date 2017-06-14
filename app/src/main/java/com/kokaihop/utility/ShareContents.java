package com.kokaihop.utility;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajendra Singh on 14/6/17.
 */

public class ShareContents {
    private Context context;
    private String recipeLink;
    private String recipeTitle;
    private String oneLinkText = "Ladda ned från: http://onelink.to/f6n497";

    private String emailContent;

    public ShareContents(Context context) {
        this.context = context;

    }


    public void setRecipeLink(String facebookContent) {
        this.recipeLink = facebookContent;
    }


    //    to share the picture with external applications
    public void share(String imageUrl) {
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
                if (packageName.equals("com.twitter.android") || packageName.equals("com.facebook.katana") || packageName.equals("com.android.mms") || packageName.equals("com.google.android.gm")) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");

                    if (packageName.equals("com.twitter.android")) {
                        intent.putExtra(Intent.EXTRA_TEXT, recipeLink);
                    } else if (packageName.equals("com.facebook.katana")) {
                        // Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
                        // One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they want to share. We can also make a custom landing page, and the link
                        // will show the <meta content ="..."> text from that page with our link in Facebook.
                        intent.putExtra(Intent.EXTRA_TEXT, recipeLink);
                    } else if (packageName.equals("com.android.mms")) {
                        intent.putExtra(Intent.EXTRA_SUBJECT, "kokaihop");
                        intent.putExtra(Intent.EXTRA_TEXT, "share on sms"/*resources.getString(R.string.share_sms)*/);
                    } else if (packageName.equals("com.google.android.gm")) {
                        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(emailContent));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "kokaihop - " + recipeTitle);
                        intent.setType("message/rfc822");
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

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }
}
