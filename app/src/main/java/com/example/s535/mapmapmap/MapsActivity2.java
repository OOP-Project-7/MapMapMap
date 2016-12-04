package com.example.s535.mapmapmap;

import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ToggleButton;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity2 extends MapsActivity implements OnMapReadyCallback {

    private ToggleButton Bar_GPSToggle;
    public final static int REPEAT_DELAY=1000;
    public Handler handler;

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

    @Override
    public void onMapReady(final GoogleMap googleMap) { //매개변수로 GoogleMap 객체가 넘어옴

        handler=new Handler()
        {
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                drawPlayers(getList(), googleMap);
                this.sendEmptyMessageDelayed(0,REPEAT_DELAY);
            }
        };

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(36.012588, 129.326288) //위도 경도
                ,17
        ));

        googleMap.setLatLngBoundsForCameraTarget(new LatLngBounds(new LatLng(36.010921 ,129.325962), new LatLng(36.012554, 129.328148)));
        googleMap.setMinZoomPreference(17);

        drawPlayers(getList(), googleMap); //처음 한번은 그려야지그리는함수

        MarkerOptions marker=new MarkerOptions();
        marker.position(new LatLng(36.012588, 129.326288))
                .title("지곡로")
                .snippet("Jigok MainRoad");
        googleMap.addMarker(marker).showInfoWindow();

        final MarkerOptions marker1=new MarkerOptions();
        marker1.position(new LatLng(36.012839, 129.325154))
                .title("도서관")
                .snippet("이동하려면 클릭")
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.chungam, (float) 0.3)));
        googleMap.addMarker(marker1).showInfoWindow();

        Bar_GPSToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Bar_GPSToggle.isChecked()) {
                        Bar_GPSToggle.setBackgroundResource(R.mipmap.refreshbutton);
                        handler.sendEmptyMessage(0);
                    } else {
                        Bar_GPSToggle.setBackgroundResource(R.mipmap.xbutton);
                        handler.removeMessages(0);
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
