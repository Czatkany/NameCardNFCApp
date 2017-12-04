package com.example.tomi.namecardnfcapp;

/**
 * Created by Tomi on 2017. 04. 29..
 */
//This class contains the elements of the textfields on the namecard
public class NameCardTextValue{
    private String tag;
    private float xCoord;
    private float yCoord;
    private String text;
    private int color;
    private float size;
    private String textStyle;
    private boolean isUnderlined;
    public NameCardTextValue(String _tag, float _xCoord, float _yCoord, String _text, int _color, float _size, String _textStyle, boolean _isUnderlined){
        this.tag = _tag;
        this.xCoord = _xCoord;
        this.yCoord = _yCoord;
        this.text = _text;
        this.color = _color;
        this.size = _size;
        this.textStyle = _textStyle;
        this.isUnderlined = _isUnderlined;
    }
    //The tag is needed to identify the input type, and the text view
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

    public int getColor() {return color; }

    public float getSize(){
        return size;
    }

    public String getTextStyle(){
        return textStyle;
    }

    public boolean getIsUnderlined(){
        return isUnderlined;
    }

    public String Value(){
        return tag + "," + xCoord + "," + yCoord + "," + text + "," + color + "," + size + "," + textStyle + "," + isUnderlined + ";";
    }
}
