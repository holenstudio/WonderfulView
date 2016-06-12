package com.holenstudio.turntableview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.holenstudio.turntableview.view.TurntableView;

public class MainActivity extends AppCompatActivity {

    private TurntableView turntableView;
    private int[] mIconArray = {
            R.drawable.arrow
            , R.drawable.arrow
            , R.drawable.arrow
            , R.drawable.arrow
            , R.drawable.arrow
            , R.drawable.arrow
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        turntableView = (TurntableView) findViewById(R.id.turntable_view);
        turntableView.setIconArray(mIconArray);
    }
}
