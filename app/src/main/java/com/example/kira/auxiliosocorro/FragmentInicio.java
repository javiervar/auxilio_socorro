package com.example.kira.auxiliosocorro;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


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

    public static FragmentInicio newInstance() {
        return new FragmentInicio();
    }


    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragmento_inicio, container, false);
        mMapView = (MapView) view.findViewById(R.id.soyelmapa);
        mMapView.onCreate(savedInstanceState);
        getUbicaciones();
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

                        // For dropping a marker at a point on the Map
                        LatLng sydney = new LatLng(-34, 151);
                        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                        // For zooming automatically to the location of the marker
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        }


                } else{
                    // para versiones anteriores a android 6.0

                    googleMap.setMyLocationEnabled(true);

                    // For dropping a marker at a point on the Map
                    LatLng sydney = new LatLng(-34, 151);
                    googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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

    public List<Ubicacion> getUbicaciones(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("api", "0ct0d3v5");
        map.put("operacion", "2");
        map.put("tipoLugar", "2");
        List<Ubicacion> ubicacions=new ArrayList<Ubicacion>();
        AndroidNetworking.get("https://auxiliosocorro.octodevs.com/Consultas")
                .addPathParameter(map)
                .addQueryParameter(map)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response
                        System.out.println("--------------------------------");
                        System.out.println(response+"");
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });

        return ubicacions;
    }



}
