package com.example.tomi.namecardnfcapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
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
import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.DEFAULT;
import static android.graphics.Typeface.DEFAULT_BOLD;
import static android.graphics.Typeface.MONOSPACE;
import static android.graphics.Typeface.SANS_SERIF;
import static android.graphics.Typeface.SERIF;
import static android.text.InputType.TYPE_CLASS_PHONE;
import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS;

/**
 * Created by Tomi on 2017. 05. 01..
 */

public class CardActionsHandler {
    private Context context;

    public CardActionsHandler(Context context){this.context = context;}

    public boolean cardExisit(String cardName){
        for(File file : context.getFilesDir().listFiles()){
            String fileName = file.getName();
            if( fileName.equals(cardName)){
                return true;
            }
        }
        return false;
    }

    public boolean isThereUserCard(){
        for(File file : context.getFilesDir().listFiles()){
            String fileName = file.getName();
            if( fileName.startsWith("<") && fileName.endsWith(">")){
                return true;
            }
        }
        return false;
    }

    public void deselectUserCard(){
        for(File file : context.getFilesDir().listFiles()){
            String fileName = file.getName();
            if( fileName.startsWith("<") && fileName.endsWith(">")){
                fileName = fileName.replace("<", "");
                fileName = fileName.replace(">", "");

                String cardNewName = file.getAbsoluteFile().toString();
                cardNewName = cardNewName.replace(file.getName(), fileName);
                boolean success = file.renameTo(new File(cardNewName));
            }
        }
    }

