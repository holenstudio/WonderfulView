package com.holenstudio.turntableview.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.holenstudio.turntableview.R;
import com.holenstudio.turntableview.view.FanlikeView;
import com.holenstudio.turntableview.view.TurntableView;

/**
 * Created by Holen on 2016/6/14.
 */
public class FanlikeFragment extends Fragment {
    private FanlikeView mFanlikeView;

    public static FanlikeFragment getInstance(Bundle args) {
        FanlikeFragment fragment = new FanlikeFragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fanlike, container, false);
        mFanlikeView = (FanlikeView) view.findViewById(R.id.fanlike_view);

        return view;
    }
}
