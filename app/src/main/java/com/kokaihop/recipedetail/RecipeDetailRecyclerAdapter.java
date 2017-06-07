package com.kokaihop.recipedetail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FeedRecyclerAdvtItemBinding;
import com.altaworks.kokaihop.ui.databinding.RecipeDetailItemCommentBinding;
import com.altaworks.kokaihop.ui.databinding.RecipeDetailItemDirectionBinding;
import com.altaworks.kokaihop.ui.databinding.RecipeDetailItemIngredentVariatorBinding;
import com.altaworks.kokaihop.ui.databinding.RecipeDetailItemIngredientBinding;
import com.altaworks.kokaihop.ui.databinding.RecipeDetailItemMainHeaderBinding;
import com.altaworks.kokaihop.ui.databinding.RecipeDetailListItemsHeadingBinding;
import com.altaworks.kokaihop.ui.databinding.RecipeDetailRateCommentItemBinding;
import com.altaworks.kokaihop.ui.databinding.RecipeDetailSimilarRecipeItemBinding;
import com.altaworks.kokaihop.ui.databinding.RecipeSpecificationItemBinding;
import com.kokaihop.database.CommentRealmObject;
import com.kokaihop.database.IngredientsRealmObject;
import com.kokaihop.feed.AdvtDetail;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.CloudinaryUtils;

import java.util.List;

