package com.example.tomi.namecardnfcapp;

/**
 * Created by Tomi on 2017. 04. 29..
 */

public class NameCardTextValue{
    private String tag;
    private float xCoord;
    private float yCoord;
    private String text;
    private int color;
    public NameCardTextValue(String _tag, float _xCoord, float _yCoord, String _text, int _color){
        this.tag = _tag;
        this.xCoord = _xCoord;
        this.yCoord = _yCoord;
        this.text = _text;
        this.color = _color;
    }

    public String getTag(){
        return tag;
    }

    public float getxCoord(){
        return xCoord;
    }

    public float getyCoord(){
        return yCoord;
    }

    public String getText(){
        return text;
    }

    public int getColor(){
        return color;
    }

    public String Value(){
        return tag + "," + xCoord + "," + yCoord + "," + text + "," + color + ";";
    }
}
