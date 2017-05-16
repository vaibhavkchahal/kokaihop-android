package com.kokaihop.feed;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.RecipeRecyclerItemBinding;
import com.kokaihop.database.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vaibhav Chahal on 15/5/17.
 */

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeRecyclerAdapter.ViewHolder> {

    private List<Recipe> recipeList = new ArrayList<>();

    public RecipeRecyclerAdapter(List<Recipe> list) {
        recipeList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.binder.setRecipe(recipe);
        holder.binder.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RecipeRecyclerItemBinding binder;

        public ViewHolder(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);
        }
    }
}