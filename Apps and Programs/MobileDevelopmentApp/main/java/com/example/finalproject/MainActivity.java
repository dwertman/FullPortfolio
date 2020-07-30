package com.example.finalproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private String LOG = "Check Me Out";

    String myFormat = "MM-dd-yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    private static final int CAMERA_REQUEST = 100; //error checking
    private static final int ITEM_CREATION = 99; //error checking
    private static final int STORAGE_PERMISSION_CODE = 1; //used for permissions

    private ImageButton btnCamera;
    private String str;

    Resources res;
    private Calendar today;

    private LinearLayout linearLayout;

    static List<Item> listOfItems = new ArrayList<Item>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //see if permissions are granted and ask user for permission if not
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE);
        }

        res = getResources();

        today = Calendar.getInstance(TimeZone.getDefault());

        linearLayout = findViewById(R.id.linear_layout);

        final LayoutInflater layoutInflater = LayoutInflater.from(this);

        if (listOfItems.size() != 0){
            for (int i = 0; i < listOfItems.size(); i++) {
                View itemView = layoutInflater.inflate(R.layout.item_layout, linearLayout, false);
                ImageView itemImage = itemView.findViewById(R.id.itemImage);
                TextView itemName = itemView.findViewById(R.id.itemName);
                TextView itemPurchaseDate = itemView.findViewById(R.id.itemBuyDate);
                TextView daysToExpiration = itemView.findViewById(R.id.itemExpiresDays);

                itemImage.setImageDrawable(listOfItems.get(i).itemImage);
                str = listOfItems.get(i).name;
                itemName.setText(str);
                str = "Purchased on\n" + listOfItems.get(i).purchaseDate;
                itemPurchaseDate.setText(str);
                str = getExpirationDays(listOfItems.get(i).expirationDate);
                daysToExpiration.setText(str);

                colorizeDays(str, daysToExpiration);

                linearLayout.addView(itemView);
            }
        }
        //set button and onClickListener
        btnCamera = findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //tell phone to open camera
                startActivityForResult(intent, CAMERA_REQUEST);//send intent to activity on result below
            }
        });

        linearLayout.removeView(btnCamera);
        linearLayout.addView(btnCamera);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            //bitmap object from intent data
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            //string object of file name of stored image
            createImageFromBitmap(bitmap);
            //intent for item info activity
            Intent intent = new Intent(MainActivity.this, ItemInfo.class);
            //start item info activity
            startActivityForResult(intent, ITEM_CREATION);
        }

        if (requestCode == ITEM_CREATION && resultCode == RESULT_OK) {
            Log.d(LOG, String.valueOf(listOfItems.size()));
            Log.d(LOG, listOfItems.get(0).expirationDate);
            Log.d(LOG, listOfItems.get(0).purchaseDate);
            finish();
            startActivity(getIntent());
        }
    }

    //takes a bitmap and stores it in phone compressed
    //apparently not a good or efficient idea to just pass a bitmap object through intents
    public String createImageFromBitmap(Bitmap bitmap) {
        String fileName = "myImage";//no .png or .jpg needed

        //try catch because working with file
        try {
            //compress bitmap into byte array and store in phone
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            // remember to close file output
            fo.close();
            bitmap.recycle();//setup bitmap for garbage collector
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;//file name of bitmap
    }

    private String getExpirationDays(String expirationDate) {
        Date date = null;

        try {
            date = sdf.parse(expirationDate);
        } catch (ParseException exception) {
            exception.printStackTrace();
        }

        Calendar purchase = Calendar.getInstance();
        purchase.setTime(date);

        Calendar today = Calendar.getInstance();

        long expirationMillis =  purchase.getTimeInMillis() - today.getTimeInMillis();

        long expirationDays = expirationMillis / (24 * 60 * 60 * 1000);

        return String.valueOf(expirationDays);
    }

    private void colorizeDays(String str, TextView textView) {
        int temp = Integer.parseInt(str);

        if (temp > 5) {
            textView.setTextColor(getColor(R.color.green));
        } else if (temp >=2) {
            textView.setTextColor(getColor(R.color.yellow));
        } else if (temp >= 0) {
            textView.setTextColor(getColor(R.color.orange));
        } else {
            textView.setTextColor(getColor(R.color.red));
        }
    }


}
