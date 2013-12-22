package com.rsxsoftware.insurance.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.rsxsoftware.insurance.R;

import static com.rsxsoftware.insurance.view.PhotoLayout.PhotoState.*;

/**
 * Created by steve.fiedelberg on 12/16/13.
 */
public class PhotoLayout extends RelativeLayout {

    PhotoState photoState = TAKE_PHOTO;
    private ImageView photo;
    private ImageButton del;

    public PhotoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void init(String text) {
        photo = (ImageView) findViewById(R.id.photo);
        del = (ImageButton) getChildAt(1);
        final TextView tv = (TextView) getChildAt(2);
        tv.setText(text);
    }
    public boolean havePhoto() {
        return photoState == PhotoLayout.PhotoState.HAVE_PHOTO;
    }

    public void setPhotoImage(Bitmap bitmap) {
        setState(HAVE_PHOTO);
        photo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        photo.setImageBitmap(bitmap);
    }

    public void setPhotoFile(String filePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        setPhotoImage(bitmap);
    }

    private void setState(PhotoState havePhoto) {
        photoState = havePhoto;
        del.setVisibility(photoState.visibility);

    }

    public void setTakePhotoImage() {
        setState(PhotoState.TAKE_PHOTO);
        photo.setImageResource(R.drawable.ic_menu_camera);
    }

    public void setDownloadingImage() {

        setState(DOWNLOADING);
        photo.setImageResource(R.drawable.stat_sys_download);
    }


    public ImageView getPhoto() {
        return photo;
    }




    public ImageButton getDel() {
        return del;
    }




    enum PhotoState {
        TAKE_PHOTO(GONE),
        DOWNLOADING(GONE),
        HAVE_PHOTO(VISIBLE);
        private final int visibility;

        PhotoState(int visibility) {

            this.visibility = visibility;
        }

    }
}
