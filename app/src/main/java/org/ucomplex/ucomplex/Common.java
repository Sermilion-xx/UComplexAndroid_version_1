package org.ucomplex.ucomplex;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Base64;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Sermi lion on 04/12/2015.
 */
public class Common {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String httpPost(String urlString, String auth, HashMap<String, String>... postDataParams) {
        final String UC_BASE_URL = urlString;

        String dataUrlParameters = "";
        try {
            if(postDataParams.length>0){
                    dataUrlParameters = getPostDataString(postDataParams[0]);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final byte[] authBytes = auth.getBytes(StandardCharsets.UTF_8);
        int flags = Base64.NO_WRAP | Base64.URL_SAFE;
        final String encoded = Base64.encodeToString(authBytes, flags);

        try {
            URL url = new URL(UC_BASE_URL);
            MyServices.connection = (HttpURLConnection) url.openConnection();
            MyServices.connection.setRequestMethod("POST");
            MyServices.connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            MyServices.connection.setRequestProperty("Content-Length", "" + Integer.toString(dataUrlParameters.getBytes().length));
            MyServices.connection.setRequestProperty("Content-Language", "en-US");
            MyServices.connection.setRequestProperty("Authorization", "Basic " + encoded);
            MyServices.connection.setUseCaches(false);
            MyServices.connection.setDoInput(true);
            MyServices.connection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(
                    MyServices.connection.getOutputStream());

            wr.writeBytes(dataUrlParameters);
            wr.flush();
            wr.close();
            // Get Response
            InputStream is = MyServices.connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (MyServices.connection != null) {
                MyServices.connection.disconnect();
            }
        }
        return null;
    }

    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String downloadPhoto(String code) {
        final String UC_BASE_URL = "https://ucomplex.org/files/photos/"+code+".jpg";

        String dataUrlParameters = "";

        try {
            URL url = new URL(UC_BASE_URL);
            MyServices.connection = (HttpURLConnection) url.openConnection();
            MyServices.connection.setRequestMethod("POST");
            MyServices.connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            MyServices.connection.setRequestProperty("Content-Length", "" + Integer.toString(dataUrlParameters.getBytes().length));
            MyServices.connection.setRequestProperty("Content-Language", "en-US");
            MyServices.connection.setUseCaches(false);
            MyServices.connection.setDoInput(true);
            MyServices.connection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(
                    MyServices.connection.getOutputStream());
            wr.writeBytes(dataUrlParameters);
            wr.flush();
            wr.close();
            // Get Response
            InputStream is = MyServices.connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (MyServices.connection != null) {
                MyServices.connection.disconnect();
            }
        }
        return null;
    }


    public static String makeDate(String time) {
        String r = "";
        String d = time.split(" ")[0];
        String t = time.split(" ")[1];
        try {
            Locale locale = new Locale("ru", "RU");
            Date date = new SimpleDateFormat("y-M-d H:m:s", locale).parse(time);
            Date today = new Date();
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(today);
            int year1 = cal1.get(Calendar.YEAR);
            int month1 = cal1.get(Calendar.MONTH);
            int day1 = cal1.get(Calendar.DAY_OF_MONTH);

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date);
            int year2 = cal2.get(Calendar.YEAR);
            int month2 = cal2.get(Calendar.MONTH);
            int day2 = cal2.get(Calendar.DAY_OF_MONTH);
            if (day1 == day2) {
                r += "Сегодня";
            } else if (day1 - 1 == day2) {
                r += "Вчера";
            } else {
                r += d;
            }
            r += " в " + t;
        } catch (Exception ex) {

        }
        return r;
    }

    public static Map<String, String> parseJsonKV(JSONObject jObject) throws JSONException {

        Map<String,String> map = new HashMap<>();
        Iterator iter = jObject.keys();
        while(iter.hasNext()){
            String key = (String)iter.next();
            String value = jObject.getString(key);
            map.put(key,value);
        }
        return map;
    }


    public static String readableFileSize(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }


}
