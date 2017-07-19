package com.kokaihop.home;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.DialogIntgredientUnitBinding;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.IngredientsRealmObject;
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
//        ((Activity) context).finish();
    }

    public void onDoneClick(EditText ingredient, TextView unit, EditText value) {
        String ingredientName = ingredient.getText().toString();
        float amount = Float.valueOf(value.getText().toString());
        String unitName = unit.getText().toString();
        String unitId = shoppingDataManager.getIngredientUnits().get(previousSelectedIndex - 1).getId();
        Activity activity = (Activity) context;
        // extras null means addding new ingredient.
        if (activity.getIntent().getExtras() == null) {
            IngredientsRealmObject ingredientsRealmObject = new IngredientsRealmObject();
            ingredientsRealmObject.setName(ingredientName);
            ingredientsRealmObject.setAmount(amount);
            Calendar calendar = Calendar.getInstance();
            ingredientsRealmObject.setDateCreated(String.valueOf(calendar.getTimeInMillis()));
            ingredientsRealmObject.set_id(String.valueOf(calendar.getTimeInMillis()) + Constants.TEMP_INGREDIENT_ID_SIGNATURE);
            Unit unitObj = new Unit();
            unitObj.setId(unitId);
            unitObj.setName(unitName);
            ingredientsRealmObject.setUnit(unitObj);
            ingredientsRealmObject.setServerSyncNeeded(true);
            shoppingDataManager.addIngredientObjectToList(ingredientsRealmObject);
            ingredient.setText("");
            unit.setText(context.getString(R.string.text_select));
            value.setText("");
            AppUtility.showAutoCancelMsgDialog(context, "");
        } else {
            String ingredientId = activity.getIntent().getStringExtra(Constants.INGREDIENT_ID);
            if (shoppingDataManager != null) {
                shoppingDataManager.updateIngredientObject(ingredientId, ingredientName, amount, unitName, unitId);
                activity.setResult(Activity.RESULT_OK);
                activity.finish();
            }
        }
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
        ingredientUnitDialog.show();

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
        return dialog;
    }

    public interface UnitDataListener {
        void onUnitPickerValueChange(String value);
    }

}
