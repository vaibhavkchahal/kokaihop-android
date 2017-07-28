package com.kokaihop.utility;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.text.Html;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.analytics.GoogleAnalyticsHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

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
    private String ingredients;
    private String directions;
    private String recipeDescription;
    private WebView webView;
    private String packageName;
    private String className;

    public ShareContents(Context context) {
        this.context = context;

    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setRecipeLink(String facebookContent) {
        this.recipeLink = facebookContent;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    //    to share the picture with external applications
    public void shareCustom() {
        Log.i("Package Name", packageName);
        if (packageName.equals("com.twitter.android") || packageName.equals("com.facebook.katana") || packageName.equals("com.android.mms") || packageName.equals("com.android.messaging") || packageName.equals("com.google.android.gm") || packageName.equals("com.kokaihop.print")) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(packageName, className));
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            if (packageName.equals("com.twitter.android")) {
                intent.putExtra(Intent.EXTRA_TEXT, recipeLink);
                GoogleAnalyticsHelper.trackEventAction(context.getString(R.string.recipe_category), context.getString(R.string.recipe_shared_action), context.getString(R.string.recipe_twitter_label));
            } else if (packageName.equals("com.facebook.katana")) {
                // Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
                // One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they want to share. We can also make a custom landing page, and the link
                // will show the <meta content ="..."> text from that page with our link in Facebook.
                intent.putExtra(Intent.EXTRA_TEXT, recipeLink);
                GoogleAnalyticsHelper.trackEventAction(context.getString(R.string.recipe_category), context.getString(R.string.recipe_shared_action), context.getString(R.string.recipe_facebook_label));
            } else if (packageName.equals("com.android.mms") || packageName.equals("com.android.messaging")) {
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
                intent.putExtra(Intent.EXTRA_SUBJECT, "kokaihop - " + recipeTitle);
                intent.setType("image/*");
                String message = getMessageContent();
                intent.putExtra("sms_body", message);
                GoogleAnalyticsHelper.trackEventAction(context.getString(R.string.recipe_category), context.getString(R.string.recipe_shared_action), context.getString(R.string.recipe_sms_label));

            } else if (packageName.equals("com.google.android.gm")) {
                String emailContent = getEmailContent();
                intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(emailContent));
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
                intent.putExtra(Intent.EXTRA_SUBJECT, "kokaihop - " + recipeTitle);
                intent.setType("image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                GoogleAnalyticsHelper.trackEventAction(context.getString(R.string.recipe_category), context.getString(R.string.recipe_shared_action), context.getString(R.string.recipe_email_label));

            } else if (packageName.equals("com.kokaihop.print")) {
                GoogleAnalyticsHelper.trackEventAction(context.getString(R.string.recipe_category), context.getString(R.string.recipe_printed_action));
                doWebViewPrint();
            }
            if (!className.equals("print")) {
                context.startActivity(intent);
            }
        }
    }

    private void doWebViewPrint() {
        // Create a WebView object specifically for printing
        WebView webView = new WebView(context);
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "page finished loading " + url);
                createWebPrintJob(view);
                ShareContents.this.webView = null;
            }
        });
        // Generate an HTML document on the fly:
        String htmlDocument = "<html><body>"
                + "<h2>" + recipeTitle + "</h2>"
                + "<p>" + recipeDescription + "</p>"
                + "<h2>" + context.getString(R.string.text_Ingredients) + "</h2>"
                + "<p>" + ingredients + "</p>"
                + "<h2>" + context.getString(R.string.text_directions) + "</h2>"
                + "<p>" + directions + "</p>"
                + "</body></html>";
        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null);
        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        // to the PrintManager
        this.webView = webView;
    }

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


    private void createWebPrintJob(WebView webView) {
        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) context
                .getSystemService(Context.PRINT_SERVICE);
        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();
        // Create a print job with name and adapter instance
        String jobName = context.getString(R.string.app_name) + " Document";
        PrintJob printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
//        // Save the job object for later status checking
//        mPrintJobs.add(printJob);
    }
}
