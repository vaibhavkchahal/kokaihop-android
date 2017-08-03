package com.kokaihop.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.kokaihop.authentication.login.LoginActivity;
import com.kokaihop.database.IngredientsRealmObject;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.home.HomeActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.kokaihop.KokaihopApplication.getContext;


/**
 * Created by Rajendra Singh on 19/5/17.
 */

public class AppUtility {
    // An ad is placed in every nth position in the RecyclerView.
    public static final int ITEMS_PER_AD = 7;
    // First ad to be dispaly at 3rd position
    public static final int FIRST_AD_PLACE = 3;

    public static void showHomeScreen(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public static Point getDisplayPoint(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static int getHeightInAspectRatio(int width, float ratio) {
        float height = (width * ratio);
        return (int) height;
    }

    public static void showLoginDialog(final Context context, String title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.EXTRA_FROM, "loginRequired");
                ((Activity) context).startActivityForResult(intent, 1);
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static void hideKeyboard(View view) {
        // Check if no view has focus:
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showAutoCancelMsgDialog(final Context context, final String message) {
        if (context != null) {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom_rating_success_view);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView textView = (TextView) dialog.findViewById(R.id.txtview_rating_sucess);
            if (message.isEmpty()) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setText(message);
            }
            dialog.show();
            hideDialogAfterTimeOut(dialog);
        }
    }


    private static void hideDialogAfterTimeOut(final Dialog dialog) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if ((dialog != null) && (dialog.isShowing())) {
                    dialog.dismiss();
                }
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 2000);
    }

    public static void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void addAdvtInRecipeList(List<Object> recipeListWithAdds, String[] adsUNitId, Context context) {
        int adUnitIdPostion = 0;
        int firstPlaceAdd;
        int itemsPerAdd;
        if (AppUtility.isTablet10Inch(getContext())) {
            firstPlaceAdd = Constants.TAB_10_INCH_FIRST_ADD_POSITION;
            itemsPerAdd = Constants.TAB_10_ADD_REPEAT_POSITION;
        } else if (AppUtility.isTablet7Inch(getContext())) {
            if (AppUtility.isModePortrait(getContext())) {
                firstPlaceAdd = Constants.TAB_7_INCH_FIRST_ADD_POSITION_PORT;
                itemsPerAdd = Constants.TAB_7_ADD_REPEAT_POSITION_PORT;
            } else {
                firstPlaceAdd = Constants.TAB_7_INCH_FIRST_ADD_POSITION_LAND;
                itemsPerAdd = Constants.TAB_7_ADD_REPEAT_POSITION_LAND;
            }

        } else {
            firstPlaceAdd = Constants.PHONE_FIRST_ADD_POSITION;
            itemsPerAdd = Constants.PHONE_ADD_REPEAT_POSITION;
        }
        for (int recipeCount = firstPlaceAdd; recipeCount < recipeListWithAdds.size(); recipeCount += itemsPerAdd) {
            if (adUnitIdPostion > 2) {
                adUnitIdPostion = 0;
            }
            AdView adView = new AdView(context);
            if (adUnitIdPostion == 0) {
                adView.setAdSize(AdSize.LARGE_BANNER); //320x100 LARGE_BANNER
            } else {
                adView.setAdSize(AdSize.MEDIUM_RECTANGLE); //320x250 medium rectangle
            }
            adView.setAdUnitId(adsUNitId[adUnitIdPostion]);
            recipeListWithAdds.add(recipeCount, adView);
            adUnitIdPostion++;
        }

    }

    public static void showOkDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void updateRecipeItemView(RecipeRealmObject recipe, GridLayoutManager gridLayoutManager, RecyclerView recyclerView, List<Object> recipeList) {
        if (gridLayoutManager != null) {
            int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
            int lastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPosition();
            for (int i = firstVisibleItemPosition; i <= lastVisibleItemPosition; i++) {
                Object object = recipeList.get(i);
                if (object instanceof RecipeRealmObject) {
                    RecipeRealmObject recipeRealmObject = (RecipeRealmObject) object;
                    if (recipeRealmObject.getFriendlyUrl().equals(recipe.getFriendlyUrl())) {
                        recipeRealmObject.setFavorite(recipe.isFavorite());
                        recipeRealmObject.getCounter().setLikes(recipe.getCounter().getLikes());
                        recyclerView.getAdapter().notifyItemChanged(i);
                        EventBus.getDefault().removeStickyEvent(recipe);
                        break;
                    }
                }

            }
        }
    }

    public static boolean isEmptyString(String string) {
        if (string == null || string.trim().length() == 0)
            return true;
        return false;
    }

    public static String checkIfUnitExist(IngredientsRealmObject object) {
        String existingIngredientObjectKey;
        if (object.getUnit() != null) {
            existingIngredientObjectKey = object.getName() + object.getUnit().getName();
        } else {
            existingIngredientObjectKey = object.getName();
        }
        return existingIngredientObjectKey;
    }

    public static void showCoachMark(View view) {
        final Dialog dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //for dismissing anywhere you touch
        View masterView = dialog.findViewById(R.id.parent);
        masterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static boolean isTablet7Inch(Context context) {
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return large;
    }

    public static boolean isTablet10Inch(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
        return xlarge;
    }

    public static boolean isModePortrait(Context context) {
        int intOrientation = context.getResources().getConfiguration().orientation;
        if (intOrientation == Configuration.ORIENTATION_PORTRAIT) {
            return true;
        } else {
            return false;
        }
    }

    public static int getColumnsAccToScreenSize() {
        int numOfColumnInGrid;
        if (AppUtility.isTablet10Inch(getContext())) {
            numOfColumnInGrid = 4;
        } else if (AppUtility.isTablet7Inch(getContext())) {
            if (AppUtility.isModePortrait(getContext())) {
                numOfColumnInGrid = 3;
            } else {
                numOfColumnInGrid = 5;
            }
        } else {
            numOfColumnInGrid = 2;
        }
        return numOfColumnInGrid;
    }

    public static void setDividerHeight(NumberPicker picker, int height) {
        Context context = picker.getContext();
        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDividerHeight")) {
                pf.setAccessible(true);
                try {
                    pf.set(picker, height);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
