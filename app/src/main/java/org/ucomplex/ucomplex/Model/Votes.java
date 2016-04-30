package org.ucomplex.ucomplex.Model;

import java.util.ArrayList;

/**
 * Created by Sermilion on 22/03/16.
 */
public class Votes {

    public static int position;

    private int one;
    private int two;
    private int three;
    private int four;
    private int five;
    private int six;
    private int seven;
    private int eight;
    private int nine;
    private int ten;
    ArrayList<Integer> all = new ArrayList<>();


    public Votes() {
        all.add(one);
        all.add(two);
        all.add(three);
        all.add(four);
        all.add(five);
        all.add(six);
        all.add(seven);
        all.add(eight);
        all.add(nine);
        all.add(ten);
    }

    public ArrayList<Integer> getAll() {
        return all;
    }

    public void setNext(int score, int position){
        all.set(position, score);
    }

    public int next(){
        int score = all.get(position);
        position++;
        if(position==all.size()){
            position = 0;
        }
        return score;

    }

    public int getOne() {
        return one;
    }

    public void setOne(int one) {
        this.one = one;
    }

    public int getTwo() {
        return two;
    }

    public void setTwo(int two) {
        this.two = two;
    }

    public int getThree() {
        return three;
    }

    public void setThree(int three) {
        this.three = three;
    }

    public int getFour() {
        return four;
    }

    public void setFour(int four) {
        this.four = four;
    }

    public int getFive() {
        return five;
    }

    public void setFive(int five) {
        this.five = five;
    }

    public int getSix() {
        return six;
    }

    public void setSix(int six) {
        this.six = six;
    }

    public int getSeven() {
        return seven;
    }

    public void setSeven(int seven) {
        this.seven = seven;
    }

    public int getEight() {
        return eight;
    }

    public void setEight(int eight) {
        this.eight = eight;
    }

    public int getNine() {
        return nine;
    }

    public void setNine(int nine) {
        this.nine = nine;
    }

    public int getTen() {
        return ten;
    }

    public void setTen(int ten) {
        this.ten = ten;
    }
}
