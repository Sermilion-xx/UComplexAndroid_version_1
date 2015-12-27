package org.ucomplex.ucomplex.Model.Library;

/**
 * Created by Sermilion on 27/12/2015.
 */
public class CollectionItem {
    private int id;
    private String name;
    private int price;
    private int type;

    public CollectionItem(int id, String name, int price, int type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public CollectionItem(){

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
