package com.rsxsoftware.insurance.view.bind;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import com.parse.ParseObject;
import com.rsxsoftware.insurance.R;
import com.rsxsoftware.insurance.view.FragmentBase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by steve.fiedelberg on 12/26/13.
 */
public class CapturePhoto {

    public static final int OFFSET = 100;
    private final int requestCode;
    private final OnCapturePhotoListener onCapturePhotoListener;
    private String photoFilePath;

    public CapturePhoto(int requestCode, OnCapturePhotoListener onCapturePhotoListener) {

        this.requestCode = requestCode;
        this.onCapturePhotoListener = onCapturePhotoListener;
    }

    public void showPopup(final View view, final ParseObject object, final String key) {

        final PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.getMenu().add(Menu.NONE, Menu.NONE, 1, "Take Photo").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                deletePhotoCancelDownload(object, key);
                takePhoto(view);
                return true;
            }
        });
        popupMenu.getMenu().add(Menu.NONE, Menu.NONE, 2, "Attach Photo").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                deletePhotoCancelDownload(object, key);
                selectFile(view);
                return true;
            }
        });
        popupMenu.show();

    }

    private void startActivityForResult(Intent intent, int requestCode, Fragment fragment) {
        fragment.startActivityForResult(intent, requestCode);
    }

    private FragmentBase getFragment(View layout) {
        return (FragmentBase) ((Activity) layout.getContext()).getFragmentManager().findFragmentById(R.id.fragment_container);
    }

    private void selectFile(View photoLayout) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode + OFFSET, getFragment(photoLayout));
    }


    private void takePhoto(View layout) {

        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            final File imageFile = createImageFile();
            final FragmentBase fragment = getFragment(layout);
            setPhotoFilePath(imageFile.getAbsolutePath());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(intent, requestCode, fragment);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    private void deletePhotoCancelDownload(ParseObject object, String key) {
        object.remove(key);
    }


    public int getRequestCode() {
        return requestCode;
    }

    public void setPhotoFilePath(String photoFilePath) {
        this.photoFilePath = photoFilePath;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            final int thisRequestCode = getRequestCode();
            if (requestCode != thisRequestCode) {
                if (requestCode == (OFFSET + thisRequestCode)) {
                    showPhotoFromFile(data);
                } else {
                    photoFilePath = null;
                }
            }
        } else {
            photoFilePath = null;
        }

        if (photoFilePath != null) {
            onCapturePhotoListener.onHavePhoto(photoFilePath);
        }
    }


    private void showPhotoFromFile(Intent data) {
        Uri uri = data.getData();
        String scheme = uri.getScheme();
        if ("file".equals(scheme)) {
            photoFilePath = uri.getPath();
        } else if ("content".equals(scheme)) {
            // process as a uri that points to a content item
        }
    }
}
