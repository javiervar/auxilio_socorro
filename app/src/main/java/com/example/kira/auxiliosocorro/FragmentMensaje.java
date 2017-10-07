package com.example.kira.auxiliosocorro;
import android.Manifest;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
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

public class FragmentMensaje extends Fragment{
    private ImageButton contacto;
    public static FragmentMensaje newInstance() {
        return new FragmentMensaje();
    }

LayoutInflater inflater;
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragmento_mensaje, container, false);
        this.inflater = inflater;
        contacto = (ImageButton) view.findViewById(R.id.btnimg);
        contacto.setOnClickListener(eventosMenu);
        return view;


    }
    final View.OnClickListener eventosMenu = new View.OnClickListener() {
        public void onClick(final View v) {
            switch(v.getId()) {
                case R.id.btnimg:
                    AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                    View mView = inflater.inflate(R.layout.dialog_contactos, null);
                    final ListView listaContactos = (ListView) mView.findViewById(R.id.dialoglist);
                    
                    final Button botonAceptar = (Button) mView.findViewById(R.id.buttonaceptar);
                    final Button botonVolver = (Button) mView.findViewById(R.id.buttonvolver);
                    alert.show();
                    break;

            }
        }
    };
}
