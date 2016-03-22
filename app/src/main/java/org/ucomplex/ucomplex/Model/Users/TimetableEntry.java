package org.ucomplex.ucomplex.Model.Users;

/**
 * Created by Sermilion on 22/03/16.
 */
public class TimetableEntry {
    private int id;
    private String name;
    private String description;
    private int cat;
    private int type;
    private String department;
    private int client;

    public TimetableEntry() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCat() {
        return cat;
    }

    public void setCat(int cat) {
        this.cat = cat;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getClient() {
        return client;
    }

    public void setClient(int client) {
        this.client = client;
    }
}
