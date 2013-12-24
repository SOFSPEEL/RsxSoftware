package com.rsxsoftware.insurance.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import com.rsxsoftware.insurance.ParseFileSaveService;
import com.rsxsoftware.insurance.R;
import com.rsxsoftware.insurance.view.bind.*;

/**
 * Created by steve.fiedelberg on 12/15/13.
 */
public class ContentFragment extends FragmentBase {

    @Bind(id = R.id.desc, key = "desc", Converter = BinderText.class)
    private EditText desc;
    @Bind(id = R.id.cost, key = "cost", Converter = BinderNumber.class)
    private EditText cost;
    @Bind(id = R.id.date_purchased, key = "datePurchased", Converter = BinderDate.class)
    private DatePicker datePurchased;
    @Bind(id = R.id.photoLayout, key = "photo", Converter = BinderPhoto.class, requestCode = 1, text = "Item")
    public PhotoLayout photoLayout;
    @Bind(id = R.id.receiptLayout, key = "receipt", Converter = BinderPhoto.class, requestCode = 2, text = "Receipt")
    public PhotoLayout receiptLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View layout = inflater.inflate(R.layout.content, null);

        layout.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        layout.findViewById(R.id.save).setOnClickListener(new SaveListener(new Binder(this, getRealObject(), layout)));

        return layout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode > 100) {
                showPhotoFromFile(requestCode, data);
            } else {
                showPhotoFromCamera(requestCode);
            }
        }
    }

    private void showPhotoFromFile(int requestCode, Intent data) {
        Uri uri = data.getData();
        String scheme = uri.getScheme();
        if ("file".equals(scheme)) {
            final String path = uri.getPath();
            if (requestCode == 101) {
                photoLayout.setPhotoFile(path);
            } else if (requestCode == 102) {
                receiptLayout.setPhotoFile(path);
            }
        } else if ("content".equals(scheme)) {
            // process as a uri that points to a content item
        }
    }

    private void showPhotoFromCamera(int requestCode) {
        if (requestCode == 1 ) {
            photoLayout.setPhotoImage();
        } else if (requestCode == 2) {
            receiptLayout.setPhotoImage();
        }
    }

    private class SaveListener implements View.OnClickListener {
        private final Binder binder;

        public SaveListener(Binder binder) {
            this.binder = binder;
        }

        @Override
        public void onClick(View v) {

            binder.toObject();
            getRealObject().saveEventually();

            final Activity activity = getActivity();
            activity.startService(new Intent(activity, ParseFileSaveService.class));

            getFragmentManager().popBackStack();
        }
    }
}
