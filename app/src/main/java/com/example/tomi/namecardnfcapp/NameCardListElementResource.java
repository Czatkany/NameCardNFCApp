package com.example.tomi.namecardnfcapp;

/**
 * Created by Tomi on 2017. 04. 30..
 */

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
