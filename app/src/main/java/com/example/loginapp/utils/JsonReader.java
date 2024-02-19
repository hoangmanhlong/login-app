package com.example.loginapp.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JsonReader {

    private static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static JSONArray loadJSONArrayFromAsset(Context context, String fileName) {
        try {
            return new JSONArray(loadJSONFromAsset(context, fileName));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject loadJSONObjectFromAsset(Context context, String fileName) {
        try {
            return new JSONObject(loadJSONFromAsset(context, fileName));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

