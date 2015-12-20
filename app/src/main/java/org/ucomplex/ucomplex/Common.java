package org.ucomplex.ucomplex;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;

import com.amulyakhare.textdrawable.TextDrawable;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Model.Users.User;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Sermi lion on 04/12/2015.
 */
public class Common {



    public static int getColor(int index) {
        String [] hexColors = {"#f6a6c1","#92d6eb","#4dd9e2","#68d9f0","#c69ad9","#ff83b6","#fda79d","#f8c092",
                "#928fbf","#aa7aad","#e27193","#fb736d","#36add8","#ff6c76","#4dbcbb","#4da8b6",""};
        return Color.parseColor(hexColors[index]);
    }


    private LayoutInflater inflater;
    private DisplayImageOptions options;

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

    public static Bitmap getBitmapFromURL(String code) {
        try {
            final String UC_BASE_URL = "https://ucomplex.org/files/photos/" + code + ".jpg";
            URL url = new URL(UC_BASE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public static Drawable getDrawable(User user){
        final int colorsCount = 16;
        final int number = (user.getId() <= colorsCount) ? user.getId() : user.getId() % colorsCount;
        char firstLetter = user.getName().split("")[1].charAt(0);

        TextDrawable drawable = TextDrawable.builder().beginConfig()
                .width(120)
                .height(120)
                .endConfig()
                .buildRound(String.valueOf(firstLetter), Common.getColor(number));
        return drawable;
    }





//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    public static String downloadPhoto(String code) {
//        final String UC_BASE_URL = "https://ucomplex.org/files/photos/"+code+".jpg";
//
//        String dataUrlParameters = "";
//
//        try {
//            URL url = new URL(UC_BASE_URL);
//            MyServices.connection = (HttpURLConnection) url.openConnection();
//            MyServices.connection.setRequestMethod("POST");
//            MyServices.connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            MyServices.connection.setRequestProperty("Content-Length", "" + Integer.toString(dataUrlParameters.getBytes().length));
//            MyServices.connection.setRequestProperty("Content-Language", "en-US");
//            MyServices.connection.setUseCaches(false);
//            MyServices.connection.setDoInput(true);
//            MyServices.connection.setDoOutput(true);
//
//            DataOutputStream wr = new DataOutputStream(
//                    MyServices.connection.getOutputStream());
//            wr.writeBytes(dataUrlParameters);
//            wr.flush();
//            wr.close();
//            // Get Response
//            InputStream is = MyServices.connection.getInputStream();
//            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
//            String line;
//            StringBuilder response = new StringBuilder();
//            while ((line = rd.readLine()) != null) {
//                response.append(line);
//                response.append('\r');
//            }
//            rd.close();
//            return response.toString();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//
//            if (MyServices.connection != null) {
//                MyServices.connection.disconnect();
//            }
//        }
//        return null;
//    }


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
