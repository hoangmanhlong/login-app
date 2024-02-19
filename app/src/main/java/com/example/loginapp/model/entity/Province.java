package com.example.loginapp.model.entity;

import com.google.gson.JsonIOException;

import org.json.JSONException;
import org.json.JSONObject;

public class Province {
    private int id;

    private String name;

    private String country;

    private Coord coord;

    public Province(int id, String name, String country, Coord coord) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.coord = coord;
    }

    public Province(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getInt("id");
        name = jsonObject.getString("name");
        country = jsonObject.getString("country");
        JSONObject coord = jsonObject.getJSONObject("coord");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public Coord getCoord() {
        return coord;
    }
}
