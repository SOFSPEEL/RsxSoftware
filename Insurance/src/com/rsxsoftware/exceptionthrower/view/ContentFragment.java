package com.rsxsoftware.exceptionthrower.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import com.rsxsoftware.exceptionthrower.ParseFilesSaveService;
import com.rsxsoftware.exceptionthrower.R;
import com.rsxsoftware.exceptionthrower.view.bind.*;

/**
 * Created by steve.fiedelberg on 12/15/13.
 */
public class ContentFragment extends FragmentBase {

    public static final int REQUEST_PHOTO = 1;
    public static final int REQUEST_RECEIPT = 2;

    @Bind(id = R.id.desc, key = "desc", Converter = BinderText.class)
    private EditText desc;
    @Bind(id = R.id.cost, key = "cost", Converter = BinderNumber.class)
    private EditText cost;
    @Bind(id = R.id.date_purchased, key = "datePurchased", Converter = BinderDate.class)
    private DatePicker datePurchased;
    @Bind(id = R.id.photoLayout, key = "photo", Converter = BinderPhoto.class, requestCode = REQUEST_PHOTO, text = "Item")
    public PhotoLayout photoLayout;
    @Bind(id = R.id.receiptLayout, key = "receipt", Converter = BinderPhoto.class, requestCode = REQUEST_RECEIPT, text = "Receipt")
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
        CapturePhoto capturePhoto = null;
        if ((requestCode == REQUEST_PHOTO) || (requestCode == (100 + REQUEST_PHOTO))) {
            capturePhoto = photoLayout.getCapturePhoto();
        } else if ((requestCode == REQUEST_RECEIPT) || (requestCode == (100 + REQUEST_RECEIPT))) {
            capturePhoto = receiptLayout.getCapturePhoto();
        }

        if (capturePhoto != null) {
            capturePhoto.onActivityResult(requestCode, resultCode, data);
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
            getRealObject().saveInBackground();

            final Activity activity = getActivity();
            activity.startService(new Intent(activity, ParseFilesSaveService.class));

            getFragmentManager().popBackStack();
        }
    }
}
