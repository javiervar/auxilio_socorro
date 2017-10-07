package com.example.kira.auxiliosocorro;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kira on 6/10/17.
 */

public class FragmentRefugio  extends Fragment {
    public static FragmentRefugio newInstance() {
        return new FragmentRefugio();
    }


    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragmento_refugio, container, false);
        return view;
    }

}