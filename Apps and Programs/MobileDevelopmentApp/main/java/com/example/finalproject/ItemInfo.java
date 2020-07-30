package com.example.finalproject;

import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ItemInfo extends AppCompatActivity {

    String myFormat = "MM-dd-yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    Bitmap bitmap; //bitmap of picture of item
    ImageView itemImage; //thumbnail to hold picture
    EditText itemName, itemExpireDate, itemPurchaseDate;
    Drawable d;

    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        itemImage = findViewById(R.id.fridgeItemPic);
        itemName = findViewById(R.id.itemName);
        itemExpireDate = findViewById(R.id.expirationDate);
        itemPurchaseDate = findViewById(R.id.datePurchased);

        updateLabel(itemExpireDate);
        updateLabel(itemPurchaseDate);

        final DatePickerDialog.OnDateSetListener expireDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel(itemExpireDate);
            }
        };

        itemExpireDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ItemInfo.this, expireDate, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final DatePickerDialog.OnDateSetListener purchaseDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel(itemPurchaseDate);
            }
        };

        itemPurchaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ItemInfo.this, purchaseDate, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //reconstruct bitmap image from phone storage
        try {
        bitmap = BitmapFactory.decodeStream(this.openFileInput("myImage"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        d = new BitmapDrawable(getResources(), bitmap);

        itemImage.setImageDrawable(d);
        //itemImage.setImageBitmap(bitmap); //put image in image view (scaling issues?)

    }

    private void updateLabel(EditText editText) {
        editText.setText(sdf.format(calendar.getTime()));
    }

    //add item to database or list
    public void addItem(View view) {
        Item item = new Item();

        item.itemImage = d;
        item.name = itemName.getText().toString();
        item.purchaseDate = itemPurchaseDate.getText().toString();
        item.expirationDate = itemExpireDate.getText().toString();

        MainActivity.listOfItems.add(item);

        setResult(RESULT_OK);
        finish();
    }

}
