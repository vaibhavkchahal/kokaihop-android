package com.kokaihop.home;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ShoppingListRecyclerItemBinding;
import com.kokaihop.database.IngredientsRealmObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vaibhav Chahal on 15/6/17.
 */

public class ShoppingListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<IngredientsRealmObject> ingredientsList = new ArrayList<>();
    private Context context;


    public ShoppingListRecyclerAdapter(List<IngredientsRealmObject> list) {
        ingredientsList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_recycler_item, parent, false);
        return new ViewHolderShowIngredients(v);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final ViewHolderShowIngredients holderShowComments = (ViewHolderShowIngredients) holder;
        final IngredientsRealmObject ingredientsRealmObject = ingredientsList.get(position);
        holderShowComments.binder.setModel(ingredientsRealmObject);
        holderShowComments.binder.setHandler(new ShoppingListHandler());
        holderShowComments.binder.executePendingBindings();
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
}