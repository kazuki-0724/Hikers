package com.example.hikers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

public class MapsFragment extends Fragment
        implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener, MyLocationManager.OnLocationResultListener {


    private LatLng start;
    private List<LatLng> latLngList = new ArrayList<>();
    private MyLocationManager locationManager;
    private GoogleMap googleMap;
    private Button button;
    private TextView distance;
    private TextView atmosphere;
    private Chronometer chronometer;


    private boolean startFlag = false;
    private float totalDistance = 0;


    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this::onMapReady);
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        locationManager = new MyLocationManager(getContext(), this);
        locationManager.startLocationUpdates();


        distance = (TextView) view.findViewById(R.id.distance);
        atmosphere = (TextView) view.findViewById(R.id.atmosphere);
        chronometer = (Chronometer) view.findViewById(R.id.chronometer);


        //ボタンの挙動
        button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button.getText().equals("START")) {
                    button.setText("STOP");
                    Log.d("debug", "計測開始");

                    //前のデータを削除
                    googleMap.clear();
                    latLngList.clear();
                    distance.setText("0m");
                    atmosphere.setText("0歩");

                    //計測中
                    startFlag = true;
                    locationManager.stopLocationUpdates();
                    locationManager.startLocationUpdates();

                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();

                } else if (button.getText().equals("STOP")) {
                    button.setText("START");
                    Log.d("debug", "計測停止");

                    //計測停止
                    startFlag = false;
                    chronometer.stop();

                    LatLng last = latLngList.get(latLngList.size()-1);
                    googleMap.addMarker(new MarkerOptions().position(last).title("GOAL"));
                    //textViewに反映させる
                    distance.setText(String.format("%.0fm", totalDistance));
                }
            }
        });

        distance.setText("0m");
    }


    @Override
    public void onResume() {
        super.onResume();

        //locationManager = new MyLocationManager(getContext(), this);
        //locationManager.startLocationUpdates();
    }


    @Override
    public void onPause() {
        super.onPause();

        if (locationManager != null) {
            locationManager.stopLocationUpdates();
        }
    }


    @Override
    public void onPolylineClick(@NonNull Polyline polyline) {
        // Flip from solid stroke to dotted stroke pattern.
        if ((polyline.getPattern() == null) || (!polyline.getPattern().contains(DOT))) {
            polyline.setPattern(PATTERN_POLYLINE_DOTTED);
        } else {
            // The default pattern is a solid stroke.
            polyline.setPattern(null);
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);

    }


    public void drawPolyLine(){

        Polyline polyline;
        polyline = googleMap.addPolyline(new PolylineOptions().clickable(true).addAll(latLngList));

        polyline.setTag("ルート");
        stylePolyline(polyline);
    }


    /**
     * 2点間の距離（メートル）、方位角（始点、終点）を取得
     * ※配列で返す[距離、始点から見た方位角、終点から見た方位角]
     */
    public float[] getDistance(double x, double y, double x2, double y2) {

        // 結果を格納するための配列を生成
        float[] results = new float[3];
        // 距離計算
        Location.distanceBetween(x, y, x2, y2, results);

        return results;
    }



    /**
     * Styles the polyline, based on type.
     * @param polyline The polyline object that needs styling.
     */
    private void stylePolyline(Polyline polyline) {
        polyline.setStartCap(new RoundCap());
        polyline.setEndCap(new RoundCap());
        polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
        polyline.setColor(Color.RED);
        polyline.setJointType(JointType.ROUND);
    }


    @Override
    public void onLocationResult(LocationResult locationResult) {

        if (locationResult == null) {
            Log.e("error","# No location data.");
            return;
        }

        // 緯度・経度を取得
        double latitude = locationResult.getLastLocation().getLatitude();
        double longitude = locationResult.getLastLocation().getLongitude();
        LatLng latLng = new LatLng(latitude,longitude);


        if(startFlag == false){
            Log.d("debug","現在地");
            start = latLng;
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 18));
            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(start));
            //googleMap.addMarker(new MarkerOptions().position(current).title("今ここ"));



        }else if(startFlag == true){

            if(latLngList.size() == 0){
                //開始地点に関する処理
                Log.d("debug","開始点");

                //前のデータを削除
                latLngList.clear();
                googleMap.clear();

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start,18));
                googleMap.addMarker(new MarkerOptions().position(start).title("START"));
                latLngList.add(start);

            }else{
                //最後の移動地点を取得
                Log.d("debug","更新");
                LatLng lastLocation = latLngList.get(latLngList.size()-1);
                //最後の移動地点と現在の地点との距離を算出
                float[] distance = getDistance(latitude,longitude, lastLocation.latitude, lastLocation.longitude);
                //移動距離の合計を加算していく
                totalDistance += distance[0];
                this.distance.setText(String.format("%.0fm", totalDistance));
                this.atmosphere.setText(String.format("%.0f歩", totalDistance/0.8));

                //移動地点を追加していく
                latLngList.add(latLng);

                //polyLineの描画
                drawPolyLine();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            }
        }


        //for debug
        Log.d("debug","lat : "+latitude + "long : " + longitude);
        //Toast.makeText(getContext(),latLngList.size() +" lat : "+latitude + " long : " + longitude,Toast.LENGTH_SHORT).show();

    }


}