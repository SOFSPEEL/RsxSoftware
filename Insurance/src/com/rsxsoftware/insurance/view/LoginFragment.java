package com.rsxsoftware.insurance.view;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.rsxsoftware.insurance.R;

/**
 * Created by steve.fiedelberg on 12/23/13.
 */
public class LoginFragment extends Fragment {

    private UserActivity activity;
    private EditText user;
    private EditText password;
    private View loginButton;
    private View layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.login, null);
        user = (EditText) layout.findViewById(R.id.user);
        user.setText("Steve");
        password = (EditText) layout.findViewById(R.id.password);
        password.setText("Junk");
        loginButton = layout.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retryLogin();
            }
        });

        return layout;

    }

    @Override
    public void onAttach(Activity activity) {
        this.activity = (UserActivity) activity;
        super.onAttach(activity);
    }

    private void retryLogin() {

        final ProgressFragment progressFragment = new ProgressFragment(getFragmentManager(), "Logging in");

        ParseUser.logInInBackground(user.getText().toString(), password.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser currentUser, ParseException e) {
                progressFragment.dismiss();

                if (e != null) {
                    Toast.makeText(getActivity(), "Failed to login: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    new InventoriesFragment().switchTo(activity, new InventoriesFragment(), new UserFacade());
                }
            }
        });


    }
}
