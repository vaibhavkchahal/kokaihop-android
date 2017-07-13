package com.kokaihop.recipedetail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FeedRecyclerAdvtItemBinding;
import com.altaworks.kokaihop.ui.databinding.RecipeDetailAddCommentsHeadingBinding;
import com.altaworks.kokaihop.ui.databinding.RecipeDetailCommentsHeadingBinding;
import com.altaworks.kokaihop.ui.databinding.RecipeDetailDirectionHeadingBinding;
import com.altaworks.kokaihop.ui.databinding.RecipeDetailIngredientHeadingBinding;
import com.altaworks.kokaihop.ui.databinding.RecipeDetailItemDirectionBinding;
import com.altaworks.kokaihop.ui.databinding.RecipeDetailItemIngredentVariatorBinding;
import com.altaworks.kokaihop.ui.databinding.RecipeDetailItemIngredientBinding;
import com.altaworks.kokaihop.ui.databinding.RecipeDetailItemIngredientSubheaderBinding;
import com.altaworks.kokaihop.ui.databinding.RecipeDetailItemMainHeaderBinding;
import com.altaworks.kokaihop.ui.databinding.RecipeDetailSimilarRecipeHeadingBinding;
import com.altaworks.kokaihop.ui.databinding.RecipeDetailSimilarRecipeItemBinding;
import com.altaworks.kokaihop.ui.databinding.RecipeItemCommentBinding;
import com.altaworks.kokaihop.ui.databinding.RecipeSpecificationItemBinding;
import com.kokaihop.comments.CommentsHandler;
import com.kokaihop.database.CommentRealmObject;
import com.kokaihop.database.IngredientsRealmObject;
import com.kokaihop.feed.AdvtDetail;
import com.kokaihop.feed.RecipeHandler;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.CloudinaryUtils;

import java.text.DecimalFormat;
import java.util.List;

import static com.kokaihop.utility.AppUtility.getHeightInAspectRatio;

