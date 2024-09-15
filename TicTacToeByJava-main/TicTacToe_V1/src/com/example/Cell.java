package com.example;

public class Cell {
    private int x;
    private int y;
    private int width;
    private int height;
    private String value;

    public static final String X_VALUE = "x";
    public static final String O_VALUE = "o";
    public static final String EMPTY_VALUE = "";

    public Cell() {this.value = "";}

    public Cell(int x, int y, int width, int height, String value) {
        this();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.value = value;
    }

    public int getX() {return x;}
    public int getY() {return y;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public String getValue() {return value;}

    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    public void setWeight(int width) {this.width = width;}
    public void setHeight(int height) {this.height = height;}
    public void setValue(String value) {this.value = value;}
}
