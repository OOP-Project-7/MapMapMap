package com.example.s535.mapmapmap;

import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity4 extends MapsActivity implements OnMapReadyCallback {

    private ToggleButton Bar_GPSToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        Bar_GPSToggle = (ToggleButton) findViewById(R.id.Bar_GPSToggle);
        Bar_GPSToggle.setBackgroundResource(R.mipmap.xbutton);

        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        GoogleApiClient client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                new LatLng(36.021738, 129.323129) //위도 경도
        ));

        googleMap.setLatLngBoundsForCameraTarget(new LatLngBounds(new LatLng(36.019800, 129.315200), new LatLng(36.024157, 129.323000)));
        googleMap.setMinZoomPreference(17);
        MarkerOptions marker=new MarkerOptions();
        drawPlayers(getList(), googleMap); //처음 한번은 그려야지그리는함수

        marker.position(new LatLng(36.021738, 129.323129))
                .title("체육관 및 실험동")
                .snippet("GYM");
        googleMap.addMarker(marker).showInfoWindow();
        Bar_GPSToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Bar_GPSToggle.isChecked()) {
                        Bar_GPSToggle.setBackgroundResource(R.mipmap.refreshbutton);
                        drawPlayers(getList(), googleMap); //지도에 그리는함수. 나중에 실행시켜보고 수정필요??
                    } else {
                        Bar_GPSToggle.setBackgroundResource(R.mipmap.xbutton);
                    }
                } catch (SecurityException ex) {
                }
            }
        });

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

    }
}