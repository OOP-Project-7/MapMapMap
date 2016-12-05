package com.example.s535.mapmapmap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener {


    private DatabaseReference mRef;
    private DatabaseReference mbRef; // 방명록
    private FirebaseAuth mAuth;

    /*status bar를 위한 변수선언(자기 자신에 대한 정보)*/
    private ArrayList<User> mapPlayerList;
    private List<Marker> markerList;
    private List<Marker> tagMarkerList;
    private List<Building> buildingList;
    private List<Marker> buildingMarkerList;
    private String UserId;
    private String UserProfile;
    private int UserTag;
    private double lat, lng;
    private TextView Bar_Profile, Bar_Tag, Bar_Loc;
    private ImageButton Bar_Setting;
    private LocationManager lm;
    private LocationListener mLocationListener;
    private boolean map1init;
    private boolean map2init;
    private boolean map3init;
    private boolean map4init;
    private User curUser;
    private int curUserindex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // only for gingerbread and newer versions
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            //map의 oncreate에서 내 GPS좌표 얻어다가 서버에 보내서 내 좌표 초기화시켜줘야한다
        }
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        mbRef = FirebaseDatabase.getInstance().getReference("Buildings");
        mAuth = FirebaseAuth.getInstance();


        /*여기에서 사용자들 정보를 서버에서 다 받아다가 리스트에 저장*/
        mapPlayerList = new ArrayList<User>(); //mapPlayerList공간 동적할당
        markerList=new ArrayList<Marker>();
        tagMarkerList=new ArrayList<Marker>();
        buildingList=new ArrayList<Building>();
        buildingMarkerList=new ArrayList<Marker>();
        initBuildingList(buildingList);
        for(int i=0; i<buildingList.size(); i++)
        {
            loadVisitor(buildingList.get(i));
        }


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mapPlayerList.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    String user_id = (String) postSnapshot.child("user_id").getValue();
                    String foot_type = (String) postSnapshot.child("foot_type").getValue();
                    String foot_color = (String) postSnapshot.child("foot_color").getValue();
                    String tag_type = (String) postSnapshot.child("tag_type").getValue();
                    String year = (String) postSnapshot.child("year").getValue();
                    String month = (String) postSnapshot.child("month").getValue();
                    String day = (String) postSnapshot.child("day").getValue();
                    String statusmessage = (String) postSnapshot.child("statusmessage").getValue();
                    String longitude = (String) postSnapshot.child("longitude").getValue();
                    String latitude = (String) postSnapshot.child("latitude").getValue();

                    User user = new User(user_id, foot_type, foot_color, tag_type, year, month, day, statusmessage, latitude, longitude);

                    mapPlayerList.add(user);
                    Bar_Setting.setEnabled(true);
                }
                renewStatusBar();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        UserId=mAuth.getCurrentUser().getUid(); //내 아이디를 참조

        /*상태바 구현*/
        Bar_Profile = (TextView) findViewById(R.id.Bar_Profile);
        Bar_Tag = (TextView) findViewById(R.id.Bar_Tag);
        Bar_Setting = (ImageButton) findViewById(R.id.Bar_Setting);
        Bar_Loc=(TextView)findViewById(R.id.Bar_Loc);


        Bar_Profile.setText("");
        Bar_Tag.setText("");


        Bar_Setting.setBackgroundResource(R.mipmap.settingbutton); //태그고치기
        Bar_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*putExtra 사용자 정보*/
                Intent it = new Intent(MapsActivity.this, EditActivity.class);
                it.putExtra("users", mapPlayerList);
                startActivity(it);
            }
        });
        Bar_Setting.setEnabled(false);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                //여기서 위치값이 갱신되면 이벤트가 발생한다.
                //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.
                lat = location.getLatitude();     //위도
                lng = location.getLongitude();    //경도
                editUserGps(UserId, Double.toString(lat), Double.toString(lng));
                //lat과 lng서버로 보내기
            }
            public void onProviderDisabled(String provider) {
            }
            public void onProviderEnabled(String provider) {
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };

        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 0, mLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000, 0, mLocationListener);
        }
        catch (SecurityException ex){
            Toast.makeText(getApplicationContext(), "위치서비스를 켜주세요!!!", Toast.LENGTH_SHORT).show();
        }
        map1init=false;
        map2init=false;
        map3init=false;
        map4init=false;
    }

    private void editUserGps(String userID, String latitude, String longitude){
        mRef.child(userID).child("latitude").setValue(latitude);
        mRef.child(userID).child("longitude").setValue(longitude);
    }

    public Bitmap bitmapSizeByScall(int ID, float scall_zero_to_one_f) {
        Bitmap bitmapIn = BitmapFactory.decodeResource(getResources(), ID);
        Bitmap bitmapOut = Bitmap.createScaledBitmap(bitmapIn,
                Math.round(bitmapIn.getWidth() * scall_zero_to_one_f),
                Math.round(bitmapIn.getHeight() * scall_zero_to_one_f), false);
        return bitmapOut;
    }

    public void saveVisitor (Building building, String visitor) {
        String key;
        key = mbRef.child(building.getbuildingName()).push().getKey();
        mbRef.child(building.getbuildingName()).child(key).setValue(visitor);
        //방명록 서버에 저장
    }

    public void loadVisitor (final Building building) {
        mbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if (building.getbuildingName().equals(postSnapshot.getKey())) {
                        for(DataSnapshot postpostSnapshot: postSnapshot.getChildren()) {
                            String visitor = (String) postpostSnapshot.getValue();
                            building.getVisitors().add(visitor);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void initMarkers(final GoogleMap googleMap) {
        for(int i=0; i<markerList.size(); i++)
        {
            markerList.get(i).remove();
        }
        for(int i=0; i<tagMarkerList.size(); i++)
        {
            tagMarkerList.get(i).remove();
        }
        MarkerOptions marker = new MarkerOptions();
        Marker markertemp;

        SimpleDateFormat sdf = new SimpleDateFormat("MMd");
        String now = sdf.format(new Date());
        for (int i = 0; i < mapPlayerList.size(); i++) {
                if(mapPlayerList.get(i).getBirthDay().equals(now))
                {
                    marker.position(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()))
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.birthdaycake, (float) 0.3)))
                            .title(mapPlayerList.get(i).getStatusmessage());
                    markertemp = googleMap.addMarker(marker);
                    markerList.add(markertemp);
                }
                else {
                    switch (mapPlayerList.get(i).getFootType()) {
                        case 0:
                            switch (mapPlayerList.get(i).getFootColor()) {
                                case 0:
                                    // case에 따라 marker찍어주기
                                    marker.position(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.bear_black, (float) 0.3)))
                                            .title(mapPlayerList.get(i).getStatusmessage());
                                    markertemp = googleMap.addMarker(marker);
                                    markerList.add(markertemp);
                                    break;
                                case 1:
                                    marker.position(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.bear_red, (float) 0.3)))
                                            .title(mapPlayerList.get(i).getStatusmessage());
                                    markertemp = googleMap.addMarker(marker);
                                    markerList.add(markertemp);
                                    break;
                                case 2:
                                    marker.position(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.bear_blue, (float) 0.3)))
                                            .title(mapPlayerList.get(i).getStatusmessage());
                                    markertemp = googleMap.addMarker(marker);
                                    markerList.add(markertemp);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 1:
                            switch (mapPlayerList.get(i).getFootColor()) {
                                case 0:
                                    // case에 따라 marker찍어주기
                                    marker.position(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.bird_black, (float) 0.3)))
                                            .title(mapPlayerList.get(i).getStatusmessage());
                                    markertemp = googleMap.addMarker(marker);
                                    markerList.add(markertemp);
                                    break;
                                case 1:
                                    marker.position(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.bird_red, (float) 0.3)))
                                            .title(mapPlayerList.get(i).getStatusmessage());
                                    markertemp = googleMap.addMarker(marker);
                                    markerList.add(markertemp);
                                    break;
                                case 2:
                                    marker.position(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.bird_blue, (float) 0.3)))
                                            .title(mapPlayerList.get(i).getStatusmessage());
                                    markertemp = googleMap.addMarker(marker);
                                    markerList.add(markertemp);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 2:
                            switch (mapPlayerList.get(i).getFootColor()) {
                                case 0:
                                    // case에 따라 marker찍어주기
                                    marker.position(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.frog_black, (float) 0.3)))
                                            .title(mapPlayerList.get(i).getStatusmessage());
                                    markertemp = googleMap.addMarker(marker);
                                    markerList.add(markertemp);
                                    break;
                                case 1:
                                    marker.position(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.frog_red, (float) 0.3)))
                                            .title(mapPlayerList.get(i).getStatusmessage());
                                    markertemp = googleMap.addMarker(marker);
                                    markerList.add(markertemp);
                                    break;
                                case 2:
                                    marker.position(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.frog_blue, (float) 0.3)))
                                            .title(mapPlayerList.get(i).getStatusmessage());
                                    markertemp = googleMap.addMarker(marker);
                                    markerList.add(markertemp);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 3:
                            switch (mapPlayerList.get(i).getFootColor()) {
                                case 0:
                                    // case에 따라 marker찍어주기
                                    marker.position(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.dog_black, (float) 0.3)))
                                            .title(mapPlayerList.get(i).getStatusmessage());
                                    markertemp = googleMap.addMarker(marker);
                                    markerList.add(markertemp);
                                    break;
                                case 1:
                                    marker.position(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.dog_red, (float) 0.3)))
                                            .title(mapPlayerList.get(i).getStatusmessage());
                                    markertemp = googleMap.addMarker(marker);
                                    markerList.add(markertemp);
                                    break;
                                case 2:
                                    marker.position(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.dog_blue, (float) 0.3)))
                                            .title(mapPlayerList.get(i).getStatusmessage());
                                    markertemp = googleMap.addMarker(marker);
                                    markerList.add(markertemp);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 4:
                            switch (mapPlayerList.get(i).getFootColor()) {
                                case 0:
                                    // case에 따라 marker찍어주기
                                    marker.position(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.man_black, (float) 0.3)))
                                            .title(mapPlayerList.get(i).getStatusmessage());
                                    markertemp = googleMap.addMarker(marker);
                                    markerList.add(markertemp);
                                    break;
                                case 1:
                                    marker.position(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.man_red, (float) 0.3)))
                                            .title(mapPlayerList.get(i).getStatusmessage());
                                    markertemp = googleMap.addMarker(marker);
                                    markerList.add(markertemp);
                                    break;
                                case 2:
                                    marker.position(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.man_blue, (float) 0.3)))
                                            .title(mapPlayerList.get(i).getStatusmessage());
                                    markertemp = googleMap.addMarker(marker);
                                    markerList.add(markertemp);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 5:
                            switch (mapPlayerList.get(i).getFootColor()) {
                                case 0:
                                    // case에 따라 marker찍어주기
                                    marker.position(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.snickers_black, (float) 0.3)))
                                            .title(mapPlayerList.get(i).getStatusmessage());
                                    markertemp = googleMap.addMarker(marker);
                                    markerList.add(markertemp);
                                    break;
                                case 1:
                                    marker.position(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.snickers_red, (float) 0.3)))
                                            .title(mapPlayerList.get(i).getStatusmessage());
                                    markertemp = googleMap.addMarker(marker);
                                    markerList.add(markertemp);
                                    break;
                                case 2:
                                    marker.position(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.snickers_blue, (float) 0.3)))
                                            .title(mapPlayerList.get(i).getStatusmessage());
                                    markertemp = googleMap.addMarker(marker);
                                    markerList.add(markertemp);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 6:
                            switch (mapPlayerList.get(i).getFootColor()) {
                                case 0:
                                    // case에 따라 marker찍어주기
                                    marker.position(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.heel_black, (float) 0.3)))
                                            .title(mapPlayerList.get(i).getStatusmessage());
                                    markertemp = googleMap.addMarker(marker);
                                    markerList.add(markertemp);
                                    break;
                                case 1:
                                    marker.position(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.heel_red, (float) 0.3)))
                                            .title(mapPlayerList.get(i).getStatusmessage());
                                    markertemp = googleMap.addMarker(marker);
                                    markerList.add(markertemp);
                                    break;
                                case 2:
                                    marker.position(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.heel_blue, (float) 0.3)))
                                            .title(mapPlayerList.get(i).getStatusmessage());
                                    markertemp = googleMap.addMarker(marker);
                                    markerList.add(markertemp);
                                    break;
                                default:
                                    break;
                            }
                            break;
                    }
                }
        }

        MarkerOptions marker1 = new MarkerOptions();
        for(int i=0; i<mapPlayerList.size(); i++)
        {
            switch(mapPlayerList.get(i).getTagType())
            {
                case 0:
                    marker1.position(new LatLng(mapPlayerList.get(i).getLatitude()+0.0002, mapPlayerList.get(i).getLongitude()))
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.imark, (float) 0.2)))
                            .visible(true);
                    markertemp=googleMap.addMarker(marker1);
                    tagMarkerList.add(markertemp);
                    break;
                case 1:
                    marker1.position(new LatLng(mapPlayerList.get(i).getLatitude()+0.0002,mapPlayerList.get(i).getLongitude()))
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.hmark, (float) 0.2)))
                            .visible(true);
                    markertemp=googleMap.addMarker(marker1);
                    tagMarkerList.add(markertemp);
                    break;
                case 2:
                    marker1.position(new LatLng(mapPlayerList.get(i).getLatitude()+0.0002, mapPlayerList.get(i).getLongitude()))
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.qmark, (float) 0.2)))
                            .visible(true);
                    markertemp=googleMap.addMarker(marker1);
                    tagMarkerList.add(markertemp);
                    break;
                case 3:
                    marker1.position(new LatLng(mapPlayerList.get(i).getLatitude()+0.0002, mapPlayerList.get(i).getLongitude()))
                            .visible(false);
                    markertemp=googleMap.addMarker(marker1);
                    tagMarkerList.add(markertemp);
            }
        }
        for(int i=0; i<tagMarkerList.size(); i++)
        {
            tagMarkerList.get(i).setAlpha((float)0.98);
        }
        for(int i=0; i<mapPlayerList.size(); i++)
        {
            if(mapPlayerList.get(i).getUser_id().equals(UserId)) {
                curUserindex = i;
                break;
            }
        }
        for(int i=0; i<markerList.size(); i++)
        {
            markerList.get(i).showInfoWindow();
            tagMarkerList.get(i).showInfoWindow();
        }
        markerList.get(curUserindex).showInfoWindow();
        tagMarkerList.get(curUserindex).showInfoWindow();
        //지도상에 다 표지했다
    }

    public void drawPlayers(final GoogleMap googleMap)
    {
        while(mapPlayerList.size()!=markerList.size())
        {
            initMarkers(googleMap);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MMd");
        String now = sdf.format(new Date());

        for(int i=0; i<mapPlayerList.size(); i++)
        {
            if(mapPlayerList.get(i).getBirthDay().equals(now))
            {
                markerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()));
                markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.birthdaycake, (float) 0.3)));
                markerList.get(i).setTitle(mapPlayerList.get(i).getStatusmessage());
            }

            else{
            switch (mapPlayerList.get(i).getFootType()) {
                case 0:
                    switch (mapPlayerList.get(i).getFootColor()) {
                        case 0:
                            // case에 따라 marker갱신
                            markerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()));
                            markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.bear_black, (float) 0.3)));
                            markerList.get(i).setTitle(mapPlayerList.get(i).getStatusmessage());
                            break;
                        case 1:
                            markerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()));
                            markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.bear_red, (float) 0.3)));
                            markerList.get(i).setTitle(mapPlayerList.get(i).getStatusmessage());
                            break;
                        case 2:
                            markerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()));
                            markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.bear_blue, (float) 0.3)));
                            markerList.get(i).setTitle(mapPlayerList.get(i).getStatusmessage());
                            break;
                        default:
                            break;
                    }
                    break;
                case 1:
                    switch (mapPlayerList.get(i).getFootColor()) {
                        case 0:
                            markerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()));
                            markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.bird_black, (float) 0.3)));
                            markerList.get(i).setTitle(mapPlayerList.get(i).getStatusmessage());
                            break;
                        case 1:
                            markerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()));
                            markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.bird_red, (float) 0.3)));
                            markerList.get(i).setTitle(mapPlayerList.get(i).getStatusmessage());
                            break;
                        case 2:
                            markerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()));
                            markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.bird_blue, (float) 0.3)));
                            markerList.get(i).setTitle(mapPlayerList.get(i).getStatusmessage());
                            break;
                        default:
                            break;
                    }
                    break;
                case 2:
                    switch (mapPlayerList.get(i).getFootColor()) {
                        case 0:
                            markerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()));
                            markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.frog_black, (float) 0.3)));
                            markerList.get(i).setTitle(mapPlayerList.get(i).getStatusmessage());
                            break;
                        case 1:
                            markerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()));
                            markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.frog_red, (float) 0.3)));
                            markerList.get(i).setTitle(mapPlayerList.get(i).getStatusmessage());
                            break;
                        case 2:
                            markerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()));
                            markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.frog_blue, (float) 0.3)));
                            markerList.get(i).setTitle(mapPlayerList.get(i).getStatusmessage());
                            break;
                        default:
                            break;
                    }
                    break;
                case 3:
                    switch (mapPlayerList.get(i).getFootColor()) {
                        case 0:
                            markerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()));
                            markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.dog_black, (float) 0.3)));
                            markerList.get(i).setTitle(mapPlayerList.get(i).getStatusmessage());
                            break;
                        case 1:
                            markerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()));
                            markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.dog_red, (float) 0.3)));
                            markerList.get(i).setTitle(mapPlayerList.get(i).getStatusmessage());
                            break;
                        case 2:
                            markerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()));
                            markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.dog_blue, (float) 0.3)));
                            markerList.get(i).setTitle(mapPlayerList.get(i).getStatusmessage());
                            break;
                        default:
                            break;
                    }
                    break;
                case 4:
                    switch (mapPlayerList.get(i).getFootColor()) {
                        case 0:
                            markerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()));
                            markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.man_black, (float) 0.3)));
                            markerList.get(i).setTitle(mapPlayerList.get(i).getStatusmessage());
                            break;
                        case 1:
                            markerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()));
                            markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.man_red, (float) 0.3)));
                            markerList.get(i).setTitle(mapPlayerList.get(i).getStatusmessage());
                            break;
                        case 2:
                            markerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()));
                            markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.man_blue, (float) 0.3)));
                            markerList.get(i).setTitle(mapPlayerList.get(i).getStatusmessage());
                            break;
                        default:
                            break;
                    }
                    break;
                case 5:
                    switch (mapPlayerList.get(i).getFootColor()) {
                        case 0:
                            markerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()));
                            markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.snickers_black, (float) 0.3)));
                            markerList.get(i).setTitle(mapPlayerList.get(i).getStatusmessage());
                            break;
                        case 1:
                            markerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()));
                            markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.snickers_red, (float) 0.3)));
                            markerList.get(i).setTitle(mapPlayerList.get(i).getStatusmessage());
                            break;
                        case 2:
                            markerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()));
                            markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.snickers_blue, (float) 0.3)));
                            markerList.get(i).setTitle(mapPlayerList.get(i).getStatusmessage());
                            break;
                        default:
                            break;
                    }
                    break;
                case 6:
                    switch (mapPlayerList.get(i).getFootColor()) {
                        case 0:
                            markerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()));
                            markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.heel_black, (float) 0.3)));
                            markerList.get(i).setTitle(mapPlayerList.get(i).getStatusmessage());
                            break;
                        case 1:
                            markerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()));
                            markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.heel_red, (float) 0.3)));
                            markerList.get(i).setTitle(mapPlayerList.get(i).getStatusmessage());
                            break;
                        case 2:
                            markerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude(), mapPlayerList.get(i).getLongitude()));
                            markerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.heel_blue, (float) 0.3)));
                            markerList.get(i).setTitle(mapPlayerList.get(i).getStatusmessage());
                            break;
                        default:
                            break;
                    }
                    break;
                }
            }
         }
        for(int i=0; i<tagMarkerList.size(); i++)
        {
            switch(mapPlayerList.get(i).getTagType())
            {
                case 0:
                    tagMarkerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude()+0.0002, mapPlayerList.get(i).getLongitude()));
                    tagMarkerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.imark, (float) 0.2)));
                    tagMarkerList.get(i).setVisible(true);
                    break;
                case 1:
                    tagMarkerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude()+0.0002, mapPlayerList.get(i).getLongitude()));
                    tagMarkerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.hmark, (float) 0.2)));
                    tagMarkerList.get(i).setVisible(true);
                    break;
                case 2:
                    tagMarkerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude()+0.0002, mapPlayerList.get(i).getLongitude()));
                    tagMarkerList.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.qmark, (float) 0.2)));
                    tagMarkerList.get(i).setVisible(true);
                    break;
                case 3:
                    tagMarkerList.get(i).setPosition(new LatLng(mapPlayerList.get(i).getLatitude()+0.0002, mapPlayerList.get(i).getLongitude()));
                    tagMarkerList.get(i).setVisible(false);
                    break;
            }
        }
    }

    public void drawBuilding(List<Building> list, final GoogleMap googleMap)
    {
        MarkerOptions marker = new MarkerOptions();
        Marker markertemp;

        marker.position(new LatLng(list.get(0).getbuildingLatitude(), list.get(0).getbuildingLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.playground, (float) 0.8)));
        markertemp = googleMap.addMarker(marker);
        buildingMarkerList.add(markertemp);
        marker.position(new LatLng(list.get(1).getbuildingLatitude(), list.get(1).getbuildingLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.plaza, (float) 0.8)));
        markertemp = googleMap.addMarker(marker);
        buildingMarkerList.add(markertemp);
        marker.position(new LatLng(list.get(2).getbuildingLatitude(), list.get(2).getbuildingLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.rist, (float) 0.8)));
        markertemp = googleMap.addMarker(marker);
        buildingMarkerList.add(markertemp);
        marker.position(new LatLng(list.get(3).getbuildingLatitude(), list.get(3).getbuildingLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.hall, (float) 0.8)));
        markertemp = googleMap.addMarker(marker);
        buildingMarkerList.add(markertemp);
        marker.position(new LatLng(list.get(4).getbuildingLatitude(), list.get(4).getbuildingLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.hwanljung, (float) 0.8)));
        markertemp = googleMap.addMarker(marker);
        buildingMarkerList.add(markertemp);
        marker.position(new LatLng(list.get(5).getbuildingLatitude(), list.get(5).getbuildingLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.library, (float) 0.8)));
        markertemp = googleMap.addMarker(marker);
        buildingMarkerList.add(markertemp);
        marker.position(new LatLng(list.get(6).getbuildingLatitude(), list.get(6).getbuildingLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.c5, (float) 0.8)));
        markertemp = googleMap.addMarker(marker);
        buildingMarkerList.add(markertemp);
        marker.position(new LatLng(list.get(7).getbuildingLatitude(), list.get(7).getbuildingLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.life, (float) 0.8)));
        markertemp = googleMap.addMarker(marker);
        buildingMarkerList.add(markertemp);
        marker.position(new LatLng(list.get(8).getbuildingLatitude(), list.get(8).getbuildingLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.gift, (float) 0.8)));
        markertemp = googleMap.addMarker(marker);
        buildingMarkerList.add(markertemp);
        marker.position(new LatLng(list.get(9).getbuildingLatitude(), list.get(9).getbuildingLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.robot, (float) 0.8)));
        markertemp = googleMap.addMarker(marker);
        buildingMarkerList.add(markertemp);
        marker.position(new LatLng(list.get(10).getbuildingLatitude(), list.get(10).getbuildingLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.jigok, (float) 0.8)));
        markertemp = googleMap.addMarker(marker);
        buildingMarkerList.add(markertemp);
        marker.position(new LatLng(list.get(11).getbuildingLatitude(), list.get(11).getbuildingLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.rc, (float) 0.8)));
        markertemp = googleMap.addMarker(marker);
        buildingMarkerList.add(markertemp);
        marker.position(new LatLng(list.get(12).getbuildingLatitude(), list.get(12).getbuildingLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.mandorm, (float) 0.8)));
        markertemp = googleMap.addMarker(marker);
        buildingMarkerList.add(markertemp);
        marker.position(new LatLng(list.get(13).getbuildingLatitude(), list.get(13).getbuildingLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.womandorm, (float) 0.8)));
        markertemp = googleMap.addMarker(marker);
        buildingMarkerList.add(markertemp);
        marker.position(new LatLng(list.get(14).getbuildingLatitude(), list.get(14).getbuildingLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.apartment, (float) 0.8)));
        markertemp = googleMap.addMarker(marker);
        buildingMarkerList.add(markertemp);
        marker.position(new LatLng(list.get(15).getbuildingLatitude(), list.get(15).getbuildingLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.seveneight, (float) 0.8)));
        markertemp = googleMap.addMarker(marker);
        buildingMarkerList.add(markertemp);
        marker.position(new LatLng(list.get(16).getbuildingLatitude(), list.get(16).getbuildingLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.tongzip, (float) 0.8)));
        markertemp = googleMap.addMarker(marker);
        buildingMarkerList.add(markertemp);
        marker.position(new LatLng(list.get(17).getbuildingLatitude(), list.get(17).getbuildingLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.gym, (float) 0.8)));
        markertemp = googleMap.addMarker(marker);
        buildingMarkerList.add(markertemp);
        marker.position(new LatLng(list.get(18).getbuildingLatitude(), list.get(18).getbuildingLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.laboratory, (float) 0.8)));
        markertemp = googleMap.addMarker(marker);
        buildingMarkerList.add(markertemp);
        marker.position(new LatLng(list.get(19).getbuildingLatitude(), list.get(19).getbuildingLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.accelerator, (float) 0.8)));
        markertemp = googleMap.addMarker(marker);
        buildingMarkerList.add(markertemp);

        for(int i=0; i<buildingList.size(); i++)
        {
            buildingMarkerList.get(i).setAlpha((float)0.99); //건물마커를 사용자마커와 구분짓기 위하여
            buildingMarkerList.get(i).showInfoWindow();
        }
    }

    public void initBuildingList(List<Building> list)
    {
        list.add(new Building("playground", 36.013526, 129.319836));
        list.add(new Building("plaza", 36.011917, 129.321739));
        list.add(new Building("rist", 36.010285, 129.324490));
        list.add(new Building("hall", 36.013257, 129.321291));
        list.add(new Building("hwanljung", 36.011714, 129.320300));
        list.add(new Building("library", 36.012272, 129.325658));
        list.add(new Building("c5", 36.013803, 129.326008));
        list.add(new Building("life", 36.011196, 129.325801));
        list.add(new Building("gift", 36.011483, 129.327799));
        list.add(new Building("robot", 36.010816, 129.326695));
        list.add(new Building("jigok", 36.015356, 129.322565));
        list.add(new Building("rc", 36.016837, 129.320320));
        list.add(new Building("mandorm", 36.016080, 129.320953));
        list.add(new Building("womandorm", 36.017015, 129.323278));
        list.add(new Building("apartment", 36.017228, 129.321115));
        list.add(new Building("seveneight", 36.014902, 129.321607));
        list.add(new Building("tongzip", 36.017907, 129.322227));
        list.add(new Building("gym",36.018612, 129.324106));
        list.add(new Building("laboratory",36.021655, 129.319415));
        list.add(new Building("accelerator",36.023543, 129.315588));
    }

    public boolean getmap1init(){return map1init;}
    public boolean getmap2init(){return map2init;}
    public boolean getmap3init(){return map3init;}
    public boolean getmap4init(){return map4init;}
    public void setmap1init(boolean temp){map1init=temp;}
    public void setmap2init(boolean temp){map2init=temp;}
    public void setmap3init(boolean temp){map3init=temp;}
    public void setmap4init(boolean temp){map4init=temp;}


    public void setList(ArrayList<User> list) {
        mapPlayerList = list;
    }

    public ArrayList<User> getList() {return mapPlayerList;}
    public List<Marker> getMarkerList() {return markerList;}
    public List<Building> getBuildingList() {
        return buildingList;
    }

    //base class의 마커, 어차피 세부맵에서 overriding해서 재정의
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(getApplicationContext(),
    marker.getTitle() + "클릭했음"
            , Toast.LENGTH_SHORT).show();
    return false;
    }

    public void setBar_Loc(String loc)
    {
        Bar_Loc.setText(loc);
    }

    private void renewStatusBar()
    {
        curUser=findUserByid(UserId);
        UserProfile=curUser.getStatusmessage();
        UserTag=curUser.getTagType();
        Bar_Profile.setText(UserProfile);
        switch (UserTag) {
            case 0:
                Bar_Tag.setText("공지있어요");
                break;
            case 1:
                Bar_Tag.setText("도와주세요");
                break;
            case 2:
                Bar_Tag.setText("도와드려요");
                break;
            case 3:
                Bar_Tag.setText("없음");
        }
    }

    User findUserByid(String id){
        for (User user : mapPlayerList) {
            if (user.getUser_id().equals(id)) {
                return user;
            }
        }
        return null;
    }
}