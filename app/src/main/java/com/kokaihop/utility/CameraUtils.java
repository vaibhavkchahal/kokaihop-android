package com.kokaihop.utility;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kokaihop.editprofile.EditProfileViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.kokaihop.editprofile.EditProfileViewModel.MY_PERMISSIONS;

/**
 * Created by Rajendra Singh on 9/6/17.
 */

public class CameraUtils {

    public static String userChoosenTask;
    private static File photo = new File(Environment.getExternalStorageDirectory() + "/" + Constants.APP_NAME + "/" + "temp.jpg");

    //Choose whether to click the picture or select from the gallery
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
                    cameraIntent(context);
                } else if (items[item].equals(context.getString(R.string.choose_from_library))) {
                    userChoosenTask = context.getString(R.string.choose_from_library);
                    galleryIntent(context);
                } else if (items[item].equals(context.getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //    starting camera for profile picture
    public static void cameraIntent(Context context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (checkPermission(context, Manifest.permission.CAMERA)) {
            if (checkPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                try {
                    if (!photo.getParentFile().exists()) {
                        photo.mkdirs();
                    }
                    photo.delete();
                } catch (Exception e) {
                    Toast.makeText(context, R.string.sd_card_error, Toast.LENGTH_SHORT).show();
                }

                Uri imageUri = FileProvider.getUriForFile(context, getApplicationContext().getPackageName() + ".fileprovider", photo);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                ((Activity) context).startActivityForResult(intent, EditProfileViewModel.REQUEST_CAMERA);
            }
        }

    }

    //    open gallery for profile picture
    public static void galleryIntent(Context context) {
        if (checkPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            ((Activity) context).startActivityForResult(Intent.createChooser(intent, context.getString(R.string.select_file)), EditProfileViewModel.REQUEST_GALLERY);
        }
    }


    //process image if the image is choosen from the gallery
    public static Uri onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        Drawable defaultDrawable = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            defaultDrawable = context.getResources().getDrawable(R.drawable.ic_avtar_lg,null);
//        }else{
//            defaultDrawable = context.getResources().getDrawable(R.drawable.ic_avtar_lg);
//        }
//        view.setImageBitmap(bm);
//        BindingUtils.loadImage(view,data.getDataString(),defaultDrawable,defaultDrawable);

        Logger.e("Gallery", data.getDataString());
        return Uri.parse(data.getDataString());
    }

    //process image if the image is clicked by camera
    public static Uri onCaptureImageResult(Context context) {
        Bitmap thumbnail = null;
        Uri imageUri = null;
        imageUri = FileProvider.getUriForFile(context, getApplicationContext().getPackageName() + context.getString(R.string.fileprovider), photo);
        Logger.e("Camera", imageUri.toString());

//        Drawable defaultDrawable = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            defaultDrawable = context.getResources().getDrawable(R.drawable.ic_avtar_lg,null);
//        }else{
//            defaultDrawable = context.getResources().getDrawable(R.drawable.ic_avtar_lg);
//        }
//        BindingUtils.loadImage(view,imageUri.toString(),defaultDrawable,defaultDrawable);
//        view.setImageBitmap(thumbnail);
        return imageUri;

    }


    public static String getRealPathFromURI(Context context, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;



    }

    //check for permissions of user
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context, final String permission) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, MY_PERMISSIONS);
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, MY_PERMISSIONS);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    //    to share the picture with external applications
    public static void sharePicture(final Context context, String imageUrl) {

//        try {
        Glide.with(context).load(imageUrl).asBitmap().listener(new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                resource.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File f = new File(Environment.getExternalStorageDirectory() + "/" + Constants.APP_NAME + "/" + "temporary_file.jpg");
                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
                context.startActivity(Intent.createChooser(share, "Share Image"));
                return false;
            }
        }).into(-1, -1);
//        }

    }
}