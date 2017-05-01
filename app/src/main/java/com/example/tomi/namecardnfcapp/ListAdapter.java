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

import java.util.ArrayList;

/**
 * Created by Tomi on 2017. 04. 24..
 */

public class ListAdapter extends ArrayAdapter<NameCardListElementResource> {

    private Context context;
    private ArrayList<NameCardListElementResource> cards;
    public ListAdapter(Context context, int textViewResourceId, ArrayList<NameCardListElementResource> _cards) {
        super(context, textViewResourceId, _cards);
        this.context = context;
        this.cards = new ArrayList<NameCardListElementResource>();
        this.cards.addAll(_cards);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_resource, null);
        }

        final NameCardListElementResource p = getItem(position);
        final String resourceName = context.getResources().getResourceName(p.backGroundId) + "small";
        if (p != null) {
            RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.nameCardListElement);
            Button button = (Button) v.findViewById(R.id.openCard);
            TextView text = (TextView) v.findViewById(R.id.cardName);

            if (rl != null) {
                rl.setBackgroundResource(context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName()));
            }
            if (button != null) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent cardView = new Intent(context.getApplicationContext(), CardViewActivity.class);
                        cardView.putExtra("cardNameParam", p.text);
                        context.startActivity(cardView);
                    }
                });
            }
            if(text != null){
                text.setText(p.text);
            }
        }

        return v;
    }
}