public class RecipeDetailRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_ITEM_RECIPE_MAIN_HEADER = 0;
    public static final int TYPE_ITEM_ADVT = 1;
    public static final int TYPE_ITEM_RECIPE_INGREDIENT = 2;
    public static final int TYPE_ITEM_RECIPE_INGREDIENT_VARIATOR = 3;
    public static final int TYPE_ITEM_DIRECTION = 4;
    public static final int TYPE_ITEM_RECIPE_SPECIFICATIONS = 5;
    public static final int TYPE_ITEM_COMMENT = 6;
    public static final int TYPE_ITEM_SIMILAR_RECIPIES_ITEM = 7;
    public static final int TYPE_ITEM_DIRECTION_HEADING = 8;
    public static final int TYPE_ITEM_COMMENTS_HEADING = 9;
    public static final int TYPE_ITEM_INGREDIENT_HEADING = 10;
    public static final int TYPE_ITEM_ADD_COMMENTS_HEADING = 11;
    public static final int TYPE_ITEM_SIMILAR_RECIPIES_HEADING = 12;
    public static final int TYPE_ITEM_INGREDIENT_SUB_HEADING = 13;

    private final List<Object> recipeDetailItemsList;

    private Context context;
    private PortionClickListener onPortionClickListener;
    private String comingFrom;

    public RecipeDetailRecyclerAdapter(String comingFrom, List<Object> list) {
        recipeDetailItemsList = list;
        this.comingFrom = comingFrom;
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
        } else if (viewType == TYPE_ITEM_INGREDIENT_HEADING) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail_ingredient_heading, parent, false);
            return new ViewHolderIngredientHeading(v);
        } else if (viewType == TYPE_ITEM_DIRECTION_HEADING) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail_direction_heading, parent, false);
            return new ViewHolderDirectionHeading(v);
        } else if (viewType == TYPE_ITEM_COMMENTS_HEADING) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail_comments_heading, parent, false);
            return new ViewHolderCommentsHeading(v);
        } else if (viewType == TYPE_ITEM_ADD_COMMENTS_HEADING) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail_add_comments_heading, parent, false);
            return new ViewHolderAddCommentHeading(v);
        } else if (viewType == TYPE_ITEM_SIMILAR_RECIPIES_HEADING) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail_similar_recipe_heading, parent, false);
            return new ViewHolderSimilarRecipiesHeading(v);
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
        } else if (viewType == TYPE_ITEM_COMMENT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item_comment, parent, false);
            return new ViewHolderItemComment(v);
        } else if (viewType == TYPE_ITEM_SIMILAR_RECIPIES_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail_similar_recipe_item, parent, false);
            return new ViewHolderItemSimilarRecipe(v);
        } else if (viewType == TYPE_ITEM_INGREDIENT_SUB_HEADING) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail_item_ingredient_subheader, parent, false);
            return new ViewHolderIngredientSubHeading(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_ITEM_RECIPE_MAIN_HEADER:
                ViewHolderMainHeader holderMainHeader = (ViewHolderMainHeader) holder;
                RecipeDetailHeader recipeDetailHeader = (RecipeDetailHeader) recipeDetailItemsList.get(position);
                RatingBar ratingBar = holderMainHeader.binder.ratingBar;
                holderMainHeader.binder.setModel(recipeDetailHeader);
                holderMainHeader.binder.setRatingHandler(new RecipeRatingHandler(ratingBar, recipeDetailHeader));
                holderMainHeader.binder.executePendingBindings();
                break;
            case TYPE_ITEM_ADVT:
                ViewHolderAdvt holderAdvt = (ViewHolderAdvt) holder;
                holderAdvt.binder.executePendingBindings();
                break;
            case TYPE_ITEM_INGREDIENT_HEADING:
                ViewHolderIngredientHeading holderIngredientHeading = (ViewHolderIngredientHeading) holder;
                ListHeading ingredientHeading = (ListHeading) recipeDetailItemsList.get(position);
                holderIngredientHeading.binder.setModel(ingredientHeading);
                holderIngredientHeading.binder.executePendingBindings();
                break;
            case TYPE_ITEM_INGREDIENT_SUB_HEADING:
                ViewHolderIngredientSubHeading holderIngredientSubHeading = (ViewHolderIngredientSubHeading) holder;
                IngredientSubHeader ingredientSubHeader = (IngredientSubHeader) recipeDetailItemsList.get(position);
                holderIngredientSubHeading.binder.setModel(ingredientSubHeader);
                holderIngredientSubHeading.binder.executePendingBindings();
                break;
            case TYPE_ITEM_DIRECTION_HEADING:
                ViewHolderDirectionHeading holderDirectionHeading = (ViewHolderDirectionHeading) holder;
                ListHeading directionHeading = (ListHeading) recipeDetailItemsList.get(position);
                holderDirectionHeading.binder.setModel(directionHeading);
                holderDirectionHeading.binder.executePendingBindings();
                break;
            case TYPE_ITEM_COMMENTS_HEADING:
                ViewHolderCommentsHeading holderCommentsHeading = (ViewHolderCommentsHeading) holder;
                ListHeading commentHeading = (ListHeading) recipeDetailItemsList.get(position);
                holderCommentsHeading.binder.setModel(commentHeading);
                holderCommentsHeading.binder.setHandler(new CommentsHandler());
                holderCommentsHeading.binder.executePendingBindings();
                break;
            case TYPE_ITEM_ADD_COMMENTS_HEADING:
                ViewHolderAddCommentHeading holderAddCommentHeading = (ViewHolderAddCommentHeading) holder;
                ListHeading addCommentHeading = (ListHeading) recipeDetailItemsList.get(position);
                holderAddCommentHeading.binder.setModel(addCommentHeading);
                holderAddCommentHeading.binder.setHandler(new CommentsHandler());
                holderAddCommentHeading.binder.executePendingBindings();
                break;
            case TYPE_ITEM_SIMILAR_RECIPIES_HEADING:
                ViewHolderSimilarRecipiesHeading holderSimilarRecipiesHeading = (ViewHolderSimilarRecipiesHeading) holder;
                ListHeading similarRecipeHeading = (ListHeading) recipeDetailItemsList.get(position);
                holderSimilarRecipiesHeading.binder.setModel(similarRecipeHeading);
                holderSimilarRecipiesHeading.binder.executePendingBindings();
                break;
            case TYPE_ITEM_RECIPE_INGREDIENT:
                ViewHolderItemIngredient holderIngredient = (ViewHolderItemIngredient) holder;
                IngredientsRealmObject ingredientsRealmObject = (IngredientsRealmObject) recipeDetailItemsList.get(position);
                DecimalFormat decimalFormat = new DecimalFormat("###.#");
                holderIngredient.binder.setDecimalFormat(decimalFormat);
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
                RecipeCookingDirection recipeCookingDirection = (RecipeCookingDirection) recipeDetailItemsList.get(position);
                holderDirection.binder.setModel(recipeCookingDirection.getDirection());
                holderDirection.binder.executePendingBindings();
                break;
            case TYPE_ITEM_RECIPE_SPECIFICATIONS:
                ViewHolderItemRecipeCreator holderRecipeCreator = (ViewHolderItemRecipeCreator) holder;
                RecipeSpecifications specifications = (RecipeSpecifications) recipeDetailItemsList.get(position);
                int creatorImageSize = context.getResources().getDimensionPixelOffset(R.dimen.iv_recipe_creator_height_width);
                String recipeCreatorImage = CloudinaryUtils.getRoundedImageUrl(specifications.getImageId(), String.valueOf(creatorImageSize), String.valueOf(creatorImageSize));
                holderRecipeCreator.binder.setImageUrl(recipeCreatorImage);
                holderRecipeCreator.binder.setHandler(new RecipeSpecificationHandler());
                holderRecipeCreator.binder.setModel(specifications);
                holderRecipeCreator.binder.executePendingBindings();
                break;
            case TYPE_ITEM_COMMENT:
                ViewHolderItemComment holderItemComment = (ViewHolderItemComment) holder;
                final CommentRealmObject commentRealmObject = (CommentRealmObject) recipeDetailItemsList.get(position);
                setCommentUserImage(holderItemComment, commentRealmObject);
                setCommentReplyInformation(holderItemComment, commentRealmObject);
                checkReplyEventsVisibility(holderItemComment, commentRealmObject);
                final CommentsHandler commentsHandler = new CommentsHandler();
                actionOnCommentClick(holderItemComment, commentRealmObject, commentsHandler);
                actionOnReplyClick(holderItemComment, commentRealmObject, commentsHandler);
                holderItemComment.binder.setModel(commentRealmObject);
                holderItemComment.binder.setHandler(commentsHandler);
                holderItemComment.binder.executePendingBindings();
                break;
            case TYPE_ITEM_SIMILAR_RECIPIES_ITEM:
                ViewHolderItemSimilarRecipe holderItemSimilarRecipe = (ViewHolderItemSimilarRecipe) holder;
                SimilarRecipe similarRecipe = (SimilarRecipe) recipeDetailItemsList.get(position);
                Point point = AppUtility.getDisplayPoint(context);
                int width = point.x;
                float ratio = (float) 100 / 320;
                int height = getHeightInAspectRatio(width, ratio);
                ImageView imageViewRecipe = holderItemSimilarRecipe.binder.imgviewRecipeImg;
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageViewRecipe.getLayoutParams();
                layoutParams.height = height;
                layoutParams.width = width;
                imageViewRecipe.setLayoutParams(layoutParams);
                String recipeUrl = CloudinaryUtils.getImageUrl(similarRecipe.getRecipeImageUrl(), String.valueOf(imageViewRecipe.getLayoutParams().width), String.valueOf(imageViewRecipe.getLayoutParams().height));
                int profileImageSize = context.getResources().getDimensionPixelOffset(R.dimen.similar_recipe_profile_img_height_width);
                String profileImageUrl = CloudinaryUtils.getRoundedImageUrl(similarRecipe.getUserImageUrl(), String.valueOf(profileImageSize), String.valueOf(profileImageSize));
                holderItemSimilarRecipe.binder.setRecipeImageUrl(recipeUrl);
                holderItemSimilarRecipe.binder.setProfileImageUrl(profileImageUrl);
                holderItemSimilarRecipe.binder.setModel(similarRecipe);
                holderItemSimilarRecipe.binder.setRecipeHandler(new RecipeHandler());
                holderItemSimilarRecipe.binder.executePendingBindings();
                break;
            default:
                break;

        }
    }

    private void actionOnReplyClick(ViewHolderItemComment holderItemComment, final CommentRealmObject commentRealmObject, final CommentsHandler commentsHandler) {
        holderItemComment.binder.replyTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentsHandler.openReplyScreen(context, commentRealmObject.get_id(), commentRealmObject.getPayload().getRecipe().get_id());
            }
        });
    }

    private void actionOnCommentClick(ViewHolderItemComment holderItemComment, final CommentRealmObject commentRealmObject, final CommentsHandler commentsHandler) {
        holderItemComment.binder.relativeLayoutComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentsHandler.openCommentsScreen(context, commentRealmObject.getPayload().getRecipe().get_id());
            }
        });
    }

    private void checkReplyEventsVisibility(ViewHolderItemComment holderItemComment, CommentRealmObject commentRealmObject) {
        if (comingFrom.contains("commentsSection") && !commentRealmObject.getPayload().getReplyEvents().isEmpty()) {
            holderItemComment.binder.relativeLayoutRepliedSection.setVisibility(View.VISIBLE);
        } else {
            holderItemComment.binder.relativeLayoutRepliedSection.setVisibility(View.GONE);
        }
    }

    private void setCommentUserImage(ViewHolderItemComment holderItemComment, CommentRealmObject commentRealmObject) {
        int commentUsetImageSize = context.getResources().getDimensionPixelOffset(R.dimen.imgview_comment_user_image_width);
        if (commentRealmObject.getSourceUser().getProfileImage() != null) {
            String commentUserImage = CloudinaryUtils.getRoundedImageUrl(commentRealmObject.getSourceUser().getProfileImage().getCloudinaryId(), String.valueOf(commentUsetImageSize), String.valueOf(commentUsetImageSize));
            holderItemComment.binder.setImageUrl(commentUserImage);
        }
    }

    private void setCommentReplyInformation(ViewHolderItemComment holderItemComment, CommentRealmObject commentRealmObject) {
        if (commentRealmObject.getPayload().getReplyEvents().size() > 0) {
            CommentRealmObject replyCommentRealmObject = commentRealmObject.getPayload().getReplyEvents().get(0);
            int replyCommentUsetImageSize = context.getResources().getDimensionPixelOffset(R.dimen.reply_user_image_height_width);
            if (commentRealmObject.getSourceUser().getProfileImage() != null) {
                String replyCommentUserImage = CloudinaryUtils.getRoundedImageUrl(replyCommentRealmObject.getSourceUser().getProfileImage().getCloudinaryId(), String.valueOf(replyCommentUsetImageSize), String.valueOf(replyCommentUsetImageSize));
                holderItemComment.binder.setReplyUserImageUrl(replyCommentUserImage);
            }
            String replyUserName = replyCommentRealmObject.getSourceUser().getUserName();
            holderItemComment.binder.setLatestCommentUsername(replyUserName);
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
            ListHeading heading = (ListHeading) object;
            if (heading.getTitle().equals(context.getString(R.string.text_Ingredients)))
                return TYPE_ITEM_INGREDIENT_HEADING;
            else if (heading.getTitle().equals(context.getString(R.string.text_directions)))
                return TYPE_ITEM_DIRECTION_HEADING;
            else if (heading.getTitle().equals(context.getString(R.string.text_comments)))
                return TYPE_ITEM_COMMENTS_HEADING;
            else if (heading.getTitle().equals(context.getString(R.string.add_comments)))
                return TYPE_ITEM_ADD_COMMENTS_HEADING;
            else if (heading.getTitle().equals(context.getString(R.string.text_SimilarRecipies)))
                return TYPE_ITEM_SIMILAR_RECIPIES_HEADING;
            else
                return -1;

        } else if (object instanceof IngredientsRealmObject) {
            return TYPE_ITEM_RECIPE_INGREDIENT;
        } else if (object instanceof RecipeQuantityVariator) {
            return TYPE_ITEM_RECIPE_INGREDIENT_VARIATOR;
        } else if (object instanceof RecipeCookingDirection) {
            return TYPE_ITEM_DIRECTION;
        } else if (object instanceof CommentRealmObject) {
            return TYPE_ITEM_COMMENT;
        } else if (object instanceof RecipeSpecifications) {
            return TYPE_ITEM_RECIPE_SPECIFICATIONS;
        } else if (object instanceof SimilarRecipe) {
            return TYPE_ITEM_SIMILAR_RECIPIES_ITEM;
        } else if (object instanceof IngredientSubHeader) {
            return TYPE_ITEM_INGREDIENT_SUB_HEADING;
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

    private class ViewHolderIngredientHeading extends RecyclerView.ViewHolder {
        public RecipeDetailIngredientHeadingBinding binder;

        ViewHolderIngredientHeading(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);
        }
    }

    private class ViewHolderIngredientSubHeading extends RecyclerView.ViewHolder {
        public RecipeDetailItemIngredientSubheaderBinding binder;

        ViewHolderIngredientSubHeading(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);
        }
    }

    private class ViewHolderDirectionHeading extends RecyclerView.ViewHolder {
        public RecipeDetailDirectionHeadingBinding binder;

        ViewHolderDirectionHeading(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);
        }
    }

    private class ViewHolderCommentsHeading extends RecyclerView.ViewHolder {
        public RecipeDetailCommentsHeadingBinding binder;

        ViewHolderCommentsHeading(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);
        }
    }

    private class ViewHolderAddCommentHeading extends RecyclerView.ViewHolder {
        public RecipeDetailAddCommentsHeadingBinding binder;

        ViewHolderAddCommentHeading(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);
        }
    }

    private class ViewHolderSimilarRecipiesHeading extends RecyclerView.ViewHolder {
        public RecipeDetailSimilarRecipeHeadingBinding binder;

        ViewHolderSimilarRecipiesHeading(View view) {
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


    private class ViewHolderItemSimilarRecipe extends RecyclerView.ViewHolder {
        public RecipeDetailSimilarRecipeItemBinding binder;

        ViewHolderItemSimilarRecipe(View view) {
            super(view);
            binder = DataBindingUtil.bind(view);
        }
    }

    private class ViewHolderItemComment extends RecyclerView.ViewHolder {
        public RecipeItemCommentBinding binder;

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
