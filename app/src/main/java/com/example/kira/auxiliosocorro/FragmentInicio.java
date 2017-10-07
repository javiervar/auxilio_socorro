package com.example.kira.auxiliosocorro;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    public static FragmentInicio newInstance() {
        return new FragmentInicio();
    }


    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragmento_inicio, container, false);
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

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
                    Toast.makeText(getContext(),"es android 6",Toast.LENGTH_SHORT).show();
                    // versiones con android 6.0 o superior
                   /* if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {*/
                    int permissionCheck = ContextCompat.checkSelfPermission(
                            getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
                    int permissionCheck2 = ContextCompat.checkSelfPermission(
                            getContext(), Manifest.permission.ACCESS_FINE_LOCATION);

                    if(permissionCheck!= PackageManager.PERMISSION_GRANTED || permissionCheck2!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 225);
                            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 225);


                    } else {
                    // Show rationale and request permission.
                        Toast.makeText(getContext(),"si obtuve permiso",Toast.LENGTH_SHORT).show();
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
                                            String lat = object.get("latitud").getAsString();
                                            String lon=object.get("longitud").getAsString();
                                            String nombre=object.get("per_razon_social").getAsString();
                                            System.out.println(nombre);
                                            LatLng lugar = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                                            googleMap.addMarker(new MarkerOptions().position(lugar).title(nombre).snippet("Marker Description"));

                                        }
                                        // do stuff with the result or error
                                    }
                                });

                        try{

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
                                        String lat = object.get("latitud").getAsString();
                                        String lon=object.get("longitud").getAsString();
                                        String nombre=object.get("per_razon_social").getAsString();
                                        System.out.println(nombre);
                                        LatLng lugar = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                                        googleMap.addMarker(new MarkerOptions().position(lugar).title(nombre).snippet("Marker Description"));

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

    public void setLista(JsonObject result){
        jsonLugares=result;
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
