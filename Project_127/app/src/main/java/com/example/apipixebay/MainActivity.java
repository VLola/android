package com.example.apipixebay;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String key = "29849864-42fe3d945007d4575d7ffed0a";
    TextView tvProgressHorizontal;
    ProgressBar pbHorizontal;
    Gallery simpleGallery;
    CustomizedGalleryAdapter customGalleryAdapter;
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    Bitmap[] arrayBitmaps = new Bitmap[0];
    ListView listView;
    ArrayAdapter<String> adapter;
    ImageView imageView;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        imageView = findViewById(R.id.imageView);

        simpleGallery = findViewById(R.id.languagesGallery);
        pbHorizontal = findViewById(R.id.pb_horizontal);
        tvProgressHorizontal = findViewById(R.id.tv_progress_horizontal);
        pbHorizontal.setVisibility(ProgressBar.INVISIBLE);

        simpleGallery.setOnItemClickListener((parent, view, position, id) -> {
            imageView.setImageBitmap(arrayBitmaps[position]);
        });
    }

    public void setProgressValue(int value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pbHorizontal.setVisibility(ProgressBar.VISIBLE);
                tvProgressHorizontal.setText(String.valueOf(value) + " %");
                pbHorizontal.setSecondaryProgress(value);
                if(value == 100){
                    pbHorizontal.setVisibility(ProgressBar.INVISIBLE);
                    tvProgressHorizontal.setText("");
                }
            }
        });
    }
    public void search(View v){
        if (editText.getText().length() > 0){
            String text = editText.getText().toString();
            System.out.println(text);


            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Get(text);
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    private void setList(final ListView list, final List<String> items){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items);
                if (listView != null) {
                    listView.setAdapter(adapter);
                }
            }
        });
    }

    private void setText(final TextView text, final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }

    private void setImage(final ImageView imageView, Bitmap bitMap){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Glide.with(getApplicationContext()).load(value).into(imageView);
                imageView.setImageBitmap(bitMap);
                bitmaps.add(bitMap);
            }
        });
    }
    private void loadAllGallery(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                arrayBitmaps = bitmaps.toArray(new Bitmap[0]);
                customGalleryAdapter = new CustomizedGalleryAdapter(getApplicationContext(), arrayBitmaps);
                simpleGallery.setAdapter(customGalleryAdapter);
                if(arrayBitmaps.length > 0){
                    imageView.setImageBitmap(arrayBitmaps[0]);
                }
            }
        });
    }
    private void loadGallery(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                customGalleryAdapter = new CustomizedGalleryAdapter(getApplicationContext(), arrayBitmaps);
                simpleGallery.setAdapter(customGalleryAdapter);
            }
        });
    }

    private void Get(String search) throws IOException, JSONException {
        String url = "https://pixabay.com/api/?key=" + key + "&q=" + search +"&image_type=photo";

        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());

        JSONObject root = new JSONObject(response.toString());

        JSONArray array = root.getJSONArray("hits");
        bitmaps.clear();
        for (int i = 0; i < array.length(); i++){
            JSONObject c = array.getJSONObject(i);

            String largeImageURL = c.getString("largeImageURL");
            URL imgUrl = new URL(largeImageURL);
            Bitmap bitMap = BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream());
            bitmaps.add(bitMap);
            arrayBitmaps = bitmaps.toArray(new Bitmap[0]);
            loadGallery();
            int length = array.length() - 1;
            if(i > 0 && i < length){
                Double progress = ((double)i / (double)length * 100);
                setProgressValue(progress.intValue());
            }
            else if(i == 0){
                setProgressValue(0);
            }
            else if(i == length){
                setProgressValue(100);
            }
        }
        loadAllGallery();

    }
}