package com.bldev.modalviewfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by iclicmx on 06/02/14.
 */
public class MainFragment extends Fragment {


    private View.OnClickListener onClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       View mainCointainer = inflater.inflate(R.layout.main_fragment_layout,container,false);
        mainCointainer.setOnClickListener(onClickListener);

        return mainCointainer;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }



}
