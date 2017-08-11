package com.kokaihop.home;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.DialogIntgredientUnitBinding;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.IngredientsRealmObject;
import com.kokaihop.database.ShoppingListRealmObject;
import com.kokaihop.database.Unit;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.Constants;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Vaibhav Chahal on 10/7/17.
 */
public class AddIngredientViewModel extends BaseViewModel {

    private String[] unitsArray;
    private ShoppingDataManager shoppingDataManager;
    private Context context;
    private BottomSheetDialog ingredientUnitDialog;
    private int unitDialogMinValue = 0;
    private int previousSelectedIndex;
    private UnitDataListener unitDataListener;
    private NumberPicker numberPicker;

    public AddIngredientViewModel(Context context, UnitDataListener unitDataListener) {
        this.context = context;
        this.unitDataListener = unitDataListener;
        shoppingDataManager = new ShoppingDataManager();
        ShoppingListRealmObject shoppingListRealmObject = shoppingDataManager.fetchShoppingRealmObject();
        prepareArrayForPickerDialog(context);
    }

    private void prepareArrayForPickerDialog(Context context) {
        List<Unit> unitRealmResults = shoppingDataManager.getIngredientUnits();
        unitsArray = new String[unitRealmResults.size() + 1];
        unitsArray[0] = context.getString(R.string.text_select);
        for (int i = 0; i < unitRealmResults.size(); i++) {
            unitsArray[i + 1] = unitRealmResults.get(i).getName();
        }
    }

    @Override
    protected void destroy() {
    }

    public void onBackPressed(Context context) {
        Activity activity = (Activity) context;
        activity.setResult(Activity.RESULT_OK);
        activity.finish();
    }

    public void onDoneClick(EditText ingredient, TextView unit, EditText value) {
        String ingredientName = ingredient.getText().toString();
        String amount = value.getText().toString();
        String unitName = unit.getText().toString();
        String unitId = null;
        if (previousSelectedIndex > 0) {
            unitId = shoppingDataManager.getIngredientUnits().get(previousSelectedIndex - 1).getId();
        }
        Activity activity = (Activity) context;
        // extras null means addding new ingredient.
        if (activity.getIntent().getExtras() == null) {
            ValidationsOnAddingNewIngredient(ingredient, unit, value, amount, unitName, unitId, activity);

        } else {
            validationOnEditIngredient(ingredientName, amount, unitName, unitId, activity);
        }
    }

    private void validationOnEditIngredient(String ingredientName, String amount, String unitName, String unitId, Activity activity) {
        String ingredientId = activity.getIntent().getStringExtra(Constants.INGREDIENT_ID);
        if (shoppingDataManager != null) {
            if (ingredientName.trim().length() > 0) {
                if (!validAmount(amount)) {
                    Toast.makeText(context, context.getString(R.string.invalid_quantity), Toast.LENGTH_LONG).show();
                } else {
                    float amountInFloat = 0;
                    amountInFloat = Float.valueOf(amount);
                    shoppingDataManager.updateIngredientObject(ingredientId, ingredientName, amountInFloat, unitName, unitId);
                    activity.setResult(Activity.RESULT_OK);
                    activity.finish();
                }
            } else {
                AppUtility.showOkDialog(context, context.getString(R.string.please_enter_ingredient), "");
            }
        }
    }

    private void ValidationsOnAddingNewIngredient(EditText ingredient, TextView unit, EditText value, String amount, String unitName, String unitId, Activity activity) {
        String nameOfIngredient = ingredient.getText().toString();
        if (nameOfIngredient.length() == 0 && unitName.equals(context.getString(R.string.text_select)) && value.getText().length() == 0) {
            activity.setResult(Activity.RESULT_OK);
            activity.finish();
        } else if (nameOfIngredient.length() == 0) {
            AppUtility.showOkDialog(context, context.getString(R.string.please_enter_ingredient), "");
        } else if (nameOfIngredient.length() > 0 && nameOfIngredient.trim().length() == 0) {
            Toast.makeText(context, R.string.text_invalid_ingredient, Toast.LENGTH_LONG).show();
        } else if (!validAmount(amount)) {
            Toast.makeText(context, context.getString(R.string.invalid_quantity), Toast.LENGTH_LONG).show();
        } else {
            IngredientsRealmObject ingredientsRealmObject = new IngredientsRealmObject();
            ingredientsRealmObject.setName(nameOfIngredient);
            ingredientsRealmObject.setAmount(Float.valueOf(amount));
            Calendar calendar = Calendar.getInstance();
            ingredientsRealmObject.setDateCreated(String.valueOf(calendar.getTimeInMillis()));
            ingredientsRealmObject.set_id(String.valueOf(calendar.getTimeInMillis()) + Constants.TEMP_INGREDIENT_ID_SIGNATURE);
            if (unitId != null) {
                Unit unitObj = new Unit();
                unitObj.setId(unitId);
                unitObj.setName(unitName);
                ingredientsRealmObject.setUnit(unitObj);
            }
            ingredientsRealmObject.setServerSyncNeeded(true);
            shoppingDataManager.addIngredientObjectToList(ingredientsRealmObject);
            GoogleAnalyticsHelper.trackEventAction(context.getString(R.string.buy_list_category), context.getString(R.string.buy_list_added_action), context.getString(R.string.buy_list_Ingredient_label));
            ingredient.setText("");
            unit.setText(context.getString(R.string.text_select));
            value.setText("");
            AppUtility.showAutoCancelMsgDialog(context, "");
        }
    }

    private boolean validAmount(String amount) {
        if (amount.isEmpty())
            return false;
        int decimalIndex = amount.indexOf(".");
        if (decimalIndex == -1) {
            if (amount.length() > 7)
                return false;
        } else {
            if (decimalIndex > 7)
                return false;
            if (amount.length() - decimalIndex > 3)
                return false;
        }
        return true;
    }

    public void showUnitListDialog(final Context context) {
        DialogIntgredientUnitBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_intgredient_unit, null, false);
        ingredientUnitDialog = setDialogConfigration(dialogBinding);
        numberPicker = dialogBinding.numberPickerPortion;
        numberPicker.setMinValue(unitDialogMinValue);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setDisplayedValues(unitsArray);
        numberPicker.setMaxValue(unitsArray.length - 1);
        numberPicker.setValue(previousSelectedIndex);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                unitDataListener.onUnitPickerValueChange(unitsArray[picker.getValue()]);
                previousSelectedIndex = picker.getValue();
            }
        });
        ingredientUnitDialog.getWindow().findViewById(R.id.textview_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientUnitDialog.dismiss();
            }
        });
        ingredientUnitDialog.show();
        AppUtility.setDividerHeight(dialogBinding.numberPickerPortion, Constants.NUMBER_PICKER_HEIGHT);

    }

    private BottomSheetDialog setDialogConfigration(DialogIntgredientUnitBinding binding) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(binding.getRoot());
        dialog.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
        //Grab the window of the ingredientUnitDialog, and change the width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the ingredientUnitDialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                // This is gotten directly from the source of BottomSheetDialog
                // in the wrapInBottomSheet() method
                FrameLayout bottomSheet = (FrameLayout) d.findViewById(android.support.design.R.id.design_bottom_sheet);
                // Right here!
                BottomSheetBehavior.from(bottomSheet)
                        .setPeekHeight(Constants.BOTTOM_SHET_DIALOG_PEEK_HEIGHT);
            }
        });
        return dialog;
    }

    public interface UnitDataListener {
        void onUnitPickerValueChange(String value);
    }

}
