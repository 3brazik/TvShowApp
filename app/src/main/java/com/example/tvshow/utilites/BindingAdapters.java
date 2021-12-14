package com.example.tvshow.utilites;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class BindingAdapters {

    @androidx.databinding.BindingAdapter("android:imageURL")
    public static void setImageURL (ImageView imageView ,String URl){

        try {
            imageView.setAlpha(0f);
            Picasso.get().load(URl).noFade().into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    imageView.animate().setDuration(300).alpha(1f).start();
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }catch (Exception ignored) {

        }
    }

}
