package com.example.kira.auxiliosocorro;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.kira.auxiliosocorro.Gillotine.animation.GuillotineAnimation;

public class MainActivity extends AppCompatActivity {
    private static final long RIPPLE_DURATION = 250;

    private Toolbar toolbar;

    private FrameLayout root;

    private View contentHamburger;


    private Button btnInicio,btnRefugio, btnAcopio,btnSocorro;
    private GuillotineAnimation g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        root=(FrameLayout) findViewById(R.id.root);
        contentHamburger=(View)findViewById(R.id.content_hamburger);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }

        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);

        g= new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();

        btnInicio =g.getBtnInicio();
        btnRefugio=g.getBtnRefugio();
        btnAcopio=g.getBtnAcopio();
        btnInicio.setOnClickListener(eventosMenu);
        btnRefugio.setOnClickListener(eventosMenu);
        btnAcopio.setOnClickListener(eventosMenu);
        //agregando boton prueba mensaje socorro
        btnSocorro=g.getBtnSocorro();
        btnSocorro.setOnClickListener(eventosMenu);

    }


    final View.OnClickListener eventosMenu = new View.OnClickListener() {
        public void onClick(final View v) {
            switch(v.getId()) {
                case R.id.btn_inicio:
                    btnInicio.setBackgroundResource(R.drawable.ripple);
                    btnRefugio.setBackgroundColor(Color.TRANSPARENT);
                    btnAcopio.setBackgroundColor(Color.TRANSPARENT);
                    btnSocorro.setBackgroundColor(Color.TRANSPARENT);
                    loadFragment(new FragmentInicio());
                    break;
                case R.id.btn_refugio:
                    btnRefugio.setBackgroundResource(R.drawable.ripple);
                    btnInicio.setBackgroundColor(Color.TRANSPARENT);
                    btnAcopio.setBackgroundColor(Color.TRANSPARENT);
                    btnSocorro.setBackgroundColor(Color.TRANSPARENT);
                    loadFragment(new FragmentRefugio());
                    break;
                case R.id.btn_acopio:
                    btnAcopio.setBackgroundResource(R.drawable.ripple);
                    btnRefugio.setBackgroundColor(Color.TRANSPARENT);
                    btnInicio.setBackgroundColor(Color.TRANSPARENT);
                    btnSocorro.setBackgroundColor(Color.TRANSPARENT);
                    loadFragment(new FragmentAcopio());
                    break;
                case R.id.btn_socorro://mandar mensaje de axilio
                    //btnSocorro.setBackgroundResource(R.drawable.ripple);
                    btnSocorro.setBackgroundColor(Color.TRANSPARENT);
                    btnAcopio.setBackgroundColor(Color.TRANSPARENT);
                    btnRefugio.setBackgroundColor(Color.TRANSPARENT);
                    btnInicio.setBackgroundColor(Color.TRANSPARENT);
                    //loadFragment(new FragmentAcopio());
                    Toast.makeText(getBaseContext(), "SOCORRO!!! YA VALIO ESTO", Toast.LENGTH_SHORT).show();
                    //enviar mensaje de prueba
                    String strPhone = "6641375618";
                    //String strPhone = "6641184394";
                    String strMessage = "esto es una prueba, Llorllie es Gay!!! porque le va al AMERICA D: ";

                    //intento uno este no nos sirve XD aun
                    /*Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setType("vnd.android-dir/mms-sms");
                    sendIntent.putExtra("address", strPhone);
                    sendIntent.putExtra("sms_body", strMessage);
                    startActivity(sendIntent);*/

                    //intento 2
                    //saber version de android
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
                        // versiones con android 6.0 o superior
                        checkSMSStatePermission();
                        SmsManager sms = SmsManager.getDefault();

                        sms.sendTextMessage(strPhone, null, strMessage, null, null);


                    } else{
                        // para versiones anteriores a android 6.0
                        SmsManager sms = SmsManager.getDefault();

                        sms.sendTextMessage(strPhone, null, strMessage, null, null);

                    }



                    //Toast.makeText(this, "Sent.", Toast.LENGTH_SHORT).show();

                    break;
            }
            g.close();
        }
    };

    //obtener los permisos necesarios para android 6 en adelante
    public void checkSMSStatePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.SEND_SMS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para enviar SMS.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso para enviar SMS!");
        }
    }

    //obtener la posicion y mandarla a los contactos
    public void obtenPosicion(){


    }



    public void loadFragment(Fragment fragment) {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.details_fragment, fragment);
        fragmentTransaction.commit(); // save the changes
    }



}
