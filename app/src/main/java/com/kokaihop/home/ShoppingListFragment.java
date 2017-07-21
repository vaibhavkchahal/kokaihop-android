package com.kokaihop.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentShoppingListBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.kokaihop.database.IngredientsRealmObject;
import com.kokaihop.database.ShoppingListRealmObject;
import com.kokaihop.utility.AppCredentials;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.HorizontalDividerItemDecoration;
import com.kokaihop.utility.ShareContentShoppingIngredient;
import com.kokaihop.utility.SharedPrefUtils;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ShoppingListFragment extends Fragment implements ShoppingListViewModel.IngredientsDatasetListener, ShoppingListRecyclerAdapter.RecyclerOnItemClickListener {

    private ShoppingListViewModel viewModel;
    private FragmentShoppingListBinding binding;
    private ShoppingListRecyclerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shopping_list, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shopping_list, container, false);
        viewModel = new ShoppingListViewModel(getContext(), this);
        binding.setViewModel(viewModel);
        initializerecyclerView();
        initializePullToRefresh();
        loadAdmobAd();
        editIngredient();
        return binding.getRoot();
    }

    private void initializePullToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String accessToken = SharedPrefUtils.getSharedPrefStringData(getContext(), Constants.ACCESS_TOKEN);
                if (!accessToken.isEmpty()) {
                    viewModel.fetchIngredientFromDB();
                    viewModel.deleteIngredientOnServer();
                    viewModel.updateIngredientOnServer();
                } else {
                    binding.swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }


    private void loadAdmobAd() {
        final AdView adViewBanner = new AdView(getActivity());
        adViewBanner.setAdSize(AdSize.LARGE_BANNER); //320x100 LARGE_BANNER
        adViewBanner.setAdUnitId(AppCredentials.SHOPPING_INGRIDIENT_ADS_UNIT_IDS);
        if (binding.linearLytAd.getChildCount() > 0) {
            binding.linearLytAd.removeAllViews();
        }
        if (adViewBanner.getParent() != null) {
            ((ViewGroup) adViewBanner.getParent()).removeView(adViewBanner);
        }
        binding.linearLytAd.addView(adViewBanner);
        adViewBanner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adViewBanner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                adViewBanner.setVisibility(View.GONE);
            }
        });
        // Load the ad.
        adViewBanner.loadAd(new AdRequest.Builder().build());
    }

    private void initializerecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = binding.rvRecipeIngredients;
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration(getContext()));
        adapter = new ShoppingListRecyclerAdapter(viewModel.getIngredientsList(), this);
        adapter.setViewModel(viewModel);
        recyclerView.setAdapter(adapter);
        binding.txtviewAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), AddIngredientActivity.class), Constants.ADD_INGREDIENT_REQUEST_CODE);
            }
        });
    }

    private void editIngredient() {
        binding.txtviewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.txtviewEdit.getText().toString().equals(getString(R.string.edit))) {
                    binding.txtviewEdit.setText(R.string.delete_all);
                    binding.txtviewTitle.setText("Edit List");
                    binding.txtviewDone.setText("Done");
                    binding.txtviewDone.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    binding.relativeLayoutAddIngredient.setVisibility(View.GONE);
                    adapter.setIndgredientEditor(true);

                } else {
                    showDeleteAllIngrdientDialog();
                }
            }
        });
        binding.txtviewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.txtviewDone.getText().length() > 0) {
                    binding.txtviewDone.setText("");
                    binding.txtviewDone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_share_md, 0, 0, 0);
                    binding.txtviewEdit.setText(R.string.edit);
                    binding.relativeLayoutAddIngredient.setVisibility(View.VISIBLE);
                    binding.txtviewTitle.setText("Shopping List");
                    for (int i = 0; i < binding.rvRecipeIngredients.getChildCount(); i++) {
                        View view = binding.rvRecipeIngredients.getChildAt(i);
                        view.findViewById(R.id.tv_delete).setVisibility(View.GONE);
//                        viewModel.getShoppingDataManager().updateIngredientDeleteFlag(object, false);
                    }
                    adapter.setIndgredientEditor(false);
                } else {
                    // share
                    ShareContentShoppingIngredient shareContents = new ShareContentShoppingIngredient(getContext());
                    shareContents.setShoppingIngredient(viewModel.getIngredientsList());
                    shareContents.share();
                }
            }
        });
    }

    @Override
    public void onUpdateIngredientsList() {
        RecyclerView recyclerView = binding.rvRecipeIngredients;
        visibiltyCheckOnClearMarked();
        if (recyclerView.getAdapter() != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount());
            binding.swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void visibiltyCheckOnClearMarked() {
        if (viewModel != null && viewModel.getIngredientsList().size() > 0) {
            binding.txtviewClearMarked.setVisibility(View.VISIBLE);
            binding.txtviewEdit.setEnabled(true);
            binding.txtviewDone.setEnabled(true);
        } else {
            binding.txtviewDone.setEnabled(false);
            binding.txtviewEdit.setEnabled(false);
            binding.txtviewClearMarked.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ADD_INGREDIENT_REQUEST_CODE && resultCode == RESULT_OK) {
            viewModel.fetchIngredientFromDB();
            viewModel.updateIngredientOnServer();
        }
    }

    @Override
    public void onItemClick(int position, View view) {
        ImageView imageView = (ImageView) view;
        if (imageView.getTag().equals(Constants.EDIT_INGGREDIENT_TAG)) {
            IngredientsRealmObject object = viewModel.getIngredientsList().get(position);
            Intent intent = new Intent(getContext(), AddIngredientActivity.class);
            intent.putExtra(Constants.INGREDIENT_NAME, object.getName());
            intent.putExtra(Constants.INGREDIENT_AMOUNT, object.getAmount());
            if (object.getUnit() != null) {
                intent.putExtra(Constants.INGREDIENT_UNIT, object.getUnit().getName());
            }
            intent.putExtra(Constants.INGREDIENT_ID, object.get_id());
            startActivityForResult(intent, Constants.ADD_INGREDIENT_REQUEST_CODE);
        }
    }

    private void showDeleteAllIngrdientDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setMessage(R.string.delete_all_ingredient_msg);
        dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ShoppingListRealmObject shoppingListRealmObject = viewModel.getShoppingDataManager().fetchShoppingRealmObject();
                List<String> ids = new ArrayList<String>();
                for (IngredientsRealmObject object : shoppingListRealmObject.getIngredients()) {
                    ids.add(object.get_id());
                }
                viewModel.getShoppingDataManager().deleteIngredientObjectFromDB(ids);
                viewModel.fetchIngredientFromDB();
                viewModel.deleteIngredientOnServer();
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}
