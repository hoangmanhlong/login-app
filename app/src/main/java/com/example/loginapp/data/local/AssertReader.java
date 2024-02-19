package com.example.loginapp.data.local;

import com.example.loginapp.App;
import com.example.loginapp.model.entity.Province;
import com.example.loginapp.utils.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class AssertReader {
    public  static String[] getProvinces() {
        try {
            JSONArray jsonArray = JsonReader.loadJSONArrayFromAsset(App.getInstance(), "city.list.min.json");
            List<Province> provinces = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Province province = new Province(jsonObject);
                provinces.add(province);
            }
            String[] provinceNames = new String[provinces.size()];
            for (int i = 0; i < provinces.size(); i++) {
                Province province = provinces.get(i);
                provinceNames[i] = province.getName();
            }
            return provinceNames;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
