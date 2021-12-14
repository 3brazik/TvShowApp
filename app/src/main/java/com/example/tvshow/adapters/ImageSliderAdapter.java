package com.example.tvshow.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvshow.R;
import com.example.tvshow.databinding.ItemContaineerSliderImageBinding;

public class ImageSliderAdapter extends  RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>{
    private String[] sliderImage;
    private LayoutInflater layoutInflater;


    public ImageSliderAdapter(String[] sliderImage) {
        this.sliderImage = sliderImage;
    }

    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
            ItemContaineerSliderImageBinding sliderImageBinding =DataBindingUtil.inflate(layoutInflater,
                    R.layout.item_containeer_slider_image,parent,false);

        return new ImageSliderViewHolder(sliderImageBinding);
    }
    @Override
    public void onBindViewHolder(@NonNull ImageSliderViewHolder holder, int position) {

        holder.bindSliderImage(sliderImage[position]);
    }

    @Override
    public int getItemCount() {
        return sliderImage.length;
    }

    static class ImageSliderViewHolder extends RecyclerView.ViewHolder

    {
        private ItemContaineerSliderImageBinding itemContaineerSliderImageBinding;
         public ImageSliderViewHolder (ItemContaineerSliderImageBinding itemContaineerSliderImageBinding){
             super(itemContaineerSliderImageBinding.getRoot());
             this.itemContaineerSliderImageBinding= itemContaineerSliderImageBinding;
         }
         public void bindSliderImage(String imageURL){
             itemContaineerSliderImageBinding.setImageURL(imageURL);

         }

    }
}