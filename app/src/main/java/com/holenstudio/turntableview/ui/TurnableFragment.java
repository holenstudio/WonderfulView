package com.holenstudio.turntableview.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.holenstudio.turntableview.R;
import com.holenstudio.turntableview.view.TurntableView;

/**
 * Created by Holen on 2016/6/14.
 */
public class TurnableFragment extends Fragment {
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

    public static TurnableFragment getInstance(Bundle args) {
        TurnableFragment fragment = new TurnableFragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_turnable, container, false);
        turntableView = (TurntableView) view.findViewById(R.id.turntable_view);
        turntableView.setIconArray(mIconArray);
        turntableView.setSelectedIconArray(mSelectedArray);
        turntableView.setOnDragListener(new TurntableView.OnDragListener() {
            @Override
            public void onDragFinished(int position) {

            }
        });

        return view;
    }
}
