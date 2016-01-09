package org.ucomplex.ucomplex;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucomplex.ucomplex.Model.Users.User;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by Sermi lion on 04/12/2015.
 */
public class Common {

    public static final int FILE_SELECT_CODE = 0;
    public static String folderCode;

    public static int getColor(int index) {
        String [] hexColors = {"#f6a6c1","#92d6eb","#4dd9e2","#68d9f0","#c69ad9","#ff83b6","#fda79d","#f8c092",
                "#928fbf","#aa7aad","#e27193","#fb736d","#36add8","#ff6c76","#4dbcbb","#4da8b6",""};
        return Color.parseColor(hexColors[index]);
    }


    private LayoutInflater inflater;
    private DisplayImageOptions options;


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void sendFile(String path, String companion, String msg, String auth){
        try{

            File file = new File(path);
            HttpPost httpPost = new HttpPost("http://you.com.ru/user/messages/add/");
            final byte[] authBytes = auth.getBytes(StandardCharsets.UTF_8);
            int flags = Base64.NO_WRAP | Base64.URL_SAFE;
            final String encoded = Base64.encodeToString(authBytes, flags);
            httpPost.setHeader("Authorization", "Basic " + encoded);
            MultipartEntityBuilder builder = MultipartEntityBuilder
                    .create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            Charset chars = Charset.forName("UTF-8");
            builder.setCharset(chars);
            if (file != null) {
                FileBody fb = new FileBody(file);
                builder.addPart("file", fb);
            }

            builder.addTextBody("companion", companion,
                    ContentType.TEXT_PLAIN);
            try {
                builder.addPart("msg", new StringBody(msg, "text/plain",
                        Charset.forName("UTF-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            builder.setCharset(chars);

            final HttpEntity yourEntity = builder.build();

            httpPost.setEntity(yourEntity);

            StringBuilder builderString = new StringBuilder();
            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response = null;
            response = client.execute(httpPost);
            InputStream content = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(content));
            String line;
            while ((line = reader.readLine()) != null) {
                builderString.append(line);
            }
            String message = builderString.toString();
            response.getEntity().consumeContent();
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String uploadFile(String path, String auth, String ... folder){
        try{

            File file = new File(path);
            HttpPost httpPost = new HttpPost("http://you.com.ru/student/my_files/add_files?mobile=1");
            final byte[] authBytes = auth.getBytes(StandardCharsets.UTF_8);
            int flags = Base64.NO_WRAP | Base64.URL_SAFE;
            final String encoded = Base64.encodeToString(authBytes, flags);
            httpPost.setHeader("Authorization", "Basic " + encoded);
            MultipartEntityBuilder builder = MultipartEntityBuilder
                    .create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            Charset chars = Charset.forName("UTF-8");
            builder.setCharset(chars);
            if (file != null) {
                FileBody fb = new FileBody(file);
                builder.addPart("file", fb);
            }
            if(folder.length>0){
                builder.addTextBody("folder", folder[0],
                        ContentType.TEXT_PLAIN);
            }
            builder.setCharset(chars);

            final HttpEntity yourEntity = builder.build();

            httpPost.setEntity(yourEntity);

            StringBuilder builderString = new StringBuilder();
            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response = null;
            response = client.execute(httpPost);
            InputStream content = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(content));
            String line;
            while ((line = reader.readLine()) != null) {
                builderString.append(line);
            }
            String message = builderString.toString();
            response.getEntity().consumeContent();
            return message;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static ArrayList getFileDataFromJson(String jsonData, Activity contex){
        ArrayList<org.ucomplex.ucomplex.Model.StudyStructure.File> files = new ArrayList<>();
        JSONObject fileJson = null;

        try {
            fileJson = new JSONObject(jsonData);
            JSONArray filesArray = fileJson.getJSONArray("files");

            for(int i=0;i<filesArray.length();i++){
                org.ucomplex.ucomplex.Model.StudyStructure.File file = new org.ucomplex.ucomplex.Model.StudyStructure.File();
                JSONObject jsonFile = filesArray.getJSONObject(i);
                if(!jsonFile.isNull("size")){
                    file.setSize(jsonFile.getInt("size"));
                }
                if(jsonFile.has("time")){
                    file.setTime(jsonFile.getString("time"));
                }else{
                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("ru")).format(Calendar.getInstance().getTime());
                    file.setTime(timeStamp);
                }
                file.setAddress(jsonFile.getString("address"));
                file.setName(jsonFile.getString("name"));
                file.setType(jsonFile.getString("type"));
                if(jsonFile.has("check_time")){
                    file.setCheckTime(jsonFile.getString("check_time"));
                }
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contex);
                Gson gson = new Gson();
                String json = pref.getString("loggedUser", "");
                User obj = gson.fromJson(json, User.class);
                file.setOwner(obj);
                files.add(file);
            }
            return files;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] fileToByte(String filePath){
        File file = new File(filePath);
        try {
            FileInputStream fis = new FileInputStream(file);
            //System.out.println(file.exists() + "!!");
            //InputStream in = resource.openStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            try {
                for (int readNum; (readNum = fis.read(buf)) != -1;) {
                    bos.write(buf, 0, readNum); //no doubt here is 0
                    //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                    System.out.println("read " + readNum + " bytes,");
                }
            } catch (IOException ex) {}

            return bos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @SafeVarargs
    @Nullable
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String httpPost(String urlString, String auth, HashMap<String, String>... postDataParams) {
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
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(dataUrlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setRequestProperty("Authorization", "Basic " + encoded);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(dataUrlParameters);
            wr.flush();
            wr.close();
            InputStream is = connection.getInputStream();
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
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
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
            e.printStackTrace();
            return null;
        }
    }

    public static void getPdfFromURL(String address, String id, String type, Activity context) {
            PackageManager packageManager = context.getPackageManager();
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setType("application/pdf");
            List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            final String UC_BASE_URL = "https://chgu.org/files/users/" +id+"/"+ address +"."+ type;
            if (list.size() > 0) {
                // Happy days a PDF reader exists
                context.startActivity(intent);
            } else {
                // No PDF reader, ask the user to download one first
                // or just open it in their browser like this
                intent = new Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse(UC_BASE_URL));
                context.startActivity(intent);
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


    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        if (bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            sbmp = bmp;
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        Paint paint1 = new Paint();
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setAntiAlias(true);
        paint1.setARGB(255, 237, 238, 240);
        paint1.setStrokeWidth(2);

        canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f,
                sbmp.getHeight() / 2 + 0.7f, sbmp.getWidth() / 2.2f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);
        canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f,
                sbmp.getHeight() / 2 + 0.7f, sbmp.getWidth() / 2.2f, paint1);
        return output;
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

    public static ArrayList<String> getKeys(JSONObject object) throws JSONException {
        ArrayList<String> keys = new ArrayList<>();
        Iterator iter = object.keys();
        while(iter.hasNext()){
            String key = (String)iter.next();
            keys.add(key);
        }
        return keys;
    }

    public static boolean isDownloadManagerAvailable(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }


    public static int logingRole;
    public static HttpURLConnection connection;
    public static String lang_version;
    public static String X_UVERSION;
    public static int usersDataChanged=-1;
    public static String messageCompanionName = "-";

    public static String getLoginDataFromPref(Context mContext){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = pref.getString("loggedUser", "");
        User obj = gson.fromJson(json, User.class);
        return obj.getLogin()+":"+obj.getPass()+":"+obj.getRoles().get(0).getId();
    }

    public static User getUserDataFromPref(Context mContext){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = pref.getString("loggedUser", "");
        User obj = gson.fromJson(json, User.class);
        return obj;
    }

    public static void setUserDataToPref(Context mContext, User user){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("loggedUser", json);
        editor.apply();
    }

    public static Bitmap decodePhotoPref(Context context, String typeStr){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String encoded = pref.getString(typeStr, "");
        if(encoded.length()>0){
            int flags = Base64.NO_WRAP | Base64.URL_SAFE;
            byte[] imageAsBytes = Base64.decode(encoded.getBytes(), flags);
            Bitmap photoBitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            return photoBitmap;
        }
        return null;
    }

    public static void encodePhotoPref(Context context, Bitmap photoBitmap, String typeStr){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encoded = Base64.encodeToString(b, Base64.URL_SAFE);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        editor.putString(typeStr, encoded);
        editor.apply();
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }





//    public static String httpPost(String urlString, String auth, HashMap<String, String>... postDataParams) {
//        String lineEnd = "\r\n";
//        String twoHyphens = "--";
//        String boundary =  "*****";
//        String dataUrlParameters = "";
//        String pathToOurFile = "";
//        FileInputStream fileInputStream = null;
//        boolean sendFile = false;
//        try {
//            if(postDataParams.length>0){
//                if(postDataParams[0].containsKey("file")){
//                    sendFile = true;
//                    pathToOurFile = postDataParams[0].get("file");
//                    fileInputStream = new FileInputStream(new File(pathToOurFile) );
//                }
//                dataUrlParameters = getPostDataString(postDataParams[0]);
//            }
//        } catch (UnsupportedEncodingException | FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        final byte[] authBytes = auth.getBytes(StandardCharsets.UTF_8);
//        int flags = Base64.NO_WRAP | Base64.URL_SAFE;
//        final String encoded = Base64.encodeToString(authBytes, flags);
//        try {
//            URL url = new URL(urlString);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
//            connection.setDoInput(true);
//            connection.setDoOutput(true);
//            connection.setUseCaches(false);
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            if(sendFile){
//
//            }else{
//                connection.setRequestProperty("Content-Length", "" + Integer.toString(dataUrlParameters.getBytes().length));
//            }
//
//            connection.setRequestProperty("Content-Language", "en-US");
//            connection.setRequestProperty("Authorization", "Basic " + encoded);
//            connection.setUseCaches(false);
//            connection.setDoInput(true);
//            connection.setDoOutput(true);
//
//            DataOutputStream dataOutputStream = new DataOutputStream(
//                    connection.getOutputStream());
//            dataOutputStream.writeBytes(dataUrlParameters);
//
//            if (sendFile) {
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile + "\"" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);
//
//                int bytesRead, bytesAvailable, bufferSize;
//                byte[] buffer;
//                int maxBufferSize = 1024 * 1024;
//
//                assert fileInputStream != null;
//                bytesAvailable = fileInputStream.available();
//                bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                buffer = new byte[bufferSize];
//                // Read file
//                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                while (bytesRead > 0) {
//                    dataOutputStream.write(buffer, 0, bufferSize);
//                    bytesAvailable = fileInputStream.available();
//                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//                }
//
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//            }
//
//            dataOutputStream.flush();
//            dataOutputStream.close();
//
//            InputStream is = connection.getInputStream();
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
//            return null;
//        } finally {
//            if (connection != null) {
//                connection.disconnect();
//            }
//        }
//    }

}
