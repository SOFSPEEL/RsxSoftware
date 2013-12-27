package com.rsxsoftware.insurance.view.bind;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageButton;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.rsxsoftware.insurance.ParseFilesSaveService;
import com.rsxsoftware.insurance.view.PhotoLayout;

/**
 * Created by steve.fiedelberg on 12/16/13.
 */
public class BinderPhoto extends BinderBase {

    private String photoFilePath;

    @Override
    public void toView(final Bind bind, View view, final ParseObject object) {

        final PhotoLayout layout = (PhotoLayout) view;

        layout.init(bind.text());

        final ImageButton del = layout.getDel();
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                deletePhotoCancelDownload(object, bind.key());     //TODO:
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


        CapturePhoto capturePhoto = new CapturePhoto(bind.requestCode(), new OnCapturePhotoListener() {

            @Override
            public void onHavePhoto(String photoFilePath) {

                photoLayout.setPic(photoFilePath);
                BinderPhoto.this.photoFilePath = photoFilePath;
            }
        });

        photoLayout.setCapturePhoto(capturePhoto);

        capturePhoto.showPopup(photoLayout, object, bind.key());
    }

    @Override
    public void toObject(Bind bind, View view, ParseObject object) {

        if (photoFilePath != null) {

            ParseFilesSaveService.add(photoFilePath, object, bind.key(), view.getContext());

        }
    }
}
