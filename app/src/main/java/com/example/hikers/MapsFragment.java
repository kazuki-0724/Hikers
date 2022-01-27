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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener, MyLocationManager.OnLocationResultListener{


    private Location currentLocation;
    private List<Location> locationList = new ArrayList<>();
    private MyLocationManager locationManager;
    private GoogleMap googleMap;

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

    }

    @Override
    public void onResume() {
        super.onResume();

        locationManager = new MyLocationManager(getContext(), this);
        locationManager.startLocationUpdates();
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
        //線分を引く(その時に座標のピンを打つ)
        /*
        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        //座標を6つ用意
                        //latLng,
                        new LatLng(-35.016,143.321),
                        new LatLng(-34.747,145.592)
                        //new LatLng(-34.364,147.891),
                        //new LatLng(-33.501,150.217),
                        //new LatLng(-32.306,149.248),
                        //new LatLng(-32.491,147.309)
                ));


        //線に対してタグをつける
        polyline1.setTag("A");
        stylePolyline(polyline1);
        */

        //LatLng init = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        //カメラ設定
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(init, 10));

        /*
        googleMap.setOnPolylineClickListener(this);
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Marker"));
        */

    }




    // [START maps_poly_activity_style_polyline]
    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;

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

    // [END maps_poly_activity_style_polyline]

    // [START maps_poly_activity_on_polyline_click]
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    // Create a stroke pattern of a gap followed by a dot.
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);




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

        //for debug
        Log.d("debug","lat : "+latitude + "long : " + longitude);
        Toast.makeText(getContext(),"lat : "+latitude + " long : " + longitude,Toast.LENGTH_LONG).show();

        currentLocation = locationResult.getLastLocation();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));

    }
}