package com.rsxsoftware.exceptionthrower;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by steve.fiedelberg on 12/14/13.
 */
public class ThrowExceptionActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final View throwButton = findViewById(R.id.buttonThrow);
        throwButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 float f = 8/0;
            }
        });
    }
}