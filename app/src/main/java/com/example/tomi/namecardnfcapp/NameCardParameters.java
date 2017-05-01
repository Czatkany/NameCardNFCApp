package com.example.tomi.namecardnfcapp;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by Tomi on 2017. 04. 29..
 */

public class NameCardParameters {
    private Drawable background;
    private ArrayList<NameCardTextValue> texts;

    public NameCardParameters(Drawable _background){
        this.background = _background;
        this.texts = new ArrayList<NameCardTextValue>();
    }
    public void Add(NameCardTextValue text){
        texts.add(text);
    }
    public ArrayList<NameCardTextValue> getTexts(){
        return texts;
    }
    public Drawable getBackground(){
        return background;
    }
}

