package org.ucomplex.ucomplex.Model;

/**
 * Created by Sermilion on 31/12/2015.
 */
public class Message {
    private int id;
    private int from;
    private String message;
    private String time;
    private int status;
    private String name;
    private String fileType;

    public Message() {
    }

    public String getFileType() {
        return fileType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
