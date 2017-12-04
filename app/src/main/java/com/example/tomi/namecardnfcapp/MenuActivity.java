package com.example.tomi.namecardnfcapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.internal.NavigationMenu;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class MenuActivity extends AppCompatActivity {

    private io.github.yavski.fabspeeddial.FabSpeedDial floatMenuButton;
    private Context context = MenuActivity.this;
    private CardActionsHandler cardActionHandler = new CardActionsHandler(context);
    private GestureDetectorCompat gestureObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        floatMenuButton = (io.github.yavski.fabspeeddial.FabSpeedDial) findViewById(R.id.position_bottom_end);
        floatMenuButton.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch(menuItem.getTitle().toString()){
                    case "SavedCards":
                        Intent listScreen = new Intent(getApplicationContext(), ListActivity.class);
                        startActivity(listScreen);
                        break;
                    case "CardEditor":
                        Intent editorScreen = new Intent(getApplicationContext(), EditorActivity.class);
                        startActivity(editorScreen);
                        break;
                    case "GetCards":
                        Intent getCard = new Intent(getApplicationContext(), NFCActivity.class);
                        getCard.putExtra("cardParam","");
                        startActivity(getCard);
                        break;
                    case "DeleteCards":
                        File[] allFiles = MenuActivity.this.getFilesDir().listFiles();
                        for(File file : allFiles){
                            file.delete();
                        }
                        break;
                    case "Quit":
                        finish();
                        break;
                }
                return false;
            }
        });

        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

        if(!cardActionHandler.isThereUserCard()){
            OpenUserCardSetter();
        }
    }

    public boolean onTouchEvent(MotionEvent event){
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    //This class stands for the swipe gesture
    class LearnGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if(e1.getX() > e2.getX()){

                Intent listScreen = new Intent(getApplicationContext(), ListActivity.class);
                //finish();
                startActivity(listScreen);

            }else if(e1.getX() < e2.getX()){

            }
            return true;
        }
    }
    //This is a popup window, which come up if the user doesn't selected any cards
    private void OpenUserCardSetter(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater vi = LayoutInflater.from(context);
        final View v = vi.inflate(R.layout.set_namecard, null);
        Button createNew = (Button) v.findViewById(R.id.create_new);
        createNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editorScreen = new Intent(getApplicationContext(), EditorActivity.class);
                startActivity(editorScreen);
            }
        });
        Button fromList = (Button) v.findViewById(R.id.select_from_list);
        fromList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listScreen = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(listScreen);
            }
        });
        builder.setTitle(R.string.set_namecard);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setView(v);
        builder.show();
    }
}
