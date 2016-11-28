package com.example.s535.mapmapmap;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used. onMapReady는 맵이 사용가능하면 호출되는 callback메소드이다.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Seoul, Korea.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) { //매개변수로 GoogleMap 객체가 넘어옴
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(36.012588, 129.326288) //위도 경도
                ,17
        ));
        //googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.setLatLngBoundsForCameraTarget(new LatLngBounds(new LatLng(36.010921 ,129.325962), new LatLng(36.012554, 129.328148)));
        googleMap.setMinZoomPreference(17);

        MarkerOptions marker=new MarkerOptions();
        marker.position(new LatLng(36.012588, 129.326288))
                .title("지곡로")
                .snippet("Jigok MainRoad");
        googleMap.addMarker(marker).showInfoWindow();

        final MarkerOptions marker1=new MarkerOptions();
        marker1.position(new LatLng(36.012839, 129.325154))
                .title("도서관")
                .snippet("이동하려면 클릭")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.chungam));;
        googleMap.addMarker(marker1).showInfoWindow();

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker markertemp) {
                Toast.makeText(getApplicationContext(),
                        markertemp.getTitle() + "클릭했음"
                        , Toast.LENGTH_SHORT).show();

                if(markertemp.getTitle().equals(marker1.getTitle())) {
                    Intent intent = new Intent(getApplicationContext(), MapsActivityLibrary.class);
                    startActivity(intent);
                }

                return false;
            }
        });
    }
}



        /*mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/


/**
 * ATTENTION: This was auto-generated to implement the App Indexing API.
 * See https://g.co/AppIndexing/AndroidStudio for more information.
 */
