package com.rsxsoftware.insurance.view.bind;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.rsxsoftware.insurance.R;
import com.rsxsoftware.insurance.prefs.Prefs;
import com.rsxsoftware.insurance.view.PhotoLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by steve.fiedelberg on 12/16/13.
 */
public class BinderPhoto extends BinderBase {
    @Override
    public void toView(final Bind bind, View view, final ParseObject object) {

        final PhotoLayout layout = (PhotoLayout) view;

        layout.init(bind.text());

        final ImageButton del = layout.getDel();
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePhotoCancelDownload(object, bind, object.getParseFile(bind.key()));
                setupPhoto(object, bind, layout, del);
            }
        });

        setupPhoto(object, bind, layout, del);
    }

    private void setupPhoto(ParseObject object, final Bind bind, final PhotoLayout photoLayout, final ImageButton del) {
        final ParseFile photoFile = object.getParseFile(bind.key());
        final boolean hasPhoto = photoFile != null;
        if (hasPhoto) {
            photoLayout.setDownloadingImage();
            photoFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        Bitmap mutableBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
                        photoLayout.setPhotoImage(mutableBitmap);
                        del.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            photoLayout.setTakePhotoImage();
        }

        setupToCapturePhoto(bind, photoLayout, photoFile, object);

    }

    private void setupToCapturePhoto(final Bind bind, final PhotoLayout photoLayout, final ParseFile photoFile, final ParseObject object) {
        photoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final PopupMenu popupMenu = new PopupMenu(photoLayout.getContext(), photoLayout);
                popupMenu.getMenu().add(Menu.NONE, Menu.NONE, 1, "Take Photo").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        deletePhotoCancelDownload(object, bind, photoFile);
                        takePhoto(photoLayout, bind);
                        return true;
                    }
                });
                popupMenu.getMenu().add(Menu.NONE, Menu.NONE, 2, "Attach Photo").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        deletePhotoCancelDownload(object, bind, photoFile);
                        selectFile(photoLayout, bind);
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    private void deletePhotoCancelDownload(ParseObject object, Bind bind, ParseFile photoFile) {
        object.remove(bind.key());
    }

    private void selectFile(PhotoLayout photoLayout, Bind bind) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(photoLayout, intent, bind.requestCode() + 100);
    }

    private void takePhoto(PhotoLayout layout, Bind bind) {

        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            final File imageFile = createImageFile();
            layout.setFilePath(imageFile.getAbsolutePath());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(layout, intent, bind.requestCode());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void startActivityForResult(PhotoLayout layout, Intent intent, int requestCode) {
        final Fragment fragment = ((Activity) layout.getContext()).getFragmentManager().findFragmentById(R.id.fragment_container);
        fragment.startActivityForResult(intent, requestCode);
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

    @Override
    public void toObject(Bind bind, View view, ParseObject object) {
        final PhotoLayout layout = (PhotoLayout) view;

        final String filePath = layout.getFilePath();
        if (filePath != null) {

            Prefs.updateFilesToSaveEventually(layout.getContext(), object, bind.key(), filePath);

        }
    }
}
