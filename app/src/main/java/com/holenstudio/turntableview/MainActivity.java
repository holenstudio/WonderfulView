package com.holenstudio.turntableview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.holenstudio.turntableview.view.TurntableView;

public class MainActivity extends AppCompatActivity {

    private TurntableView turntableView;
    private int[] mIconArray = {
            R.drawable.auto
            , R.drawable.flower
            , R.drawable.night
            , R.drawable.run
            , R.drawable.scene
            , R.drawable.time
    };
    private int[] mSelectedArray = {
            R.drawable.auto_selected
            , R.drawable.flower_selected
            , R.drawable.night_selected
            , R.drawable.run_selected
            , R.drawable.scene_selected
            , R.drawable.time_selected
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        turntableView = (TurntableView) findViewById(R.id.turntable_view);
        turntableView.setIconArray(mIconArray);
        turntableView.setSelectedIconArray(mSelectedArray);
        turntableView.setOnDragListener(new TurntableView.OnDragListener() {
            @Override
            public void onDragFinished(int position) {

            }
        });
    }
}
