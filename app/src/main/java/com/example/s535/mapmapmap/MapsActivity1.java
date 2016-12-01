package com.example.s535.mapmapmap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


import static com.example.s535.mapmapmap.R.mipmap.bear_black;
import static com.example.s535.mapmapmap.R.mipmap.bear_blue;

public class MapsActivity1 extends MapsActivity implements OnMapReadyCallback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        GoogleApiClient client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onMapReady(final GoogleMap googleMap) { //매개변수로 GoogleMap 객체가 넘어옴

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(36.011791, 129.321883) //위도 경도
                , 17
        ));
        //googleMap.getUiSettings().setAllGesturesEnabled(false);

        googleMap.setLatLngBoundsForCameraTarget(new LatLngBounds(new LatLng(36.010070, 129.319867), new LatLng(36.012591, 129.322485)));
        googleMap.setMinZoomPreference(17);
        List<Player> tempList=new ArrayList<Player>();
        setList(tempList); //처음에 한번 리스트 생성
        getPlayers(getList()); //서버로부터 받아서 리스트에 저장하는 함수
        MarkerOptions marker=new MarkerOptions();
        drawPlayers(getList(), googleMap); //지도에 그리는함수

        //지도상에 다 표지했다

        marker.position(new LatLng(36.011791, 129.321883))
                .title("대강당앞광장")
                .snippet("LargePlace");

        googleMap.addMarker(marker).showInfoWindow();

       /* yearSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){
                    public void onItemSelected
                            (AdapterView<?> parent, View view, int position, long id){
                        birthday_year=1990+position;
                    }
                    public void onNothingSelected(AdapterView<?> parent){
                    }
                }*/
        googleMap.setOnMarkerClickListener(
                new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker) {
                        final Handler handler = new Handler();

                        final long startTime = SystemClock.uptimeMillis();
                        final long duration = 500; //이거 애니메이션 duration임 작을수록 빠르게튄다

                        Projection proj = googleMap.getProjection();
                        final LatLng markerLatLng = marker.getPosition();
                        Point startPoint = proj.toScreenLocation(markerLatLng);
                        startPoint.offset(0, -100);
                        final LatLng startLatLng = proj.fromScreenLocation(startPoint);

                        final Interpolator interpolator = new BounceInterpolator();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                long elapsed = SystemClock.uptimeMillis() - startTime;
                                float t = interpolator.getInterpolation((float) elapsed / duration);
                                double lng = t * markerLatLng.longitude + (1 - t) * startLatLng.longitude;
                                double lat = t * markerLatLng.latitude + (1 - t) * startLatLng.latitude;
                                marker.setPosition(new LatLng(lat, lng));

                                if (t <1.0 ) {
                                    // Post again 16ms later.
                                    handler.postDelayed(this, 16);
                                }
                            }
                        });
                        return false;
                    }
                });
        //googleMap.setOnMarkerClickListener(this);
    }
}

