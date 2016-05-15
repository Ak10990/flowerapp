package com.android.flowerapp.models;

import java.util.ArrayList;

public class Flowers {
    private String result;
    private ArrayList<Data> data;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<Data> getFlowersData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Flowers{" +
                "result='" + result + '\'' +
                ",flowers=" + data +
                '}';
    }
}