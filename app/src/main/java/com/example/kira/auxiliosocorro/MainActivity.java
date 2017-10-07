package com.example.kira.auxiliosocorro;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationManager;


import com.example.kira.auxiliosocorro.Gillotine.animation.GuillotineAnimation;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.kira.auxiliosocorro.R.id.editText;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
    private static final long RIPPLE_DURATION = 250;

    private Toolbar toolbar;

    private FrameLayout root;

    private View contentHamburger;

    JsonObject jsonLugares=new JsonObject();

    //pruebas de localizacion
    private LocationManager locationManager;
    private Location location;
    private String latitud="ERROR",longitud="ERROR",direccion="ERROR";
    private Criteria criteria = new Criteria();
    private TextToSpeech textToSpeech;//pasar texto a voz
    private boolean sigue=false;//saber si seguir con la alarma


    private Button btnInicio,btnRefugio, btnAcopio,btnSocorro,btnAlarma;
    private GuillotineAnimation g;

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
        btnAlarma=g.getBtnAlarma();
        btnAlarma.setOnClickListener(eventosMenu);

    }


    final View.OnClickListener eventosMenu = new View.OnClickListener() {
        public void onClick(final View v) {
            switch(v.getId()) {
                case R.id.btn_inicio:
                    btnInicio.setBackgroundResource(R.drawable.ripple);
                    btnRefugio.setBackgroundColor(Color.TRANSPARENT);
                    btnAcopio.setBackgroundColor(Color.TRANSPARENT);
                    btnSocorro.setBackgroundColor(Color.TRANSPARENT);
                    btnAlarma.setBackgroundColor(Color.TRANSPARENT);
                    loadFragment(new FragmentInicio());
                    break;
                case R.id.btn_refugio:
                    btnRefugio.setBackgroundResource(R.drawable.ripple);
                    btnInicio.setBackgroundColor(Color.TRANSPARENT);
                    btnAcopio.setBackgroundColor(Color.TRANSPARENT);
                    btnSocorro.setBackgroundColor(Color.TRANSPARENT);
                    btnAlarma.setBackgroundColor(Color.TRANSPARENT);
                    loadFragment(new FragmentRefugio());
                    break;
                case R.id.btn_acopio:
                    btnAcopio.setBackgroundResource(R.drawable.ripple);
                    btnRefugio.setBackgroundColor(Color.TRANSPARENT);
                    btnInicio.setBackgroundColor(Color.TRANSPARENT);
                    btnSocorro.setBackgroundColor(Color.TRANSPARENT);
                    btnAlarma.setBackgroundColor(Color.TRANSPARENT);
                    loadFragment(new FragmentAcopio());
                    break;
                case R.id.btn_socorro://mandar mensaje de axilio
                    //btnSocorro.setBackgroundResource(R.drawable.ripple);
                    btnSocorro.setBackgroundColor(Color.TRANSPARENT);
                    btnAcopio.setBackgroundColor(Color.TRANSPARENT);
                    btnRefugio.setBackgroundColor(Color.TRANSPARENT);
                    btnInicio.setBackgroundColor(Color.TRANSPARENT);
                    btnAlarma.setBackgroundColor(Color.TRANSPARENT);
                    //loadFragment(new FragmentAcopio());
                    Toast.makeText(getBaseContext(), "SOCORRO!!! YA VALIO ESTO", Toast.LENGTH_SHORT).show();
                    //enviar mensaje de prueba
                    String strPhone = "6641375618";
                    //String strPhone = "6641184394";
                    String strMessage = "";

                    //intento 2
                    //obtener la posicion de la persona

                    //comprobamos GPS Activado
                    if(isLocationEnabled()){
                        //si esta activado hacemos todo para mandar el mensaje

                        //saber version de android
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
                            // versiones con android 6.0 o superior

                            //antes de mandar mensaje obtenemos la posicion
                            obtenPosicion();//seteamos posicion
                            mandaPosicion();//mandamos posicion
                            setLocation(location);//obtener direccion
                            checkSMSStatePermission();//pedimos permiso para la versiones de 6 o mayores
                            mandaMensajes(getBaseContext());

                            /*strMessage = "Auxilio estoy en Peligro, mi direccion es>: \r\n\r\n"+direccion+
                                    "mi ubicacion es: ";


                            //mandamos mensaje a contactos
                            checkSMSStatePermission();//pedimos permiso para la versiones de 6 o mayores
                            SmsManager sms = SmsManager.getDefault();

                            sms.sendTextMessage(strPhone, null, strMessage, null, null);*/


                        } else{
                            // para versiones anteriores a android 6.0

                            //antes de mandar mensaje obtenemos la posicion
                            obtenPosicion();//seteamos posicion
                            Toast.makeText(getBaseContext(),latitud,Toast.LENGTH_SHORT).show();
                            Toast.makeText(getBaseContext(),longitud,Toast.LENGTH_SHORT).show();

                            //antes de mandar mensaje obtenemos la posicion
                            //obtenPosicion();//seteamos posicion
                            mandaPosicion();//mandamos posicion
                            setLocation(location);//obtener direccion
                            mandaMensajes(getBaseContext());

                           /* strMessage = "Auxilio estoy en Peligro, mi direccion es>: \r\n\r\n"+direccion+
                                    "mi ubicacion es: ";


                            //mandamos mensaje

                            SmsManager sms = SmsManager.getDefault();

                            sms.sendTextMessage(strPhone, null, strMessage+latitud+","+longitud, null, null);*/

                        }


                    }else{//de lo contrario damos una mensaje
                        Toast.makeText(getBaseContext(),"GPS desactivado",Toast.LENGTH_SHORT).show();
                    }



                    break;

                case R.id.btn_alarma:
                    textToSpeech = new TextToSpeech(getApplicationContext(),new TextToSpeech.OnInitListener(){
                        @Override
                        public void onInit(int status) {
                            habla();
                        }
                    });
                    btnAcopio.setBackgroundColor(Color.TRANSPARENT);
                    btnRefugio.setBackgroundColor(Color.TRANSPARENT);
                    btnInicio.setBackgroundColor(Color.TRANSPARENT);
                    btnSocorro.setBackgroundColor(Color.TRANSPARENT);
                    btnAlarma.setBackgroundColor(Color.TRANSPARENT);

                    //loadFragment(new FragmentAcopio());



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

        try{

            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

            latitud =location.getLatitude()+"";
            longitud = location.getLongitude()+"";

        }catch (SecurityException e){

        }



    }

    //Mandar posicion
    public void mandaPosicion(){


    }

    //mandar mensajes
    public void mandaMensajes(Context context){
        Log.i("Mensaje", "Mandare SMS.");
        /*JsonObject json = new JsonObject();
        json.addProperty("api", "0ct0d3v5");
        json.addProperty("operacion", "4");
        json.addProperty("idUsuario", "1");*/

        Ion.with(context)
                .load("https://auxiliosocorro.octodevs.com/Consultas")
                .setBodyParameter("api", "0ct0d3v5")
                .setBodyParameter("operacion", "4")
                .setBodyParameter("usuarioId", "1")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        setLista(result);
                        Log.i("JSON", result+"");
                        JsonArray listaContactos = result.getAsJsonArray("listaContactos");
                        Log.i("ListaContactos", listaContactos+"");
                        // do stuff with the result or error
                        JsonArray jsonArray = result.getAsJsonArray("listaContactos");
                        Log.i("jsonArray", jsonArray+"");

                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject object = jsonArray.get(i).getAsJsonObject();
                            int tipoContacto=object.get("tipoContacto").getAsInt();
                            String Mensaje = object.get("mensaje").getAsString()+"estoy en : "+direccion+
                                    "y mi localizacion es: "+latitud+","+longitud;
                            String telefono=object.get("telefono").getAsString();

                            /*if (tipoContacto==1){
                                bitmapDescriptor
                                        = BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_RED);
                            }else{
                                bitmapDescriptor
                                        = BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_RED);
                            }*/
                            //mandamos mensaje a contactos

                            SmsManager sms = SmsManager.getDefault();

                            sms.sendTextMessage(telefono, null,Mensaje, null, null);


                        }


                    }
                });
    }


    public void setLista(JsonObject result){
        jsonLugares=result;
    }

    //obten direccion
    public void setLocation(Location loc) {
        //Obtener la direcci—n de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address address = list.get(0);
                    ///messageTextView2.setText("Mi direcci—n es: \n" + address.getAddressLine(0));
                    direccion=address.getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //saber si se tiene el GPS Activado
    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    //pasartexto a voz
    private void habla(){
        textToSpeech.setLanguage( new Locale( "spa", "ESP" ) );
        String auxilio="AUXILIO SOCORRO";

        speak(auxilio);



    }

    private void speak( String str )
    {
        textToSpeech.speak( str, TextToSpeech.QUEUE_FLUSH, null );
        textToSpeech.setSpeechRate( 0.0f );
        textToSpeech.setPitch( 0.0f );
    }



    public void loadFragment(Fragment fragment) {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.details_fragment, fragment);
        fragmentTransaction.commit(); // save the changes
    }



    @Override
    public void onInit( int status )
    {
        if ( status == TextToSpeech.LANG_MISSING_DATA | status == TextToSpeech.LANG_NOT_SUPPORTED )
        {
            Toast.makeText( this, "ERROR LANG_MISSING_DATA | LANG_NOT_SUPPORTED", Toast.LENGTH_SHORT ).show();
        }
    }



}
