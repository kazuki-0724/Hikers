package com.example.hikers;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

@Entity
public class Record {

    //ID
    @PrimaryKey(autoGenerate = true)
    private String id;
    //日時
    @ColumnInfo(name = "date")
    private String date;
    //移動距離
    @ColumnInfo(name = "totalDistance")
    private int totalDistance;
    //総歩数
    @ColumnInfo(name = "totalSteps")
    private int totalSteps;

    //polyLine用の地点情報
    private List<LatLng> latLngList;


    public Record(String id, String date, int totalDistance, int totalSteps, List<LatLng> latLngList){
        this.id = id;
        this.date = date;
        this.totalDistance = totalDistance;
        this.totalSteps =  totalSteps;
        this.latLngList = latLngList;
    }

    public String getId() { return id; }

    public String getDate() {
        return date;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public List<LatLng> getLatLngList() {
        return latLngList;
    }

    public void setId(String id) { this.id = id; }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public void setLatLngList(List<LatLng> latLngList) {
        this.latLngList = latLngList;
    }
}

