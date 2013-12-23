package com.rsxsoftware.insurance.view;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;
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
    private ViewSwitcher switcher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        switcher = (ViewSwitcher) inflater.inflate(R.layout.login, null);
        user = (EditText) switcher.findViewById(R.id.user);
        user.setText("Steve");
        password = (EditText) switcher.findViewById(R.id.password);
        password.setText("Junk");
        loginButton = switcher.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switcher.showNext();
                retryLogin();
            }
        });

        return switcher;

    }

    @Override
    public void onAttach(Activity activity) {
        this.activity = (UserActivity) activity;
        super.onAttach(activity);
    }

    private void retryLogin() {

        final ProgressFragment progressFragment = new ProgressFragment();
        final Bundle args = new Bundle();
        args.putString("title", "Logging in");
        progressFragment.setArguments(args);
        progressFragment.show(getFragmentManager().beginTransaction(), "dialog");


        ParseUser.logInInBackground(user.getText().toString(), password.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser currentUser, ParseException e) {
                progressFragment.dismiss();

                if (e != null) {
                    Toast.makeText(getActivity(), "Failed to login: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    switcher.showNext();
                } else {
                    new InventoriesFragment().switchTo(activity, new InventoriesFragment(), new UserFacade(currentUser));
                }
            }
        });


    }
}
