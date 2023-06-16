package com.example.coins;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Animation animation;
    Context context;
    boolean check = false;
    static ArrayList<Coin> coins;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        animation = AnimationUtils.loadAnimation(this, R.anim.animate);

        if(coins == null) {
            load();
            loadApplication();
        }
        else{
            loadCoins();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_settings);
        int result = AppCompatDelegate.getDefaultNightMode();
        if (result == AppCompatDelegate.MODE_NIGHT_YES) {
            item.setTitle("Disable Night Mode");
        }
        else if (result == AppCompatDelegate.MODE_NIGHT_NO){
            item.setTitle("Enable Night Mode");
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_settings :
                setTheme();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void load(){
        String res = loadDb();
        if(!res.equals("")) {
            changeTheme(Integer.parseInt(res));
        }
    }
    public void setTheme(){
        int result = AppCompatDelegate.getDefaultNightMode();
        if (result == AppCompatDelegate.MODE_NIGHT_YES) {
            writeDb(String.valueOf(AppCompatDelegate.MODE_NIGHT_NO));
            changeTheme(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            writeDb(String.valueOf(AppCompatDelegate.MODE_NIGHT_YES));
            changeTheme(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }
    private void changeTheme(int id){
        AppCompatDelegate.setDefaultNightMode(id);
    }

    private String loadDb(){
        String tableName = "myTable";
        String columnName1 = "mode";
        // db
        SQLiteDatabase db = openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        // create table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (" + columnName1 + " TEXT)");
        // read table
        Cursor query = db.rawQuery("SELECT * FROM " + tableName + ";", null);
        // write result

        String mode = "";
        while(query.moveToNext()){
            mode = query.getString(0);
        }

        // close connection
        query.close();
        db.close();
        return mode;
    }

    public void writeDb(String value1){

        String tableName = "myTable";
        String columnName1 = "mode";
        // value
        ContentValues values = new ContentValues();
        values.put(columnName1, value1);
        // db
        SQLiteDatabase db = getApplicationContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        // create table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (" + columnName1 + " TEXT)");
        // insert value
        db.insert(tableName, null, values);

        db.close();
    }

    private ArrayList<Coin> parseHtml() throws IOException {
        ArrayList<Coin> coins = new ArrayList<>();
        Document doc = Jsoup.connect("https://privatbank.ua/premium-banking/coins").get();

        Element content = doc.getElementsByClass("coins-container").get(0);
        Elements items = content.getElementsByClass("lazy-coin");
        for (Element item : items) {
            String name = item.getElementsByClass("bold-text").text();
            String price = item.getElementsByClass("coin-name").text();
            Coin coin = new Coin();
            coin.name = name;
            coin.price = price;
            coins.add(coin);
        }

        int i = 0;
        Elements links = content.getElementsByClass("images-coins");
        for (Element link : links) {
            Elements images = link.getElementsByTag("img");
            String front = images.get(0).attr("data-src");
            String back = images.get(1).attr("data-src");
            coins.get(i).urlFrontImage = front;
            coins.get(i).urlBackImage = back;

            i++;
        }
        return coins;
    }
    private void loadCoins(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    int color = getResources().getColor(R.color.black, null);
                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) color = getResources().getColor(R.color.white, null);
                    CoinAdapter adapter = new CoinAdapter( getBaseContext().getApplicationContext(), coins, animation, color);
                    ListView listView = (ListView) findViewById(R.id.listView);
                    listView.setAdapter(adapter);

                }catch (Exception ex){
                    System.out.println(ex.toString());
                }

            }
        });
    }

    private void loadApplication(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    coins = parseHtml();
                    for (Coin coin: coins) {
                        coin.frontBitmap = getBitMap(coin.urlFrontImage);
                        coin.backBitmap = getBitMap(coin.urlBackImage);
                    }
                    loadCoins();

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private Bitmap getBitMap(String url) throws IOException {
        URL imgUrl = new URL(url);
        return BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream());
    }

    private void setAnimate(String url_1, String url_2) throws IOException {

        Bitmap bitMap1 = getBitMap(url_1);
        Bitmap bitMap2 = getBitMap(url_2);

        // create background 1
        AnimationDrawable animationDrawable_1 = new AnimationDrawable();
        animationDrawable_1.setOneShot(true);
        animationDrawable_1.addFrame(new BitmapDrawable(bitMap1), 1000);
        animationDrawable_1.addFrame(new BitmapDrawable(bitMap2), 1000);

        // create background 2
        AnimationDrawable animationDrawable_2 = new AnimationDrawable();
        animationDrawable_2.setOneShot(true);
        animationDrawable_2.addFrame(new BitmapDrawable(bitMap2), 1000);
        animationDrawable_2.addFrame(new BitmapDrawable(bitMap1), 1000);

        setImage(animationDrawable_1, animationDrawable_2);

    }

    private void setImage(AnimationDrawable animationDrawable_1, AnimationDrawable animationDrawable_2){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    // set background 1
                    imageView.setBackground(animationDrawable_1);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!check){
                                check = true;
                                imageView.setBackground(animationDrawable_1);
                                animationDrawable_1.start();
                                imageView.startAnimation(animation);
                            }
                            else{
                                check = false;
                                imageView.setBackground(animationDrawable_2);
                                animationDrawable_2.start();
                                imageView.startAnimation(animation);
                            }
                        }
                    });
                }catch (Exception ex){
                    System.out.println(ex.toString());
                }

            }
        });
    }



}