public class RecipeDetailRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_ITEM_RECIPE_MAIN_HEADER = 0;
    public static final int TYPE_ITEM_ADVT = 1;
    public static final int TYPE_LIST_ITEM_HEADING = 2;
    public static final int TYPE_ITEM_RECIPE_INGREDIENT = 3;
    public static final int TYPE_ITEM_RECIPE_INGREDIENT_VARIATOR = 4;
    public static final int TYPE_ITEM_DIRECTION = 5;
    public static final int TYPE_ITEM_RECIPE_SPECIFICATIONS = 6;
    public static final int TYPE_ITEM_SHOW_ALL_COMMENTS = 7;
    public static final int TYPE_ITEM_RATE_AND_COMMENT = 8;
    public static final int TYPE_ITEM_SIMILAR_ITEM = 9;
    private final List<Object> recipeDetailItemsList;

    private Context context;
    private PortionClickListener onPortionClickListener;

    public RecipeDetailRecyclerAdapter(List<Object> list) {
        recipeDetailItemsList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == TYPE_ITEM_RECIPE_MAIN_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail_item_main_header, parent, false);
            return new ViewHolderMainHeader(v);
        } else if (viewType == TYPE_ITEM_ADVT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_recycler_advt_item, parent, false);
            return new ViewHolderAdvt(v);
        } else if (viewType == TYPE_LIST_ITEM_HEADING) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail_list_items_heading, parent, false);
            return new ViewHolderListItemHeading(v);
        } else if (viewType == TYPE_ITEM_RECIPE_INGREDIENT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail_item_ingredient, parent, false);
            return new ViewHolderItemIngredient(v);
        } else if (viewType == TYPE_ITEM_RECIPE_INGREDIENT_VARIATOR) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail_item_ingredent_variator, parent, false);
            return new ViewHolderIngrdientVariator(v);
        } else if (viewType == TYPE_ITEM_DIRECTION) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail_item_direction, parent, false);
            return new ViewHolderItemDirection(v);
        } else if (viewType == TYPE_ITEM_RECIPE_SPECIFICATIONS) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_specification_item, parent, false);
            return new ViewHolderItemRecipeCreator(v);
        } else if (viewType == TYPE_ITEM_SHOW_ALL_COMMENTS) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail_item_comment, parent, false);
            return new ViewHolderItemComment(v);
        }
       /* else if (viewType == TYPE_ITEM_RATE_AND_COMMENT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail_rate_comment_item, parent, false);
            return new ViewHolderItemRateComment(v);
        }*/
        else if (viewType == TYPE_ITEM_SIMILAR_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail_similar_recipe_item, parent, false);
            return new ViewHolderItemSimilarRecipe(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_ITEM_RECIPE_MAIN_HEADER:
                ViewHolderMainHeader holderMainHeader = (ViewHolderMainHeader) holder;
                RecipeDetailHeader recipeDetailHeader = (RecipeDetailHeader) recipeDetailItemsList.get(position);
                holderMainHeader.binder.setModel(recipeDetailHeader);
                holderMainHeader.binder.executePendingBindings();
                break;
            case TYPE_ITEM_ADVT:
                ViewHolderAdvt holderAdvt = (ViewHolderAdvt) holder;
//                AdvtDetail advtDetail = (AdvtDetail) recipeDetailItemsList.get(position);
//                holderAdvt.binder.setModel(advtDetail);
                holderAdvt.binder.executePendingBindings();
                break;
            case TYPE_LIST_ITEM_HEADING:
                ViewHolderListItemHeading holderHeading = (ViewHolderListItemHeading) holder;
                ListHeading listHeading = (ListHeading) recipeDetailItemsList.get(position);
                holderHeading.binder.setModel(listHeading);
                holderHeading.binder.executePendingBindings();
                break;
            case TYPE_ITEM_RECIPE_INGREDIENT:
                ViewHolderItemIngredient holderIngredient = (ViewHolderItemIngredient) holder;
                IngredientsRealmObject ingredientsRealmObject = (IngredientsRealmObject) recipeDetailItemsList.get(position);
                holderIngredient.binder.setModel(ingredientsRealmObject);
                holderIngredient.binder.executePendingBindings();
                break;
            case TYPE_ITEM_RECIPE_INGREDIENT_VARIATOR:
                ViewHolderIngrdientVariator holderVariator = (ViewHolderIngrdientVariator) holder;
                final RecipeQuantityVariator variator = (RecipeQuantityVariator) recipeDetailItemsList.get(position);
                holderVariator.binder.setModel(variator);
                holderVariator.binder.setClick(new PortionClickListener() {
                    @Override
                    public void onPortionClick(int quantity) {

                        onPortionClickListener.onPortionClick(quantity);
                    }
                });
                holderVariator.binder.executePendingBindings();
                break;
            case TYPE_ITEM_DIRECTION:
                ViewHolderItemDirection holderDirection = (ViewHolderItemDirection) holder;
                RecipeCookingDirection direction = (RecipeCookingDirection) recipeDetailItemsList.get(position);
                holderDirection.binder.setModel(direction);
                holderDirection.binder.executePendingBindings();
                break;
            case TYPE_ITEM_RECIPE_SPECIFICATIONS:
                ViewHolderItemRecipeCreator holderRecipeCreator = (ViewHolderItemRecipeCreator) holder;
                RecipeSpecifications specifications = (RecipeSpecifications) recipeDetailItemsList.get(position);
                holderRecipeCreator.binder.setModel(specifications);
                holderRecipeCreator.binder.executePendingBindings();
                break;
            case TYPE_ITEM_SHOW_ALL_COMMENTS:
                ViewHolderItemComment holderItemComment = (ViewHolderItemComment) holder;
                CommentRealmObject commentRealmObject = (CommentRealmObject) recipeDetailItemsList.get(position);
                holderItemComment.binder.setModel(commentRealmObject);
                holderItemComment.binder.executePendingBindings();
                break;
            /*case TYPE_ITEM_RATE_AND_COMMENT:
                ViewHolderItemRateComment holderItemRateComment = (ViewHolderItemRateComment) holder;
                RecipeDetailHeader recipeDetailHeader = (RecipeDetailHeader) recipeDetailItemsList.get(position);
                holderMainHeader.binder.setModel(recipeDetailHeader);

                holderItemRateComment.binder.executePendingBindings();
                break;*/
            case TYPE_ITEM_SIMILAR_ITEM:
                ViewHolderItemSimilarRecipe holderItemSimilarRecipe = (ViewHolderItemSimilarRecipe) holder;
                SimilarRecipe similarRecipe = (SimilarRecipe) recipeDetailItemsList.get(position);

                String url = AppUtility.getImageUrlWithAspectRatio(context, 280, 320, holderItemSimilarRecipe.binder.imgviewUserprofile, similarRecipe.getRecipeImageUrl());
                similarRecipe.setRecipeImageUrl(url);
                int profileImageSize = context.getResources().getDimensionPixelOffset(R.dimen.similar_recipe_profile_img_height_width);
                similarRecipe.setUserImageUrl(CloudinaryUtils.getRoundedImageUrl(similarRecipe.getUserImageUrl(), String.valueOf(profileImageSize), String.valueOf(profileImageSize)));

                holderItemSimilarRecipe.binder.setModel(similarRecipe);
                holderItemSimilarRecipe.binder.executePendingBindings();
                break;
            default:
                break;

        }
    }

    @Override
    public int getItemViewType(int position) {
        Object object = recipeDetailItemsList.get(position);
        if (object instanceof RecipeDetailHeader) {
            return TYPE_ITEM_RECIPE_MAIN_HEADER;
        } else if (object instanceof AdvtDetail) {
            return TYPE_ITEM_ADVT;
        } else if (object instanceof ListHeading) {
            return TYPE_LIST_ITEM_HEADING;
        } else if (object instanceof IngredientsRealmObject) {
            return TYPE_ITEM_RECIPE_INGREDIENT;
        } else if (object instanceof RecipeQuantityVariator) {
            return TYPE_ITEM_RECIPE_INGREDIENT_VARIATOR;
        } else if (object instanceof RecipeCookingDirection) {
            return TYPE_ITEM_DIRECTION;
        } else if (object instanceof CommentRealmObject) {
            return TYPE_ITEM_SHOW_ALL_COMMENTS;
        } else if (object instanceof RecipeSpecifications) {
            return TYPE_ITEM_RECIPE_SPECIFICATIONS;
        } else if (object instanceof SimilarRecipe) {
            return TYPE_ITEM_SIMILAR_ITEM;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return recipeDetailItemsList.size();
    }

    public void setPortionClickListener(PortionClickListener onPortionClickListener) {
        this.onPortionClickListener = onPortionClickListener;

    }


    public class ViewHolderMainHeader extends RecyclerView.ViewHolder {
        public RecipeDetailItemMainHeaderBinding binder;

        public ViewHolderMainHeader(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);

        }
    }

    public class ViewHolderAdvt extends RecyclerView.ViewHolder {
        public FeedRecyclerAdvtItemBinding binder;


        public ViewHolderAdvt(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);

        }
    }

    private class ViewHolderListItemHeading extends RecyclerView.ViewHolder {
        public RecipeDetailListItemsHeadingBinding binder;

        ViewHolderListItemHeading(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);
        }
    }

    private class ViewHolderItemIngredient extends RecyclerView.ViewHolder {
        public RecipeDetailItemIngredientBinding binder;

        ViewHolderItemIngredient(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);
        }
    }

    private class ViewHolderItemDirection extends RecyclerView.ViewHolder {
        public RecipeDetailItemDirectionBinding binder;

        ViewHolderItemDirection(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);
        }
    }

    private class ViewHolderItemRateComment extends RecyclerView.ViewHolder {
        public RecipeDetailRateCommentItemBinding binder;

        ViewHolderItemRateComment(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);
        }
    }

    private class ViewHolderItemSimilarRecipe extends RecyclerView.ViewHolder {
        public RecipeDetailSimilarRecipeItemBinding binder;

        ViewHolderItemSimilarRecipe(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);
        }
    }

    private class ViewHolderItemComment extends RecyclerView.ViewHolder {
        public RecipeDetailItemCommentBinding binder;

        ViewHolderItemComment(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);
        }
    }

    private class ViewHolderIngrdientVariator extends RecyclerView.ViewHolder {
        public RecipeDetailItemIngredentVariatorBinding binder;

        ViewHolderIngrdientVariator(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);
        }
    }

    private class ViewHolderItemRecipeCreator extends RecyclerView.ViewHolder {
        public RecipeSpecificationItemBinding binder;

        ViewHolderItemRecipeCreator(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);
        }
    }

    public interface PortionClickListener {
        void onPortionClick(int quantity);

    }
}
