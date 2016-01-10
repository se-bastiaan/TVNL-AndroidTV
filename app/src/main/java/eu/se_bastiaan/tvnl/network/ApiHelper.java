package eu.se_bastiaan.tvnl.network;

import com.google.gson.Gson;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

public class ApiHelper {

    public static <T> T getAs(ResponseBody body, Class clazz) {
        try {
            return (T) getAs(body.string(), clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T getAs(String json, Class clazz) {
        Gson gson = new Gson();
        try {
            return (T) gson.fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
