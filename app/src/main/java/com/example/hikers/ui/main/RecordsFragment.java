package com.example.hikers.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hikers.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Kazuki0724
 */
public class RecordsFragment extends Fragment
        implements OnMapReadyCallback, AdapterView.OnItemClickListener {


    private ListView spotListView;
    private CalendarView calendarView;
    private ArrayAdapter<String> spotArrayAdapter;
    private List<LatLng> latLngList1;
    private List<LatLng> latLngList2;
    private List<LatLng> latLngList3;

    //テスト用
    final static String[] items = {"1","2","3"};

    private GoogleMap googleMap;

    private String selectedDay;





    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_records,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //生成
        spotListView = (ListView) view.findViewById(R.id.spotListView);
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        spotArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,items);

        //listViewにアダプターセット
        spotListView.setAdapter(spotArrayAdapter);
        spotListView.setOnItemClickListener(this);

        /**********************************/
        //テスト用
        latLngList1 = new ArrayList<>();
        latLngList2 = new ArrayList<>();
        latLngList3 = new ArrayList<>();
        /**********************************/


        //カレンダーの挙動
        calendarView.setOnDateChangeListener((calendarView, year, month, day) -> selectedDay = String.format("%d/%d/%d",year,month+1,day));

        //マップ関連
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this::onMapReady);
        }

        //当日だけは日付データがないから
        Calendar calendar = Calendar.getInstance();
        selectedDay = String.format("%d/%d/%d",calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE));


        /****************************************************************/
        //テスト用データ
        LatLng[] latLngs = new LatLng[6];
        latLngs[0] = new LatLng(-35.016, 143.321);
        latLngs[1] = new LatLng(-34.747, 145.592);
        latLngs[2] = new LatLng(-34.364, 147.891);
        latLngs[3] = new LatLng(-33.501, 150.217);
        latLngs[4] = new LatLng(-32.306, 149.248);
        latLngs[5] = new LatLng(-32.491, 147.309);

        latLngList1.add(latLngs[0]);
        latLngList1.add(latLngs[1]);
        latLngList1.add(latLngs[2]);
        latLngList1.add(latLngs[3]);
        latLngList1.add(latLngs[4]);
        latLngList1.add(latLngs[5]);

        latLngList2.add(latLngs[0]);
        latLngList2.add(latLngs[1]);
        latLngList2.add(latLngs[2]);
        latLngList2.add(latLngs[3]);

        latLngList3.add(latLngs[3]);
        latLngList3.add(latLngs[5]);

        /****************************************************************/

    }


    /**
     * マップに描画する
     * @param latLngList 取得した移動座標のリスト
     */
    public void drawPolyLine(List<LatLng> latLngList){

        this.googleMap.clear();
        Polyline polyline = this.googleMap.addPolyline(new PolylineOptions().clickable(false).addAll(latLngList));
        polyline.setColor(Color.RED);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngList.get( (latLngList.size()-1)/2 ), 10));
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Log.d("debug",selectedDay + " " + position);
        Toast.makeText(getContext(),selectedDay + " " + position,Toast.LENGTH_LONG).show();

        /**
         * positionがListViewのどのアイテムなのかを表している
         */



        if(position == 0){
            drawPolyLine(latLngList1);
        }else if(position == 1){
            drawPolyLine(latLngList2);
        }else if(position == 2){
            drawPolyLine(latLngList3);
        }


    }

}
