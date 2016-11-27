package com.example.s535.mapmapmap;

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
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.s535.mapmapmap.R.id.map1;

public class MapsActivity1 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps1);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map1);

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
    public void onMapReady(final GoogleMap googleMap) { //매개변수로 GoogleMap 객체가 넘어옴
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(36.011791, 129.321883) //위도 경도
                , 17
        ));
        //googleMap.getUiSettings().setAllGesturesEnabled(false);

        googleMap.setLatLngBoundsForCameraTarget(new LatLngBounds(new LatLng(36.010070, 129.319867), new LatLng(36.012591, 129.322485)));
        /*googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if(17<=googleMap.getCameraPosition().zoom && googleMap.getCameraPosition().zoom<18) {
                    LatLng temp = googleMap.getCameraPosition().target;
                    double x = temp.longitude;
                    double y = temp.latitude;
                    double maxX = 129.321859;
                    double maxY = 36.012085;
                    double minX = 129.319043;
                    double minY = 36.011417;
                    if (x < minX) x = minX;
                    if (x > maxX) x = maxX;
                    if (y < minY) y = minY;
                    if (y > maxY) y = maxY;
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(
                            new LatLng(y, x)),30,null);
                }
                else if(18<=googleMap.getCameraPosition().zoom && googleMap.getCameraPosition().zoom<19) {
                    LatLng temp = googleMap.getCameraPosition().target;

                    double x = temp.longitude;
                    double y = temp.latitude;
                    double maxX = 129.323892;
                    double maxY = 36.014334;
                    double minX = 129.319302;
                    double minY = 36.009399;
                    if (x < minX) x = minX;
                    if (x > maxX) x = maxX;
                    if (y < minY) y = minY;
                    if (y > maxY) y = maxY;
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(
                            new LatLng(y, x)),30,null);
                }
                else if(19<=googleMap.getCameraPosition().zoom && googleMap.getCameraPosition().zoom<20) {
                    LatLng temp = googleMap.getCameraPosition().target;

                    double x = temp.longitude;
                    double y = temp.latitude;
                    double maxX = 129.323892;
                    double maxY = 36.014334;
                    double minX = 129.320023;
                    double minY = 36.008786;
                    if (x < minX) x = minX;
                    if (x > maxX) x = maxX;
                    if (y < minY) y = minY;
                    if (y > maxY) y = maxY;
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                            new LatLng(y, x)));
                }

            }
        });*/
        googleMap.setMinZoomPreference(17);

        MarkerOptions marker=new MarkerOptions();
        marker.position(new LatLng(36.011791, 129.321883))
                .title("대강당앞광장")
                .snippet("LargePlace");
        googleMap.addMarker(marker).showInfoWindow();

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getApplicationContext(),
                        marker.getTitle() + "클릭했음"
                        , Toast.LENGTH_SHORT).show();
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
