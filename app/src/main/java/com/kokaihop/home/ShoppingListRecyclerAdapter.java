package com.kokaihop.home;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ShoppingListRecyclerItemBinding;
import com.kokaihop.cookbooks.model.ItemEditor;
import com.kokaihop.database.IngredientsRealmObject;

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
    private ImageView imageViewDelete;

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
        holderShowIngredients.binder.imageviewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(position, v);
            }
        });
        imageViewDelete = holderShowIngredients.binder.ivDelete;
        holderShowIngredients.binder.setHandler(new ShoppingListHandler());
        holderShowIngredients.binder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previousDelete >= 0) {
                    holderShowIngredients.binder.tvDelete.setVisibility(View.GONE);
                    viewModel.getShoppingDataManager().updateIngredientDeleteFlag(ingredientsList.get(previousDelete), false);
                    imageView.setVisibility(View.VISIBLE);
                }
                previousDelete = holderShowIngredients.getAdapterPosition();
                imageView = holderShowIngredients.binder.ivDelete;
                imageView.setVisibility(View.GONE);
                holderShowIngredients.binder.tvDelete.setVisibility(View.VISIBLE);
                viewModel.getShoppingDataManager().updateIngredientDeleteFlag(ingredientsRealmObject, true);
            }
        });
        holderShowIngredients.binder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientId = ingredientsRealmObject.get_id();
//                viewModel.removeRecipeFromCookbook(recipe.get_id(), getAdapterPosition());
            }
        });
        holderShowIngredients.binder.executePendingBindings();
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

   /* public void removeDeleteButton() {
        for (IngredientsRealmObject object : ingredientsList) {
            imageViewDelete.setVisibility(View.GONE);
            viewModel.getShoppingDataManager().updateIngredientDeleteFlag(object, false);
        }
    }*/

    public interface RecyclerOnItemClickListener {
        void onItemClick(int position, View view);
    }

}