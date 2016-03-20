package org.ucomplex.ucomplex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.gson.Gson;
import com.prolificinteractive.materialcalendarview.CalendarDay;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public static int ROLE = -1;
    public static final int FILE_SELECT_CODE = 0;
    public static String folderCode;
    public static int userListChanged = -1;
    public static int GALLERY_INTENT_CALLED = 0;
    public static int GALLERY_KITKAT_INTENT_CALLED = 1;
    public static int newMesg = 0;
    public static boolean newUsr;
    public static ArrayList<Integer> fromMessages = new ArrayList<>();

    public static Typeface getTypeFace(Context context, String typeFace) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/" + typeFace);
        return tf;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    public static int getColor(int index) {
        String[] hexColors = {"#f6a6c1", "#92d6eb", "#4dd9e2", "#68d9f0", "#c69ad9", "#ff83b6", "#fda79d", "#f8c092",
                "#928fbf", "#aa7aad", "#e27193", "#fb736d", "#36add8", "#ff6c76", "#4dbcbb", "#4da8b6", ""};
        return Color.parseColor(hexColors[index]);
    }


    public static void fetchMyNews(final Context context) {
        if(context!=null){
            new AsyncTask<Void, Void, String>() {

                @Override
                protected String doInBackground(Void... params) {
                    String url = "http://you.com.ru/user/my_news?mobile=1";
                    return Common.httpPost(url, Common.getLoginDataFromPref(context));
                }

                @Override
                protected void onPostExecute(String jsonData) {
                    super.onPostExecute(jsonData);

                    if (jsonData != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonData);
                            JSONObject messagesJson = jsonObject.getJSONObject("messages");
                            if (messagesJson != null) {
                                ArrayList<String> fromMessagesStr = Common.getKeys(messagesJson);
                                fromMessagesStr.remove("sum");
                                fromMessages.clear();
                                for (String from : fromMessagesStr) {
                                    fromMessages.add(Integer.valueOf(from));
                                }
                                if (newMesg != messagesJson.getInt("sum")) {
                                    Common.newMesg = messagesJson.getInt("sum");
                                    Intent broadcast = new Intent();
                                    broadcast.setAction("org.ucomplex.newMessageListBroadcast");
                                    broadcast.putExtra("newMessage", Common.newMesg);
                                    context.sendBroadcast(broadcast);
                                }
                                Common.newMesg = messagesJson.getInt("sum");
                                Common.newUsr = jsonObject.getBoolean("friends_req");
                                Intent broadcast = new Intent();
                                broadcast.setAction("org.ucomplex.newMessageMenuBroadcast");
                                broadcast.putExtra("newFriend", Common.newUsr);
                                broadcast.putExtra("newMessage", Common.newMesg);
                                context.sendBroadcast(broadcast);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.execute();
        }
    }


    public static String sendFile(String path, String companion, String msg, String auth) {
        try {
            File file = new File(path);
            HttpPost httpPost = new HttpPost("http://you.com.ru/user/messages/add?mobile=1");
            final byte[] authBytes = auth.getBytes("UTF-8");
            int flags = Base64.NO_WRAP | Base64.URL_SAFE;
            final String encoded = Base64.encodeToString(authBytes, flags);
            httpPost.setHeader("Authorization", "Basic " + encoded);
            MultipartEntityBuilder builder = MultipartEntityBuilder
                    .create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            Charset chars = Charset.forName("UTF-8");
            builder.setCharset(chars);
            FileBody fb = new FileBody(file);
            builder.addPart("file", fb);

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
            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response;
            response = client.execute(httpPost);
            InputStream content = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(content));
            BufferedReader rd = new BufferedReader(new InputStreamReader(content));
            String line;
            StringBuilder responseBuilder = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                responseBuilder.append(line);
                responseBuilder.append('\r');
            }
            rd.close();
            return responseBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String uploadFile(String path, String auth, String... folder) {
        try {
            java.io.File file = new java.io.File(path);
            HttpPost httpPost = new HttpPost("http://you.com.ru/student/my_files/add_files?mobile=1");
            final byte[] authBytes = auth.getBytes("UTF-8");
            int flags = Base64.NO_WRAP | Base64.URL_SAFE;
            final String encoded = Base64.encodeToString(authBytes, flags);
            httpPost.setHeader("Authorization", "Basic " + encoded);
            MultipartEntityBuilder builder = MultipartEntityBuilder
                    .create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            Charset chars = Charset.forName("UTF-8");
            builder.setCharset(chars);
            FileBody fb = new FileBody(file);
            builder.addPart("file", fb);
            if (folder.length > 0) {
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


    public static ArrayList getFileDataFromJson(String jsonData, Activity contex) {
        ArrayList<org.ucomplex.ucomplex.Model.StudyStructure.File> files = new ArrayList<>();
        JSONObject fileJson;
        if (jsonData != null) {
            try {
                fileJson = new JSONObject(jsonData);
                JSONArray filesArray = fileJson.getJSONArray("files");

                for (int i = 0; i < filesArray.length(); i++) {
                    org.ucomplex.ucomplex.Model.StudyStructure.File file = new org.ucomplex.ucomplex.Model.StudyStructure.File();
                    JSONObject jsonFile = filesArray.getJSONObject(i);
                    if (!jsonFile.isNull("size")) {
                        file.setSize(jsonFile.getInt("size"));
                    }
                    if (jsonFile.has("time")) {
                        file.setTime(jsonFile.getString("time"));
                    } else {
                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("ru")).format(Calendar.getInstance().getTime());
                        file.setTime(timeStamp);
                    }
                    file.setAddress(jsonFile.getString("address"));
                    file.setName(jsonFile.getString("name"));
                    file.setType(jsonFile.getString("type"));
                    if (jsonFile.has("check_time")) {
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
        }

        return null;
    }

    @SafeVarargs
    @Nullable
    public static String httpPost(String urlString, String auth, HashMap<String, String>... postDataParams) {
        String dataUrlParameters = "";
        try {
            if (postDataParams.length > 0) {
                dataUrlParameters = getPostDataString(postDataParams[0]);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] authBytes = null;

        try {
            authBytes = auth.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        int flags = Base64.NO_WRAP | Base64.URL_SAFE;
        final String encoded = Base64.encodeToString(authBytes, flags);
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(dataUrlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setRequestProperty("Authorization", "Basic " + encoded);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
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
        for (Map.Entry<String, String> entry : params.entrySet()) {
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

    public static Bitmap getBitmapFromURL(String code, int type) {
        try {
            String UC_BASE_URL;
            if(type==0){
                UC_BASE_URL = "https://ucomplex.org/files/photos/" + code + ".jpg";
            }else{
                UC_BASE_URL = code;
            }
            URL url = new URL(UC_BASE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Drawable getDrawable(User user) {
        final int colorsCount = 16;
        final int number = (user.getId() <= colorsCount) ? user.getId() : user.getId() % colorsCount;
        char firstLetter = user.getName().split("")[1].charAt(0);

        return TextDrawable.builder().beginConfig()
                .width(120)
                .height(120)
                .endConfig()
                .buildRound(String.valueOf(firstLetter), Common.getColor(number));
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


    public static String makeDate(String time, boolean... justDate) {
        String r = "";
        String yyyyMMdd = time.split(" ")[0];
        String hhMMss = null;
        if (time.length() == 2) {
            hhMMss = time.split(" ")[1];
        }
        try {
            Locale locale = new Locale("ru", "RU");
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale).parse(time);
            Date today = new Date();

            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(today);
            int day1 = cal1.get(Calendar.DAY_OF_MONTH);
            int month1 = cal1.get(Calendar.MONTH);
            int year1 = cal1.get(Calendar.YEAR);

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date);
            int month2 = cal2.get(Calendar.MONTH);
            int year2 = cal2.get(Calendar.YEAR);
            int day2 = cal2.get(Calendar.DAY_OF_MONTH);

            if (day1 == day2 && month1 == month2 && year1 == year2) {
                r += "Сегодня";
            } else if (day1 - 1 == day2 && month1 == month2 && year1 == year2) {
                r += "Вчера";
            } else {
                String[] tempYyMMdd = yyyyMMdd.split("-");
                String tempMonth = tempYyMMdd[1];
                String month = "";
                if (tempMonth.equals("01")) {
                    month = "января";
                }
                if (tempMonth.equals("02")) {
                    month = "февряля";
                }
                if (tempMonth.equals("03")) {
                    month = "марта";
                }
                if (tempMonth.equals("04")) {
                    month = "апреля";
                }
                if (tempMonth.equals("05")) {
                    month = "мая";
                }
                if (tempMonth.equals("06")) {
                    month = "июня";
                }
                if (tempMonth.equals("07")) {
                    month = "июля";
                }
                if (tempMonth.equals("08")) {
                    month = "августа";
                }
                if (tempMonth.equals("09")) {
                    month = "сентября";
                }
                if (tempMonth.equals("10")) {
                    month = "октября";
                }
                if (tempMonth.equals("11")) {
                    month = "ноября";
                }
                if (tempMonth.equals("12")) {
                    month = "декабря";
                }
                r += tempYyMMdd[2] + " " + month + " " + tempYyMMdd[0] + " г.";
            }
            if (justDate != null) {
                if (hhMMss != null) {
                    r += " в " + hhMMss.substring(0, 5);
                } else {

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (r.equals("")) {
            r = yyyyMMdd;
        }
        return r;
    }

    public static Map<String, String> parseJsonKV(JSONObject jObject) throws JSONException {
        Map<String, String> map = new HashMap<>();
        Iterator iter = jObject.keys();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            String value = jObject.getString(key);
            map.put(key, value);
        }
        return map;
    }


    public static String readableFileSize(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static ArrayList<String> getKeys(JSONObject object) throws JSONException {
        ArrayList<String> keys = new ArrayList<>();
        Iterator iter = object.keys();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            keys.add(key);
        }
        return keys;
    }

    public static boolean isDownloadManagerAvailable() {

        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }


    public static HttpURLConnection connection;
    public static String X_UVERSION;
    public static String messageCompanionName = "-";

    public static void setRoleToPref(Context mContext, int role) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
        editor.putInt("userRole", role);
        editor.apply();
    }

    public static int getRoleFromPref(Context mContext) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        int role = pref.getInt("userRole", -1);
        return role;
    }

    public static String getLoginDataFromPref(Context mContext) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = pref.getString("loggedUser", "");
        User obj = gson.fromJson(json, User.class);
        if(obj!=null){
            return obj.getLogin() + ":" + obj.getPass() + ":" + obj.getId();
        }else{
            return "";
        }
    }

    public static User getUserDataFromPref(Context mContext) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = pref.getString("loggedUser", "");
        return gson.fromJson(json, User.class);
    }

    public static void setUserDataToPref(Context mContext, User user) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("loggedUser", json);
        editor.apply();
    }

    public static Bitmap decodePhotoPref(Context context, String typeStr) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String encoded = pref.getString(typeStr, "");
        if (encoded.length() > 0) {
            int flags = Base64.NO_WRAP | Base64.URL_SAFE;
            byte[] imageAsBytes = Base64.decode(encoded.getBytes(), flags);
            return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        }
        return null;
    }

    public static void encodePhotoPref(Context context, Bitmap photoBitmap, String typeStr) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encoded = Base64.encodeToString(b, Base64.URL_SAFE);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        editor.putString(typeStr, encoded);
        editor.apply();
    }

    public static void deleteFromPref(Context context, String typeStr) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.remove(typeStr);
        editor.apply();
    }

    public static boolean hasKeyPref(Context context, String typeStr) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = preferences.getString("typeStr", null);
        return value != null;
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index;
                if (cursor != null) {
                    column_index = cursor.getColumnIndexOrThrow("_data");
                    if (cursor.moveToFirst()) {
                        cursor.close();
                        return cursor.getString(column_index);
                    }
                }
            } catch (Exception ignored) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getStringUserType(Context context, int type) {
        String typeStr = null;
        if (type == 0) {
            typeStr = context.getResources().getString(R.string.sotrudnik);
        }
        if (type == 1) {
            typeStr = context.getResources().getString(R.string.administrator);
        }
        if (type == 2) {
            typeStr = context.getResources().getString(R.string.sub_administrator);
        } else if (type == 3) {
            typeStr = context.getResources().getString(R.string.prepodvatel);
        } else if (type == 4) {
            typeStr = context.getResources().getString(R.string.student);
        } else if (type == 5) {
            typeStr = context.getResources().getString(R.string.metodist_po_raspisaniyu);
        } else if (type == 6) {
            typeStr = context.getResources().getString(R.string.metodist_ko);
        } else if (type == 7) {
            typeStr = context.getResources().getString(R.string.bibliotekar);
        } else if (type == 8) {
            typeStr = context.getResources().getString(R.string.tehsekretar);
        } else if (type == 9) {
            typeStr = context.getResources().getString(R.string.abiturient);
        } else if (type == 10) {
            typeStr = context.getResources().getString(R.string.uchebny_otdel);
        } else if (type == 11) {
            typeStr = context.getResources().getString(R.string.rukovoditel);
        } else if (type == 12) {
            typeStr = context.getResources().getString(R.string.monitoring);
        } else if (type == 13) {
            typeStr = context.getResources().getString(R.string.dekan);
        } else if (type == 14) {
            typeStr = context.getResources().getString(R.string.otdel_kadrov);
        }
        return typeStr;
    }

}
