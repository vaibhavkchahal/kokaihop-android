package com.kokaihop.userprofile;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityConfirmImageUploadBinding;
import com.bumptech.glide.Glide;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.base.BaseActivity;

public class ConfirmImageUploadActivity extends BaseActivity {

    ActivityConfirmImageUploadBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri imageUri = getIntent().getData();
        GoogleAnalyticsHelper.trackScreenName(getString(R.string.image_presenter_screen));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_image_upload);
        setUpImage(imageUri);
//        binding.imageToUpload.setImageURI();
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    public void setUpImage(Uri imageUri) {
        Glide.with(this)
                .load(imageUri)
                .into(binding.imageToUpload);
//
//        try {
//            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//            final ByteArrayOutputStream compressed = new ByteArrayOutputStream();
//            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//              selectedImage.compress(Bitmap.CompressFormat.PNG, 50, compressed);
//            final Bitmap compressedSelectedImage = BitmapFactory.decodeStream(new ByteArrayInputStream(compressed.toByteArray()));
//            binding.imageToUpload.setImageBitmap(selectedImage);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }
}
