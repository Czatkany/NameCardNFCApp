package com.example.tomi.namecardnfcapp;

import android.support.annotation.NonNull;

/**
 * Created by Tomi on 2017. 04. 30..
 */
//This is the collection of the namecards for the listview
public class NameCardListElementResource {
    public int backGroundId;
    public String text;
    public NameCardListElementResource(int _backGroundId, String _text){
        this.backGroundId = _backGroundId;
        this.text = _text;
    }

    public NameCardListElementResource getItem(){
        return this;
    }
}
