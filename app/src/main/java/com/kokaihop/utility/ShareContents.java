package com.kokaihop.utility;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajendra Singh on 14/6/17.
 */

public class ShareContents {
    private Context context;
    private String recipeLink;
    private String recipeTitle;
    private String emailSaticContent1 = "Jag vill tipsa dig om ett bra recept från kokaihop.se!";
    private String emailSaticContent2 = "Läs receptet här :";
    private String oneLinkText = "Ladda ned från: http://onelink.to/f6n497";
    private File imageFile;


    public ShareContents(Context context) {
        this.context = context;

    }

    public void setRecipeLink(String facebookContent) {
        this.recipeLink = facebookContent;
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
                if (packageName.equals("com.twitter.android") || packageName.equals("com.facebook.katana") || packageName.equals("com.android.mms") || packageName.equals("com.android.messaging") || packageName.equals("com.google.android.gm")) {
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
                    } else if (packageName.equals("com.android.mms") || packageName.equals("com.android.messaging")) {
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "kokaihop - " + recipeTitle);
                        intent.setType("image/*");
                        String message = getMessageContent();
//                        intent.putExtra(Intent.EXTRA_TEXT,  message);
                        intent.putExtra("sms_body", message);

                    } else if (packageName.equals("com.google.android.gm")) {
                        String emailContent = getEmailContent();
                        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(emailContent));
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "kokaihop - " + recipeTitle);
                        intent.setType("image/*");
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
        String sms = recipeTitle + "\n" + recipeLink + "\n" + oneLinkText;
        return sms;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }


    public String getEmailContent() {
        String emailContent = "<html><body><b>" + emailSaticContent1 + "</b><br/>" + recipeTitle + "<br/>" + emailSaticContent2 + "<a href=\"" + recipeLink + "\">" + recipeLink + "</a>"
                + "<br/> <br/>Ladda ned från: <br/> http://onelink.to/f6n497 </body></html>";
        return emailContent;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }
}
