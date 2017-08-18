package com.kokaihop.userprofile;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.RowHistoryRecipeBinding;
import com.bumptech.glide.Glide;
import com.kokaihop.cookbooks.CookbookDetailViewModel;
import com.kokaihop.cookbooks.model.ItemEditor;
import com.kokaihop.database.RecipeHistoryRealmObject;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.feed.Recipe;
import com.kokaihop.feed.RecipeDataManager;
import com.kokaihop.feed.RecipeHandler;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.CloudinaryUtils;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 17/5/17.
 */

public class RecipeHistoryAdapter extends RecyclerView.Adapter<RecipeHistoryAdapter.ViewHolder> {

    private ArrayList<Recipe> recipeList;
    private RowHistoryRecipeBinding binding;
    private Context context;
    private CookbookDetailViewModel viewModel;
    private RecipeHandler recipeHandler;
    private Fragment fragment;
    private RecipeDataManager recipeDataManager;
    private ItemEditor editCookbook;
    private TextView tvPreviousDelete;
    private String recipeId;
    private int previousDelete = -1, animationDuration = 300;

    public RecipeHistoryAdapter(Fragment fragment, ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
        this.fragment = fragment;
        recipeHandler = new RecipeHandler();
        editCookbook = new ItemEditor();
        recipeDataManager = new RecipeDataManager();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        context = parent.getContext();
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_history_recipe, parent, false);
        int size = context.getResources().getDimensionPixelSize(R.dimen.history_recipe_image_size);
        ImageView ivCover = binding.ivRecipeImage;
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.ivRecipeImage.getLayoutParams();
        layoutParams.height = size;
        layoutParams.width = size;
        ivCover.setLayoutParams(layoutParams);
        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Recipe recipe = recipeList.get(position);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.ivRecipeImage.getLayoutParams();
        String imageWidth = String.valueOf(layoutParams.width);
        String imageHeight = String.valueOf(layoutParams.height);
        if (recipe.getMainImageUrl() == null || recipe.getMainImageUrl().isEmpty()) {
            if (recipe.getMainImagePublicId() != null) {
                recipe.setMainImageUrl(CloudinaryUtils.getRoundedCornerImageUrl(recipe.getMainImagePublicId(), String.valueOf(layoutParams.width), String.valueOf(layoutParams.height)));
            } else {
                Glide.clear(binding.ivRecipeImage);
            }
        }
        holder.bind(recipe, imageWidth, imageHeight);
        binding.executePendingBindings();


    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    //    uppdate the history changes on the user profile screen.
    public void displayHistoryChanges() {
        recipeList.clear();
        ArrayList<RecipeHistoryRealmObject> historyList = new HistoryDataManager().getHistory();
        for (RecipeHistoryRealmObject historyRealmObject : historyList) {
            RecipeRealmObject recipeRealmObject = recipeDataManager.fetchRecipe(historyRealmObject.getId());
            recipeList.add(recipeDataManager.getRecipe(recipeRealmObject));
        }
        ((HistoryFragment) fragment).updateHistory();
    }

    public void setEditCookbook(boolean isEditCookbook) {
        editCookbook.setEditMode(isEditCookbook);
    }

    public void removeDeleteButton() {
        for (Recipe recipe : recipeList) {
            recipe.setRecipeDelete(false);
            recipe.setRecipeEdit(false);
        }
    }

    public void enterRecipeEditMode() {
        for (Recipe recipe : recipeList) {
            recipe.setRecipeEdit(true);
        }
    }

    public void editListUpdated() {
        if (previousDelete >= 0)
            recipeList.get(previousDelete).setRecipeDelete(true);
    }

    public CookbookDetailViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(CookbookDetailViewModel viewModel) {
        this.viewModel = viewModel;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        RowHistoryRecipeBinding binding;
        int size;

        public ViewHolder(RowHistoryRecipeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            size = binding.tvDelete.getMaxWidth();
        }


        public void bind(final Recipe recipe, final String imageWidth, final String imageHeight) {
            binding.setRecipe(recipe);
            binding.setEditCookbook(editCookbook);
            binding.executePendingBindings();
            binding.recipeRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!editCookbook.isEditMode()) {
                        recipeHandler.openRecipeDetail(v, recipe.get_id(), getAdapterPosition(), imageWidth, imageHeight);
                        if (fragment instanceof HistoryFragment) {
                            User.getInstance().setIndex(3);
                            displayHistoryChanges();
                        } else {
                            if (fragment instanceof RecipeFragment)
                                ((RecipeFragment) fragment).getUser().setIndex(0);
                        }
                    } else if (previousDelete >= 0) {
                        tvPreviousDelete.animate().translationX(size).setDuration(animationDuration);
                        completeAnimation(previousDelete);

                    }
                }
            });
            binding.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.tvDelete.animate().translationX(size).setDuration(0);
                    binding.tvDelete.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (previousDelete >= 0 && previousDelete != getAdapterPosition()) {
                                tvPreviousDelete.animate().translationX(size).setDuration(animationDuration);
                                completeAnimation(previousDelete);
                            }
                            tvPreviousDelete = binding.tvDelete;
                            previousDelete = getAdapterPosition();
                            recipe.setRecipeEdit(false);
                            recipe.setRecipeDelete(true);
                            binding.tvDelete.animate().translationX(0).setDuration(animationDuration);
                        }
                    }, animationDuration);
                }
            });
            binding.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recipeId = recipe.get_id();
                    viewModel.removeRecipeFromCookbook(recipe.get_id(), getAdapterPosition());
                }
            });
        }

        private void completeAnimation(final int previousDelete) {
            tvPreviousDelete.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recipeList.get(previousDelete).setRecipeDelete(false);
                    recipeList.get(previousDelete).setRecipeEdit(true);
                }
            }, animationDuration);
        }
    }

    public void removeRecipe(int position) {
        if (recipeList.size() > position && position >= 0) {
            if (recipeList.get(position).get_id().equals(recipeId)) {
                recipeList.remove(position);
                notifyItemRemoved(position);
            }
        }
        previousDelete = -1;
    }
}
