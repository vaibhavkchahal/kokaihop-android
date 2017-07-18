package com.kokaihop.home;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ShoppingListRecyclerItemBinding;
import com.kokaihop.cookbooks.model.ItemEditor;
import com.kokaihop.database.IngredientsRealmObject;
import com.kokaihop.utility.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vaibhav Chahal on 15/6/17.
 */

public class ShoppingListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<IngredientsRealmObject> ingredientsList = new ArrayList<>();
    private Context context;
    private RecyclerOnItemClickListener clickListener;
    private ItemEditor itemEditor;
    private int previousDelete = -1;
    private ImageView imageView;
    private ShoppingListViewModel viewModel;
    private String ingredientId;
    private TextView textViewDelete;

    public ShoppingListRecyclerAdapter(List<IngredientsRealmObject> list, RecyclerOnItemClickListener clickListener) {
        ingredientsList = list;
        this.clickListener = clickListener;
        itemEditor = new ItemEditor();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_recycler_item, parent, false);
        return new ViewHolderShowIngredients(v);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ViewHolderShowIngredients holderShowIngredients = (ViewHolderShowIngredients) holder;
        final IngredientsRealmObject ingredientsRealmObject = ingredientsList.get(position);
        holderShowIngredients.binder.setModel(ingredientsRealmObject);
        holderShowIngredients.binder.setItemEditor(itemEditor);
        setMarkOnText(holderShowIngredients, ingredientsRealmObject);
        setEditImageClickListener(position, holderShowIngredients);
        holderShowIngredients.binder.setHandler(new ShoppingListHandler());
        setMinusButtonClick(holderShowIngredients);
        setDeleteClick(holderShowIngredients, ingredientsRealmObject);
        setWholeRowItemClick(holderShowIngredients, ingredientsRealmObject);
        holderShowIngredients.binder.executePendingBindings();
    }

    private void setWholeRowItemClick(final ViewHolderShowIngredients holderShowIngredients, final IngredientsRealmObject ingredientsRealmObject) {
        holderShowIngredients.binder.relativeLayoutListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View divider = holderShowIngredients.binder.viewVerticalDivider;
                ImageView imgviewEdit = holderShowIngredients.binder.imageviewEdit;
                TextView name = holderShowIngredients.binder.txtviewIngredentName;
                if (divider.getVisibility() == View.VISIBLE) {
                    view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.white_FFF7F7F7));
                    divider.setVisibility(View.GONE);
                    imgviewEdit.setImageResource(R.drawable.ic_tick_sm);
                    imgviewEdit.setTag(Constants.MARKED_INGREDIENT_TAG);
                    viewModel.getShoppingDataManager().markIngredientObjectInDB(ingredientsRealmObject.get_id());
                    viewModel.getMarkedIds().add(ingredientsRealmObject.get_id());
                    name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.white));
                    divider.setVisibility(View.VISIBLE);
                    name.setPaintFlags(name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    imgviewEdit.setImageResource(R.drawable.ic_edit_md);
                    imgviewEdit.setTag(Constants.EDIT_INGGREDIENT_TAG);
                    viewModel.getMarkedIds().remove(ingredientsRealmObject.get_id());
                    viewModel.getShoppingDataManager().UnMarkIngredientObjectInDB(ingredientsRealmObject.get_id());
                }
            }
        });
    }

    private void setDeleteClick(ViewHolderShowIngredients holderShowIngredients, final IngredientsRealmObject ingredientsRealmObject) {
        holderShowIngredients.binder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientId = ingredientsRealmObject.get_id();
                List<String> ids = new ArrayList<String>();
                ids.add(ingredientId);
                viewModel.getShoppingDataManager().deleteIngredientObjectFromDB(ids);
                viewModel.fetchIngredientFromDB();
                viewModel.deleteIngredientOnServer();
            }
        });
    }

    private void setMinusButtonClick(final ViewHolderShowIngredients holderShowIngredients) {
        holderShowIngredients.binder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previousDelete >= 0) {
                    textViewDelete.setVisibility(View.GONE);
//                    viewModel.getShoppingDataManager().updateIngredientDeleteFlag(ingredientsList.get(previousDelete), false);
                    imageView.setVisibility(View.VISIBLE);
                }
                previousDelete = holderShowIngredients.getAdapterPosition();
                imageView = holderShowIngredients.binder.ivDelete;
                textViewDelete = holderShowIngredients.binder.tvDelete;
                imageView.setVisibility(View.GONE);
                textViewDelete.setVisibility(View.VISIBLE);
//                viewModel.getShoppingDataManager().updateIngredientDeleteFlag(ingredientsRealmObject, true);
            }
        });
    }

    private void setEditImageClickListener(final int position, ViewHolderShowIngredients holderShowIngredients) {
        holderShowIngredients.binder.imageviewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(position, v);
            }
        });
    }

    private void setMarkOnText(ViewHolderShowIngredients holderShowIngredients, IngredientsRealmObject ingredientsRealmObject) {
        TextView name = holderShowIngredients.binder.txtviewIngredentName;
        if (ingredientsRealmObject.isIngredientMarked()) {
            name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            name.setPaintFlags(name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }


    public class ViewHolderShowIngredients extends RecyclerView.ViewHolder {
        public ShoppingListRecyclerItemBinding binder;

        public ViewHolderShowIngredients(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);
        }
    }

    public ShoppingListViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(ShoppingListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setIndgredientEditor(boolean isEditIngredient) {
        itemEditor.setEditMode(isEditIngredient);
    }

    public interface RecyclerOnItemClickListener {
        void onItemClick(int position, View view);
    }

}