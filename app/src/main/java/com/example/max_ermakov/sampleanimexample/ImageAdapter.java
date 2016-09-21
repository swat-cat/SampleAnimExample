package com.example.max_ermakov.sampleanimexample;

import android.content.Context;
import android.content.pm.LabeledIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Integer[] mThumbIds = new Integer[36];
    private LayoutInflater inflater;

    public ImageAdapter(Context c) {
        mContext = c;
        for (int i = 0; i<mThumbIds.length; i++){
            mThumbIds[i] = R.mipmap.placeholder;
        }
        inflater = LayoutInflater.from(c);
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(convertDpToPixsels(mContext,50),convertDpToPixsels(mContext,50)));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images

    public static int convertDpToPixsels(Context context, int widthInDp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((widthInDp * scale) + 0.5);
    }

}
