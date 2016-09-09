package com.example.admin.whiteout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(new MainView(this, new WhiteOut(10)));
        b1 = (Button) findViewById(R.id.reset);
    }

    // Reset the game
    public void resetModel(View v) {
        MainView mv = (MainView) findViewById(R.id.whiteout);
        mv.getModel().init();
        mv.postInvalidate();
    }

}
