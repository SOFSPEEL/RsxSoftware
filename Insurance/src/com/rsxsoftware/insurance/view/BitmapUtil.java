package com.rsxsoftware.insurance.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

/**
 * Created by steve.fiedelberg on 12/26/13.
 */
public class BitmapUtil {
    public static void ToImageView(ImageView photo, byte[] bytes) {

        // Get the dimensions of the View
        int targetW = photo.getWidth();
        int targetH = photo.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        final Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, bmOptions);
        photo.setImageBitmap(bmp);
    }
}
