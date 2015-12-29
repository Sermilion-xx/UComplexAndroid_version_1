package org.ucomplex.ucomplex.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.entity.mime.content.ByteArrayBody;
import org.ucomplex.ucomplex.Activities.Tasks.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Activities.Tasks.UploadPhotoTask;
import org.ucomplex.ucomplex.MyServices;
import org.ucomplex.ucomplex.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class SettingsActivity extends AppCompatActivity implements OnTaskCompleteListener {

    public static final int GET_FROM_GALLERY = 100;
    private Bitmap profileBitmap;
    ImageView photoImageView;
    SettingsActivity context = this;
    ByteArrayBody contentBody;
    boolean chose = false;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Настройки");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photoImageView = (ImageView) findViewById(R.id.settings_photo);
            Bitmap photoBitmap = MyServices.decodePhotoPref(context, "tempProfilePhoto");
            photoImageView.setImageBitmap(photoBitmap);

        photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GET_FROM_GALLERY);
                chose = true;
            }
        });

        Button buttonChangePhoto = (Button) findViewById(R.id.settings_photo_change_button);
        buttonChangePhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(chose){
                    UploadPhotoTask uploadPhotoTask = new UploadPhotoTask(context, context);
                    uploadPhotoTask.setupTask(contentBody);
                    chose = false;
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            try {
                profileBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                profileBitmap = getCroppedBitmap(profileBitmap, 604);
                photoImageView.setImageBitmap(profileBitmap);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                profileBitmap.compress(Bitmap.CompressFormat.JPEG, 60, bos);
                contentBody = new ByteArrayBody(bos.toByteArray(), "filename");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskComplete(AsyncTask task, Object... o) {
        if (task.isCancelled()) {
            // Report about cancel
            Toast.makeText(this, "Загрузка была отменена", Toast.LENGTH_LONG)
                    .show();
        } else {
            try {
                if((Integer)task.get()==200){
                    Toast.makeText(this, "Ваше фото отправленно на модерацию", Toast.LENGTH_LONG)
                            .show();
                    MyServices.encodePhotoPref(context, profileBitmap, "tempProfilePhoto");
                }else{
                    Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_LONG)
                            .show();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

}
