package org.ucomplex.ucomplex.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

/**
 * Created by Sermilion on 28/11/2015.
 */
public class EventRowItem implements Serializable {

    private int id;
    private EventParams params;
    private int type;
    private String time;
    private int seen;
    private Bitmap eventImageBitmap;
    private String eventText;

    public EventRowItem(int id, EventParams params, int type, String time, int seen, String eventText) {
        this.id = id;
        this.params = params;
        this.type = type;
        this.time = time;
        this.seen = seen;
        this.eventText = eventText;
    }

    public EventRowItem(){
        this.params = new EventParams();
    }

    protected class BitmapDataObject implements Serializable {
        private static final long serialVersionUID = 111696345129311948L;
        public byte[] imageByteArray;
    }

    /** Included for serialization - write this layer to the output stream. */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(this.id);
        out.writeObject(this.params);
        out.writeInt(this.type);
        out.writeObject(this.time);
        out.writeInt(this.seen);
        out.writeObject(this.eventText);

        if(this.eventImageBitmap!=null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            eventImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            BitmapDataObject bitmapDataObject = new BitmapDataObject();
            bitmapDataObject.imageByteArray = stream.toByteArray();
            out.writeObject(bitmapDataObject);
        }
    }

    /** Included for serialization - read this object from the supplied input stream. */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        this.id = in.readInt();
        this.params = (EventParams) in.readObject();
        this.type = in.readInt();
        this.time = (String) in.readObject();
        this.seen = in.readInt();
        this.eventText = (String) in.readObject();

        if(this.eventImageBitmap!=null) {
            BitmapDataObject bitmapDataObject = (BitmapDataObject) in.readObject();
            this.eventImageBitmap = BitmapFactory.decodeByteArray(bitmapDataObject.imageByteArray, 0, bitmapDataObject.imageByteArray.length);
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EventParams getParams() {
        return params;
    }

    public void setParams(EventParams params) {
        this.params = params;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    public Bitmap getEventImageBitmap() {
        return eventImageBitmap;
    }

    public void setEventImageBitmap(Bitmap eventImageBitmap) {
        this.eventImageBitmap = eventImageBitmap;
    }

    public String getEventText() {
        return eventText;
    }

    public void setEventText(String eventText) {
        this.eventText = eventText;
    }
}