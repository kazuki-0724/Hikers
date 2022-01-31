package com.example.hikers;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Record {

    //日時
    private String date;
    //移動距離
    private int totalDistance;
    //総歩数
    private int totalSteps;
    //polyLine用の地点情報
    private List<LatLng> latLngList;


    public Record(String date, int totalDistance, int totalSteps, List<LatLng> latLngList){
        this.date = date;
        this.totalDistance = totalDistance;
        this.totalSteps =  totalSteps;
        this.latLngList = latLngList;
    }

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

