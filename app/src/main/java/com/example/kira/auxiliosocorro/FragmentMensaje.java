package com.example.kira.auxiliosocorro;
import android.Manifest;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.VectorEnabledTintResources;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;

import java.lang.reflect.Array;
import java.util.ArrayList;
/**
 * Created by jose_ on 07/10/2017.
 */

public class FragmentMensaje extends Fragment {
    private ImageButton contacto;
    public static FragmentMensaje newInstance() {
        return new FragmentMensaje();
    }


    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragmento_mensaje, container, false);
        contacto = (ImageButton) view.findViewById(R.id.btnimg);
        contacto.setOnClickListener(eventosMenu);
        return view;


    }
    final View.OnClickListener eventosMenu = new View.OnClickListener() {
        public void onClick(final View v) {
            switch(v.getId()) {
                case R.id.btnimg:
                    
                    break;

            }
        }
    };
}
