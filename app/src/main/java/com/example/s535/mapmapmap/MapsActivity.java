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

import android.Manifest;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
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
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener {

    private ArrayList<User> mapPlayerList;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    /*status bar를 위한 변수선언(자기 자신에 대한 정보)*/
    private String UserId;
    private String UserProfile;
    private int UserTag;
    private double lat, lng;
    private TextView Bar_Profile, Bar_Tag;
    private ImageButton Bar_Setting;
    private LocationManager lm;
    private LocationListener mLocationListener;
    private List<Marker> markerList;
    private int markerNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // only for gingerbread and newer versions
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            //map의 oncreate에서 내 GPS좌표 얻어다가 서버에 보내서 내 좌표 초기화시켜줘야한다
        }

        /*여기에서 사용자들 정보를 서버에서 다 받아다가 리스트에 저장*/
        mapPlayerList = new ArrayList<User>(); //mapPlayerList공간 동적할당
        markerList=new ArrayList<Marker>();
        markerNum=0;

        mRef = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();


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
                }
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

        UserProfile = "상메";
        UserTag = 0;
        Bar_Profile.setText(UserProfile);

        Bar_Setting.setBackgroundResource(R.mipmap.settingbutton); //태그고치기
        switch (UserTag) {
            case 0:
                Bar_Tag.setText("A");
                break;
            case 1:
                Bar_Tag.setText("B");
                break;
            case 2:
                Bar_Tag.setText("C");
                break;
        }

        /*Bar_Loc설정*/

        Bar_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*putExtra 사용자 정보*/
                Intent it = new Intent(MapsActivity.this, EditActivity.class);
                it.putExtra("users", mapPlayerList);
                startActivity(it);
            }
        });

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                //여기서 위치값이 갱신되면 이벤트가 발생한다.
                //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.
                Toast.makeText(getApplicationContext(), "로케이션체인지", Toast.LENGTH_SHORT).show(); //여기도수정
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


    public void drawPlayers(ArrayList<User> UserList,  final GoogleMap googleMap) {
        for(int i=0; i<markerNum; i++)
        {
            markerList.get(i).remove();
        }
        MarkerOptions marker = new MarkerOptions();
        Marker markertemp;

        for (int i = 0; i < UserList.size(); i++) {
            if (UserList.get(i).getLatitude() != 0) {
                switch (UserList.get(i).getFootType()) {
                    case 0:
                        switch (UserList.get(i).getFootColor()) {
                            case 0:
                                // case에 따라 marker찍어주기
                                marker.position(new LatLng(UserList.get(i).getLatitude(), UserList.get(i).getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.bear_black, (float) 0.3)));

                                markertemp=googleMap.addMarker(marker);
                                markerList.add(markertemp);
                                break;
                            case 1:
                                marker.position(new LatLng(UserList.get(i).getLatitude(), UserList.get(i).getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.bear_red, (float) 0.3)));

                                markertemp=googleMap.addMarker(marker);
                                markerList.add(markertemp);
                                break;
                            case 2:
                                marker.position(new LatLng(UserList.get(i).getLatitude(), UserList.get(i).getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.bear_blue, (float) 0.3)));

                                markertemp=googleMap.addMarker(marker);
                                markerList.add(markertemp);
                                break;
                            default:
                                break;
                        }
                        break;
                    case 1:
                        switch (UserList.get(i).getFootColor()) {
                            case 0:
                                // case에 따라 marker찍어주기
                                marker.position(new LatLng(UserList.get(i).getLatitude(), UserList.get(i).getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.bird_black, (float) 0.3)));

                                markertemp=googleMap.addMarker(marker);
                                markerList.add(markertemp);
                                break;
                            case 1:
                                marker.position(new LatLng(UserList.get(i).getLatitude(), UserList.get(i).getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.bird_red, (float) 0.3)));

                                markertemp=googleMap.addMarker(marker);
                                markerList.add(markertemp);
                                break;
                            case 2:
                                marker.position(new LatLng(UserList.get(i).getLatitude(), UserList.get(i).getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.bird_blue, (float) 0.3)));

                                markertemp=googleMap.addMarker(marker);
                                markerList.add(markertemp);
                                break;
                            default:
                                break;
                        }
                        break;
                    case 2:
                        switch (UserList.get(i).getFootColor()) {
                            case 0:
                                // case에 따라 marker찍어주기
                                marker.position(new LatLng(UserList.get(i).getLatitude(), UserList.get(i).getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.frog_black, (float) 0.3)));
                                markertemp=googleMap.addMarker(marker);
                                markerList.add(markertemp);
                                break;
                            case 1:
                                marker.position(new LatLng(UserList.get(i).getLatitude(), UserList.get(i).getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.frog_red, (float) 0.3)));
                                markertemp=googleMap.addMarker(marker);
                                markerList.add(markertemp);
                                break;
                            case 2:
                                marker.position(new LatLng(UserList.get(i).getLatitude(), UserList.get(i).getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(bitmapSizeByScall(R.mipmap.frog_red, (float) 0.3)));
                                markertemp=googleMap.addMarker(marker);
                                markerList.add(markertemp);
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }

            markerNum++;
            }

        }
        for(int i=0; i<markerNum; i++)
        {
            markerList.get(i).showInfoWindow();
        }
        //지도상에 다 표지했다
    }



    public void setList(ArrayList<User> list) {
        mapPlayerList = list;
    }

    public ArrayList<User> getList() {
        return mapPlayerList;
    }

    //base class의 마커, 어차피 세부맵에서 overriding해서 재정의
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(getApplicationContext(),
    marker.getTitle() + "클릭했음"
            , Toast.LENGTH_SHORT).show();
    return false;
    }

}