package com.example.tomi.namecardnfcapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.R.attr.button;

/**
 * Created by Tomi on 2017. 04. 24..
 */
//This is the custom adapter for the listview
public class ListAdapter extends ArrayAdapter<NameCardListElementResource> {

    private Context context;
    private ArrayList<NameCardListElementResource> cards;
    private LayoutInflater inflater;
    public ListAdapter(Context context, int textViewResourceId, ArrayList<NameCardListElementResource> _cards) {
        super(context, textViewResourceId, _cards);
        this.context = context;
        this.cards = new ArrayList<NameCardListElementResource>();
        this.cards.addAll(_cards);
        inflater = LayoutInflater.from(context);
    }
    //This function refresh the name text of the namecard if it's selected
    public void RemoveAllSelected() {
        for(NameCardListElementResource card : cards){
            String cardName = card.text;
            if( cardName.startsWith("<") && cardName.endsWith(">")){
                cardName = cardName.replace("<", "");
                cardName = cardName.replace(">", "");
                card.text = cardName;
            }
        }
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        /*View v = new View(context);
                v = convertView;*/
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
        }
        TextView text = (TextView) convertView.findViewById(android.R.id.text1);
        final NameCardListElementResource p = getItem(position);
        final String resourceName = context.getResources().getResourceName(p.backGroundId) + "small";
        text.setText(p.text);
        convertView.setBackgroundResource(context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName()));

        return convertView;
    }
}
