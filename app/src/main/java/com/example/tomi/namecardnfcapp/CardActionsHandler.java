package com.example.tomi.namecardnfcapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.R.id.input;
import static android.text.InputType.TYPE_CLASS_PHONE;

/**
 * Created by Tomi on 2017. 05. 01..
 */

public class CardActionsHandler {
    private Context context;

    public CardActionsHandler(Context context){this.context = context;}

    public void writeCardToFile(String savedNamecardName, String cardToSave){

            FileOutputStream outputStream;

            try {
                outputStream = context.openFileOutput(savedNamecardName, Context.MODE_PRIVATE);
                outputStream.write(cardToSave.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public String readCardToString(File file){
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public NameCardListElementResource generateResourcefromFileString(String card, String cardName){
        String[] cardTexts = card.split(";");
        return new NameCardListElementResource(Integer.parseInt(cardTexts[0]), cardName);
    }

    public NameCardParameters loadCard(String card){
        String[] cardTexts = card.split(";");
        NameCardParameters actualCard= new NameCardParameters(context.getDrawable(Integer.parseInt(cardTexts[0])));
        for(int i = 1; i<cardTexts.length;i++){
            String[] textParams = cardTexts[i].split(",");
            NameCardTextValue actualText = new NameCardTextValue(textParams[0],Float.parseFloat(textParams[1]),Float.parseFloat(textParams[2]),textParams[3],Integer.parseInt(textParams[4]));
            actualCard.Add(actualText);
        }
        return actualCard;
    }

    public RelativeLayout generateCardView(RelativeLayout editField, NameCardParameters card, OnTouchListener touchListener){
        LayoutInflater vi = LayoutInflater.from(context);
        editField.setBackground(card.getBackground());

        for(NameCardTextValue textField : card.getTexts()){

            final RelativeLayout rl = (RelativeLayout) vi.inflate(R.layout.text_input, null);
            char[] tag = textField.getTag().toCharArray();
            if(!context.getClass().getSimpleName().equals("EditorActivity")){
                RelativeLayout holder = (RelativeLayout) rl.findViewById(R.id.textInput);
                holder.setX(textField.getxCoord());
                holder.setY(textField.getyCoord());
                EditText editTextToAdd = (EditText) rl.findViewById(R.id.textView);
                editTextToAdd.setText(textField.getText());
                editTextToAdd.setTextColor(textField.getColor());
                editTextToAdd.setTextIsSelectable(false);
                editTextToAdd.setEnabled(false);
                editField.addView(rl);
            }else{
                final ColorPicker cp = new ColorPicker((Activity) context, 0, 0, 0); //https://android-arsenal.com/details/1/5609 forrás
                EditText edt = (EditText) rl.findViewById(R.id.textView);
                rl.setTag("textInput" + tag[tag.length-1]);
                if(tag[tag.length-1] == TYPE_CLASS_PHONE){
                    edt.setText("0000");
                }
                edt.setInputType(input);
                final RelativeLayout inputLayout = editField;
                edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView tv, int actionId, KeyEvent event) {
                        EditText editTextForValue = (EditText) rl.findViewById(R.id.textView);
                        String editTextValue = (String) editTextForValue.getText().toString();
                        if(editTextValue.length() == 0){
                            inputLayout.removeViewInLayout(rl);
                            return true;
                        }else{

                        }
                        return false;
                    }
                });
                edt.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        final EditText editTextForValue = (EditText) v.findViewById(R.id.textView);
                        cp.show();
                        cp.setCallback(new ColorPickerCallback() {
                            @Override
                            public void onColorChosen(@ColorInt int color) {
                                editTextForValue.setTextColor(color);
                                cp.hide();
                            }
                        });
                        return false;
                    }
                });
                rl.setOnTouchListener(touchListener);
                edt.setText(textField.getText());
                edt.setTextColor(textField.getColor());
                editField.addView(rl);
                ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(rl.getLayoutParams());
                marginParams.setMargins((int) ((textField.getxCoord())), (int) (textField.getyCoord()), 0, 0);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
                rl.setLayoutParams(layoutParams);
            }
        }
        return editField;
    }

    public String saveSentCard(String card){
        try{
            String[] cardParams = card.split("/");
            writeCardToFile(cardParams[0], cardParams[1]);
            return cardParams[0];
        }
        catch(Exception e){
            Toast.makeText(context, R.string.unsupported, Toast.LENGTH_LONG).show();
            return  null;
        }
    }

    public int getCardBackgroundId(String card){
        String[] cardTexts = card.split(";");
        return Integer.parseInt(cardTexts[0]);
    }
}
