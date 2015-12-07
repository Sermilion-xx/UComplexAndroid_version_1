package org.ucomplex.ucomplex;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by Sermilion on 04/12/2015.
 */
public class Common {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String httpPost(String urlString, String auth, String ...params) {
        final String UC_BASE_URL = urlString; //"http://you.com.ru/auth?json";

        String dataUrlParameters = "";
        if(params.length>=1){
            dataUrlParameters = params[0];
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
}
