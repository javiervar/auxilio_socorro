package com.example.kira.auxiliosocorro;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by kira on 6/10/17.
 */

public class FragmentAcopio  extends Fragment {
    private EditText nombre,ubicacion;
    public static FragmentAcopio newInstance() {
        return new FragmentAcopio();
    }


    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragmento_acopio, container, false);
        nombre=(EditText)view.findViewById(R.id.add_nombre);
        ubicacion=(EditText)view.findViewById(R.id.add_ubicacion);
        return view;
    }

}