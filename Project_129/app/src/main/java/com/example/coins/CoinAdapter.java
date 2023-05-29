package com.example.coins;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class CoinAdapter extends ArrayAdapter<Coin> {
    Animation animation;
    public CoinAdapter(Context context, ArrayList<Coin> coins, Animation animation) {
        super(context, 0, coins);
        this.animation = animation;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Coin coin = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_coin, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
//        Glide.with(getContext()).load(coin.urlFrontImage).into(imageView);
        // Populate the data into the template view using the data object
        name.setText(coin.name);
        price.setText(coin.price);
        try {
            setAnimate(imageView, coin);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Return the completed view to render on screen
        return convertView;
    }

    private void setAnimate(ImageView imageView, Coin coin) throws IOException {


        // create background 1
        AnimationDrawable animationDrawable_1 = new AnimationDrawable();
        animationDrawable_1.setOneShot(true);
        animationDrawable_1.addFrame(new BitmapDrawable(coin.frontBitmap), 1000);
        animationDrawable_1.addFrame(new BitmapDrawable(coin.backBitmap), 1000);

        // create background 2
        AnimationDrawable animationDrawable_2 = new AnimationDrawable();
        animationDrawable_2.setOneShot(true);
        animationDrawable_2.addFrame(new BitmapDrawable(coin.backBitmap), 1000);
        animationDrawable_2.addFrame(new BitmapDrawable(coin.frontBitmap), 1000);
        imageView.setBackground(animationDrawable_1);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!coin.is_back){
                    coin.is_back = true;
                    imageView.setBackground(animationDrawable_1);
                    animationDrawable_1.start();
                    imageView.startAnimation(animation);
                }
                else{
                    coin.is_back = false;
                    imageView.setBackground(animationDrawable_2);
                    animationDrawable_2.start();
                    imageView.startAnimation(animation);
                }
            }
        });
    }
}
