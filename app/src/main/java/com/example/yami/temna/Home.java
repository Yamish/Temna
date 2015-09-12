package com.example.yami.temna;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * Created by Yami on 25/08/2015.
 */
public class Home extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        RelativeLayout relative1 = (RelativeLayout) findViewById(R.id.home);
        relative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Home.this, MainActivity.class);
                startActivity(myIntent);
            }
        });
    }

}
