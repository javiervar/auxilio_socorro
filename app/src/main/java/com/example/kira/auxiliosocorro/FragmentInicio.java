package com.example.kira.auxiliosocorro;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.Inflater;

//import static com.example.kira.auxiliosocorro.R.id.imageView;


/**
 * Created by kira on 6/10/17.
 */

public class FragmentInicio extends Fragment {
    MapView mMapView;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private Location location;
    private Criteria criteria = new Criteria();
    JsonObject jsonLugares=new JsonObject();
    String longitud="",latitud="",direccion="";
    public static FragmentInicio newInstance() {
        return new FragmentInicio();
    }



    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragmento_inicio, container, false);
        ImageView elBoton=(ImageView)view.findViewById(R.id.elBoton);
        elBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Solicitando Ayuda...",Toast.LENGTH_SHORT).show();
                pideAyuda();
            }
        });

        mMapView = (MapView) view.findViewById(R.id.soyelmapa);
        mMapView.onCreate(savedInstanceState);
        getUbicaciones(view.getContext());
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }




        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                if(!isLocationEnabled(view.getContext()))
                    return;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
                    //Toast.makeText(getContext(),"es android 6",Toast.LENGTH_SHORT).show();
                    // versiones con android 6.0 o superior
                   /* if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {*/
                    int permissionCheck = ContextCompat.checkSelfPermission(
                            getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
                    int permissionCheck2 = ContextCompat.checkSelfPermission(
                            getContext(), Manifest.permission.ACCESS_FINE_LOCATION);

                    if(permissionCheck!= PackageManager.PERMISSION_GRANTED || permissionCheck2!= PackageManager.PERMISSION_GRANTED||!isLocationEnabled(view.getContext())){
                            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 225);
                            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 225);


                    } else {
                    // Show rationale and request permission.
                        //Toast.makeText(getContext(),"si obtuve permiso",Toast.LENGTH_SHORT).show();
                        //mMap.setMyLocationEnabled(true);

                        googleMap.setMyLocationEnabled(true);


                        Ion.with(view.getContext())
                                .load("https://auxiliosocorro.octodevs.com/Consultas")
                                .setBodyParameter("api", "0ct0d3v5")
                                .setBodyParameter("operacion", "2")
                                .setBodyParameter("tipoLugar", "2")
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        setLista(result);
                                        JsonArray jsonArray = result.getAsJsonArray("listaLugares");

                                        for (int i = 0; i < jsonArray.size(); i++) {
                                            JsonObject object = jsonArray.get(i).getAsJsonObject();
                                            int tipoLugar=object.get("tipoLugar").getAsInt();
                                            String lat = object.get("latitud").getAsString();
                                            String lon=object.get("longitud").getAsString();
                                            String nombre=object.get("per_razon_social").getAsString();
                                            BitmapDescriptor bitmapDescriptor;
                                            if (tipoLugar==1){
                                                bitmapDescriptor
                                                        = BitmapDescriptorFactory.defaultMarker(
                                                        BitmapDescriptorFactory.HUE_RED);
                                            }else{
                                                bitmapDescriptor
                                                        = BitmapDescriptorFactory.defaultMarker(
                                                        BitmapDescriptorFactory.HUE_RED);
                                            }

                                            LatLng lugar = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                                            googleMap.addMarker(new MarkerOptions().position(lugar).title(nombre).snippet(tipoLugar==2?"REFUGIO":"CENTRO DE ACOPIO"));

                                        }
                                        // do stuff with the result or error
                                    }
                                });

                        try{
                            //para android 6 comentar todo lo de abajo
                            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                            LatLng  ubicacion = new LatLng(location.getLatitude(), location.getLongitude());
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(ubicacion).zoom(12).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }catch (SecurityException e){
                            System.out.println(e);
                        }

                        // For zooming automatically to the location of the marker


                        }


                } else{
                    // para versiones anteriores a android 6.0

                    googleMap.setMyLocationEnabled(true);



                    Ion.with(view.getContext())
                            .load("https://auxiliosocorro.octodevs.com/Consultas")
                            .setBodyParameter("api", "0ct0d3v5")
                            .setBodyParameter("operacion", "6")

                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    setLista(result);
                                    JsonArray jsonArray = result.getAsJsonArray("listaLugares");
                                    System.out.println(jsonArray);

                                    for (int i = 0; i < jsonArray.size(); i++) {
                                        JsonObject object = jsonArray.get(i).getAsJsonObject();
                                        int tipoLugar=object.get("tipoLugar").getAsInt();
                                        String lat = object.get("latitud").getAsString();
                                        String lon=object.get("longitud").getAsString();
                                        String nombre=object.get("per_razon_social").getAsString();
                                        BitmapDescriptor bitmapDescriptor;
                                        if (tipoLugar==1){
                                            bitmapDescriptor
                                                    = BitmapDescriptorFactory.defaultMarker(
                                                    BitmapDescriptorFactory.HUE_GREEN);
                                        }else{
                                            bitmapDescriptor
                                                    = BitmapDescriptorFactory.defaultMarker(
                                                    BitmapDescriptorFactory.HUE_RED);
                                        }

                                        LatLng lugar = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                                        googleMap.addMarker(new MarkerOptions().icon(bitmapDescriptor).position(lugar).title(nombre).snippet(tipoLugar==2?"REFUGIO":"CENTRO DE ACOPIO"));

                                    }
                                    // do stuff with the result or error
                                }
                            });

                    try{

                        location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                        LatLng ubicacion = new LatLng(location.getLatitude(), location.getLongitude());
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(ubicacion).zoom(12).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }catch (SecurityException e){

                    }

                    // For zooming automatically to the location of the marker

                }




                // For showing a move to my location button
                /*if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }*/

            }
        });

        return view;
    }

    public void pideAyuda(){
        //Toast.makeText(getActivity(),"presionado",Toast.LENGTH_SHORT).show();
        //comprobamos GPS Activado
        if(isLocationEnabled(getActivity())){
            //si esta activado hacemos todo para mandar el mensaje

            //saber version de android
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
                // versiones con android 6.0 o superior

                //antes de mandar mensaje obtenemos la posicion
                obtenPosicion();//seteamos posicion
                mandaPosicion();//mandamos posicion
                setLocation(location);//obtener direccion
                checkSMSStatePermission();//pedimos permiso para la versiones de 6 o mayores
                //mandaMensajes(getActivity());
                String tel1="6641184394",tel2="6641621017",tel3="6647532504";

                String strMessage = "Auxilio estoy en Peligro, estoy en : \r\n\r\n"+direccion+
                                    "mi ubicacion es: "+latitud+","+longitud;
                //mandamos mensaje a contactos
                checkSMSStatePermission();//pedimos permiso para la versiones de 6 o mayores
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(tel1, null, strMessage, null, null);
                SmsManager sms2 = SmsManager.getDefault();
                sms2.sendTextMessage(tel2, null, strMessage, null, null);
                SmsManager sms3 = SmsManager.getDefault();
                sms3.sendTextMessage(tel3, null, strMessage, null, null);


            } else{
                // para versiones anteriores a android 6.0

                //antes de mandar mensaje obtenemos la posicion
                obtenPosicion();//seteamos posicion
                Toast.makeText(getActivity(),latitud,Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),longitud,Toast.LENGTH_SHORT).show();

                //antes de mandar mensaje obtenemos la posicion
                //obtenPosicion();//seteamos posicion
                mandaPosicion();//mandamos posicion
                setLocation(location);//obtener direccion
                mandaMensajes(getActivity());

                           /* strMessage = "Auxilio estoy en Peligro, mi direccion es>: \r\n\r\n"+direccion+
                                    "mi ubicacion es: ";


                            //mandamos mensaje

                            SmsManager sms = SmsManager.getDefault();

                            sms.sendTextMessage(strPhone, null, strMessage+latitud+","+longitud, null, null);*/

            }


        }else{//de lo contrario damos una mensaje
            Toast.makeText(getActivity(),"GPS desactivado",Toast.LENGTH_SHORT).show();
        }


    }



    //obtener los permisos necesarios para android 6 en adelante
    public void checkSMSStatePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.SEND_SMS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para enviar SMS.");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 225);
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
                            String Mensaje = object.get("mensaje").getAsString()+" estoy en : "+direccion+
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




    //obten direccion
    public void setLocation(Location loc) {
        //Obtener la direcci—n de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
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
    /*private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }*/




    public void setLista(JsonObject result){
        jsonLugares=result;
    }

    private boolean isLocationEnabled(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    public void getUbicaciones(Context context){


        JsonObject json = new JsonObject();
        json.addProperty("api", "0ct0d3v5");
        json.addProperty("operacion", "2");
        json.addProperty("tipoLugar", "2");

        Ion.with(context)
                .load("https://auxiliosocorro.octodevs.com/Consultas")
                .setBodyParameter("api", "0ct0d3v5")
                .setBodyParameter("operacion", "2")
                .setBodyParameter("tipoLugar", "2")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        setLista(result);

                        // do stuff with the result or error
                    }
                });


    }




}
