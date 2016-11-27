package com.example.s535.mapmapmap;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import static com.example.s535.mapmapmap.R.id.map1;
import static com.example.s535.mapmapmap.R.mipmap.bear_black;
import static com.example.s535.mapmapmap.R.mipmap.bear_blue;

public class MapsActivity1 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private List<Player> map1PlayerList;
    /*status bar를 위한 변수선언(자기 자신에 대한 정보)*/
    private String UserProfile;
    private int UserTag;
    private double lat,lng;
    private TextView Bar_Profile, Bar_Tag;
    private ToggleButton Bar_GPSToggle;
    private ImageButton Bar_Setting;
    private LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_maps1);
        /*상태바를 위한 onCreate함수 구현*/
        {
            Bar_Profile = (TextView)findViewById(R.id.Bar_Profile);
            Bar_Tag=(TextView)findViewById(R.id.Bar_Tag);
            Bar_GPSToggle=(ToggleButton) findViewById(R.id.Bar_GPSToggle);
            Bar_Setting=(ImageButton)findViewById(R.id.Bar_Setting);
            lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);

            /*getIntent로 정보 받아와야함*/
            UserProfile="상메";
            UserTag=0;
            Bar_Profile.setText(UserProfile);
            Bar_GPSToggle.setBackgroundResource(R.mipmap.xbutton);
            Bar_Setting.setBackgroundResource(R.mipmap.settingbutton);
            switch (UserTag)
            {
                case 0: Bar_Tag.setText("A");
                    break;
                case 1: Bar_Tag.setText("B");
                    break;
                case 2: Bar_Tag.setText("C");
                    break;
            }

        /*Bar_Loc설정*/

            Bar_GPSToggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (Bar_GPSToggle.isChecked()) {
                            Bar_GPSToggle.setBackgroundResource(R.mipmap.refreshbutton);
                            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);
                            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, mLocationListener);
                        } else {
                            Bar_GPSToggle.setBackgroundResource(R.mipmap.xbutton);
                            lm.removeUpdates(mLocationListener);
                        }
                    }catch (SecurityException ex) {
                    }
                }
            });

            Bar_Setting.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MapsActivity1.this, SettingActivity.class);
                /*putExtra 사용자 정보*/
                    startActivity(intent);
                }
            });
        }
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
        map1PlayerList=new ArrayList<Player>(); //처음에 한번 리스트 생성
        getPlayers(map1PlayerList); //서버로부터 받아서 리스트에 저장하는 함수
        MarkerOptions marker=new MarkerOptions();
        drawPlayers(map1PlayerList, googleMap); //지도에 그리는함수

        //지도상에 다 표지했다

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

    public void drawPlayers(List<Player> PlayerList, final GoogleMap googleMap)
    {
        MarkerOptions marker=new MarkerOptions();
        for(int i=0; i<PlayerList.size(); i++)
        {
            switch(PlayerList.get(i).getfootType())
            {
                case 1:
                    switch(PlayerList.get(i).getfootColor())
                    {
                        case 1:
                            // case에 따라 marker찍어주기
                            marker.position(new LatLng(PlayerList.get(i).getLat(), PlayerList.get(i).getLng()))
                                    .icon(BitmapDescriptorFactory.fromResource(bear_black));
                            googleMap.addMarker(marker).showInfoWindow();
                            break;
                        case 2:
                            marker.position(new LatLng(PlayerList.get(i).getLat(), PlayerList.get(i).getLng()))
                                    .icon(BitmapDescriptorFactory.fromResource(bear_blue));
                            googleMap.addMarker(marker).showInfoWindow();
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
        //지도상에 다 표지했다
    }

    public void getPlayers(List<Player> PlayerList)
    {
        PlayerList.add(new Player("123", "hello", 36.012096, 129.322351,1,1));
        PlayerList.add(new Player("124", "helloo", 36.011437, 129.321417,1,2));

        //여기에 서버로부터 사용자정보 다 받아오는 함수 !!!! 무조건 한 번은 실행해야지.
        //그다음에 받아온 사용자정보(string type)으로부터 Player 객체 동적할당하고 리스트에 추가
        /*for(int i=0; i<받아온 데이터 줄 수; i++)
        {
           //String temp=받아온 string 한 줄. i로 인덱스줄수있겠지
           //temp에서 id골라내고, gps좌표 골라내고, 상태메시지 등등 골라내서
           if(map1PlayerList.get(i).getX()<36 && map1PlayerList.get(i).getY()<37)
           {
              //map1PlayerList.add(new Player(들어갈 정보들. 생성자참고));
           }
        }*/
        //여기까지해서 리스트 만들었다 이제 맵에 표지하자
    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            double longitude = location.getLongitude(); //경도
            double latitude = location.getLatitude();   //위도
            double altitude = location.getAltitude();   //고도
            float accuracy = location.getAccuracy();    //정확도
            String provider = location.getProvider();   //위치제공자
            /*자기자신 정보를 DB에 보내는 코드 작성*/
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}

/**
 * ATTENTION: This was auto-generated to implement the App Indexing API.
 * See https://g.co/AppIndexing/AndroidStudio for more information.
 */
