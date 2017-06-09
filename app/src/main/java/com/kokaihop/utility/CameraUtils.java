package com.kokaihop.utility;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.editprofile.EditProfileViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.kokaihop.editprofile.EditProfileViewModel.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;

/**
 * Created by Rajendra Singh on 9/6/17.
 */

public class CameraUtils {

    public static String userChoosenTask;

    public static void selectImage(final Context context) {
        final CharSequence[] items = {context.getString(R.string.take_photo),
                context.getString(R.string.choose_from_library),
                context.getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
        builder.setTitle(R.string.add_photo);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(context.getString(R.string.take_photo))) {
                    userChoosenTask = context.getString(R.string.take_photo);
                    if (checkPermission(context, Manifest.permission.CAMERA)) {
                        cameraIntent(context);
                    }
                } else if (items[item].equals(context.getString(R.string.choose_from_library))) {
                    userChoosenTask = context.getString(R.string.choose_from_library);
                    if (checkPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        galleryIntent(context);
                    }
                } else if (items[item].equals(context.getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public static void cameraIntent(Context context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ((Activity) context).startActivityForResult(intent, EditProfileViewModel.REQUEST_CAMERA);
    }

    public static void galleryIntent(Context context) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Select File"), EditProfileViewModel.REQUEST_GALLERY);
    }

    public static void onSelectFromGalleryResult(Intent data, ImageView view) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        view.setImageBitmap(bm);
    }

    public static void onCaptureImageResult(Intent data, ImageView view) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        view.setImageBitmap(thumbnail);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context, final String permission) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
