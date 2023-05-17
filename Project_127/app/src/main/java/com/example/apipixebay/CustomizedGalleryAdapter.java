package com.example.apipixebay;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class CustomizedGalleryAdapter extends BaseAdapter {

    private final Context context;
    private final Bitmap[] bitmaps;

    public CustomizedGalleryAdapter(Context c, Bitmap[] bitmaps) {
        context = c;
        this.bitmaps = bitmaps;
    }

    // returns the number of images, in our example it is 10
    public int getCount() {
        return bitmaps.length;
    }

    // returns the Item  of an item, i.e. for our example we can get the image
    public Object getItem(int position) {
        return position;
    }

    // returns the ID of an item
    public long getItemId(int position) {
        return position;
    }

    // returns an ImageView view
    public View getView(int position, View convertView, ViewGroup parent) {
        // position argument will indicate the location of image
        // create a ImageView programmatically
        ImageView imageView = new ImageView(context);

        // set image in ImageView
//        imageView.setImageResource(bitmaps[position]);
        imageView.setImageBitmap(bitmaps[position]);

        // set ImageView param
        imageView.setLayoutParams(new Gallery.LayoutParams(200, 200));
        return imageView;
    }
}