    public void writeCardToFile(String savedNamecardName, String cardToSave, boolean isUserSelectedCard){
        if(cardExisit(savedNamecardName)){
            deleteCard(savedNamecardName);
        }
        FileOutputStream outputStream;
        if(!isThereUserCard()){
            if(isUserSelectedCard){
                try {
                    outputStream = context.openFileOutput("<"+savedNamecardName+">", Context.MODE_PRIVATE);
                    outputStream.write(cardToSave.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    outputStream = context.openFileOutput(savedNamecardName, Context.MODE_PRIVATE);
                    outputStream.write(cardToSave.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else{
            if(isUserSelectedCard){
                deselectUserCard();
                try {
                    outputStream = context.openFileOutput("<"+savedNamecardName+">", Context.MODE_PRIVATE);
                    outputStream.write(cardToSave.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    outputStream = context.openFileOutput(savedNamecardName, Context.MODE_PRIVATE);
                    outputStream.write(cardToSave.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String readCardToStringFromFile(File file){
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

    public String readCardToStringFromString(String fileName){
        File[] allFiles = context.getFilesDir().listFiles();
        for(File file : allFiles){
            if(file.getName().equals(fileName)){
                return readCardToStringFromFile(file);
            }
        }
        return null;
    }

    public void deleteCard(String cardName){
        for(File file : context.getFilesDir().listFiles()){
            if(cardName.equals(file.getName())){
                file.delete();
                break;
            }
        }
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
            NameCardTextValue actualText = new NameCardTextValue(textParams[0],Float.parseFloat(textParams[1]),Float.parseFloat(textParams[2]),textParams[3],Integer.parseInt(textParams[4]), Float.parseFloat(textParams[5]), textParams[6], Boolean.parseBoolean(textParams[7]));
            actualCard.Add(actualText);
        }
        return actualCard;
    }

    public RelativeLayout generateCardView(RelativeLayout editField, NameCardParameters card){
        LayoutInflater vi = LayoutInflater.from(context);
        editField.setBackground(card.getBackground());
            if(!context.getClass().getSimpleName().equals("EditorActivity")){
                for(NameCardTextValue textField : card.getTexts()) {
                    final EditText edt = new EditText(context);
                    edt.setX(textField.getxCoord());
                    edt.setY(textField.getyCoord());
                    edt.setText(textField.getText());
                    edt.setTextColor(textField.getColor());
                    edt.setTextSize(textField.getSize());
                    String[] fonts = context.getResources().getStringArray(R.array.fonts);
                    for(final String fontName : fonts){
                        Typeface font = Typeface.createFromAsset(
                                context.getAssets(),
                                fontName);
                        if(font.toString().equals(textField.getTextStyle())){
                            edt.setTypeface(font);
                        }
                    }
                    if(textField.getIsUnderlined()){
                        SpannableString spanString = new SpannableString(edt.getText().toString());
                        spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
                        spanString.setSpan(new StyleSpan(edt.getTypeface().getStyle()), 0, spanString.length(), 0);
                        edt.setText(spanString);
                    }
                    edt.setTextIsSelectable(false);
                    edt.setEnabled(false);
                    editField.addView(edt);
                }
            }else {
                //https://android-arsenal.com/details/1/5609 forrás colorpicker
                for(NameCardTextValue textField : card.getTexts()){
                    char[] tag = textField.getTag().toCharArray();
                    final EditText edt = new EditText(context);
                    edt.setTag("textInput" + tag[tag.length - 1]);
                    if(tag[tag.length - 1] == TYPE_CLASS_PHONE){
                        edt.setText("0000");
                        edt.setInputType(tag[tag.length - 1]);
                    }else if(tag[tag.length - 1] == TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS){
                        edt.setText("addEmail");
                        edt.setInputType(tag[tag.length - 1]);
                    }else {
                        edt.setText("addName");
                        edt.setInputType(TYPE_CLASS_TEXT);
                    }
                    //setTypeface
                    String[] fonts = context.getResources().getStringArray(R.array.fonts);
                    for(final String fontName : fonts){
                        Typeface font = Typeface.createFromAsset(
                                context.getAssets(),
                                fontName);
                        if(font.toString().equals(textField.getTextStyle())){
                            edt.setTypeface(font);
                        }
                    }
                    final RelativeLayout inputLayout = editField;
                    edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView tv, int actionId, KeyEvent event) {
                            String editTextValue = (String) edt.getText().toString();
                            if (editTextValue.length() == 0) {
                                inputLayout.removeViewInLayout(edt);
                                return true;
                            } else {

                            }
                            return false;
                        }
                    });
                    edt.setOnTouchListener(new TextEditActions(context, edt){
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return super.onTouch(v, event);
                            }
                        }
                    );
                    edt.setText(textField.getText());
                    edt.setTextColor(textField.getColor());
                    edt.setTextSize(textField.getSize());
                    editField.addView(edt);
                    ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(edt.getLayoutParams());
                    marginParams.setMargins((int) ((textField.getxCoord())), (int) (textField.getyCoord()), 0, 0);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
                    edt.setLayoutParams(layoutParams);
            }
        }
        return editField;
    }

    public String saveSentCard(String card) {
        try {
            String[] cardParams = card.split("/");
            String fileName = cardParams[0];
            if( fileName.startsWith("<") && fileName.endsWith(">")){
                fileName = fileName.replace("<", "");
                fileName = fileName.replace(">", "");
            }
            if(cardParams.length != 1){
                writeCardToFile(fileName, cardParams[1], false);
            }else{
                writeCardToFile(fileName, "", false);
            }
            return fileName;
        } catch (Exception e) {
            Toast.makeText(context, R.string.unsupported, Toast.LENGTH_LONG).show();
            return null;
        }
    }

    public void selectCard(String cardName){
        if(!isThereUserCard()){
            for(File file : context.getFilesDir().listFiles()){
                String fileName = file.getName();
                if( cardName.equals(fileName)){
                    String card = readCardToStringFromFile(file);
                    deleteCard(fileName);
                    writeCardToFile(cardName, card, true);
                    break;
                }
            }
        }else{
            deselectUserCard();
            for(File file : context.getFilesDir().listFiles()){
                String fileName = file.getName();
                if( cardName.equals(fileName)){
                    String card = readCardToStringFromFile(file);
                    deleteCard(fileName);
                    writeCardToFile(cardName, card, true);
                    break;
                }
            }
        }
    }

    public String getMainCard(){
        for(File file : context.getFilesDir().listFiles()){
            String fileName = file.getName();
            if( fileName.startsWith("<") && fileName.endsWith(">")){
                return fileName;
            }
        }
        return null;
    }

    public int getCardBackgroundId(String card){
        String[] cardTexts = card.split(";");
        return Integer.parseInt(cardTexts[0]);
    }
}
