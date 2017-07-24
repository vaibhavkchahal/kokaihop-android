package com.kokaihop.userprofile;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityConfirmImageUploadBinding;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ConfirmImageUploadActivity extends AppCompatActivity {

    ActivityConfirmImageUploadBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri imageUri = getIntent().getData();
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

    public void setUpImage(Uri imageUri){
        try {
            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            binding.imageToUpload.setImageBitmap(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
