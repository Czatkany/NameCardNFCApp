package com.example.tomi.namecardnfcapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class ListActivity extends AppCompatActivity {
    private Context context = ListActivity.this;
    private ArrayList<NameCardListElementResource> cardResource;
    private ListAdapter adapter;
    private ListView cardList;
    private io.github.yavski.fabspeeddial.FabSpeedDial floatMenuButton;
    final private CardActionsHandler cardHandler = new CardActionsHandler(ListActivity.this);
    private GestureDetectorCompat gestureObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        cardResource = new ArrayList<NameCardListElementResource>();
        File[] allFiles = ListActivity.this.getFilesDir().listFiles();
        //If no cards, returning to the menu activity
        if(allFiles.length == 0){
            finish();
            Toast.makeText(context,R.string.no_cards, Toast.LENGTH_LONG).show();
        }else{
            for(File file : allFiles){
                cardResource.add(cardHandler.generateResourcefromFileString(cardHandler.readCardToStringFromFile(file), file.getName()));
            }
            cardList = (ListView) findViewById(R.id.cardListHolder);
            adapter = new ListAdapter(this, android.R.layout.simple_list_item_1, cardResource);
            cardList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder b = new AlertDialog.Builder(context);
                    b.setTitle(R.string.select_option);
                    Resources res = getResources();
                    String[] types = res.getStringArray(R.array.list_options);
                    b.setItems(types, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            switch(which){
                                case 0:
                                    Intent cardOpen = new Intent(context.getApplicationContext(), CardViewActivity.class);
                                    cardOpen.putExtra("cardNameParam", adapter.getItem(position).text);
                                    context.startActivity(cardOpen);
                                    break;
                                case 1:
                                    Intent cardEdit = new Intent(context.getApplicationContext(), EditorActivity.class);
                                    cardEdit.putExtra("cardNameParam", adapter.getItem(position).text);
                                    context.startActivity(cardEdit);
                                    break;
                                case 2:
                                    Intent sendCard = new Intent(context.getApplicationContext(), NFCActivity.class);
                                    sendCard.putExtra("cardParam", adapter.getItem(position).text + "/" + cardHandler.readCardToStringFromString(adapter.getItem(position).text));
                                    context.startActivity(sendCard);
                                    break;
                                case 3:
                                    SetAsUserCard(position);
                                    break;
                                case 4:
                                    cardHandler.deleteCard(adapter.getItem(position).text);
                                    adapter.remove(cardResource.get(position));
                                    adapter.notifyDataSetChanged();
                                    break;
                            }
                        }
                    });
                    b.show();
                    return false;
                }
            });
            cardList.setAdapter(adapter);
            floatMenuButton = (io.github.yavski.fabspeeddial.FabSpeedDial) findViewById(R.id.position_bottom_end);
            floatMenuButton.setMenuListener(new SimpleMenuListenerAdapter() {
                @Override
                public boolean onMenuItemSelected(MenuItem menuItem) {
                    switch(menuItem.getTitle().toString()){
                        case "Alphabetical":
                            Sort("text");
                            break;
                        case "BackGround":
                            Sort("id");
                            break;
                        case "Back":
                            finish();
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
            cardList.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                    if(scrollState == 0){
                        floatMenuButton.setVisibility(View.VISIBLE);
                    }else if(scrollState == 1){
                        floatMenuButton.setVisibility(View.INVISIBLE);
                    }

                }
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}
            });
        }
    }
    //Returning from an another activity
    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
    //Setting selected card as User Card
    public void SetAsUserCard(final int position){
            for(NameCardListElementResource n : cardResource){
                if(adapter.getItem(position).text.equals(n.text)) {
                    adapter.RemoveAllSelected();
                    cardHandler.selectCard(cardResource.get(position).text);
                    NameCardListElementResource temp = cardResource.get(position);
                    temp.text = "<" + temp.text + ">";
                }
            }
            Sort("text");
        }
    //Sort cards by text or background
    public void Sort(String option){
            if(option.equals("text")){
                adapter.sort(new Comparator<NameCardListElementResource>() {
                    @Override
                    public int compare(NameCardListElementResource o1, NameCardListElementResource o2) {
                        if(o1.text.startsWith("<")){
                            return -1;
                        }else if(o2.text.startsWith("<")){
                            return 1;
                        }else{
                            return o1.text.compareTo(o2.text);
                        }
                    }
                });
            }else if(option.equals("id")){
                adapter.sort(new Comparator<NameCardListElementResource>() {
                    @Override
                    public int compare(NameCardListElementResource o1, NameCardListElementResource o2) {
                        if(o1.backGroundId  < o2.backGroundId){
                            return -1;
                        }else if(o1.backGroundId  > o2.backGroundId){
                            return 1;
                        }else{
                            return 0;
                        }
                    }
                });
            }
            adapter.notifyDataSetChanged();
        }
}
