package com.example.kira.auxiliosocorro;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kira on 6/10/17.
 */

public class FragmentInicio extends Fragment {
    public static FragmentInicio newInstance() {
        return new FragmentInicio();
    }


    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragmento_inicio, container, false);
        return view;
    }

}
