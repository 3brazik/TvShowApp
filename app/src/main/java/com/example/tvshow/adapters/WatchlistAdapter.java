package com.example.tvshow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvshow.R;
import com.example.tvshow.databinding.ItemContaineerTvShowBinding;
import com.example.tvshow.listeners.TVShowListeners;
import com.example.tvshow.listeners.WatchlistListener;
import com.example.tvshow.models.TVShow;

import java.util.ArrayList;
import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.TVShowViewHolder> {
    private List<TVShow> tvShows ;
    private LayoutInflater layoutInflater ;
    private WatchlistListener watchlistListener;

    public WatchlistAdapter(List<TVShow> tvShows , WatchlistListener watchlistListener) {
        this.tvShows = tvShows;
        this.watchlistListener=watchlistListener;

    }


    @NonNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContaineerTvShowBinding tvShowBinding = DataBindingUtil.inflate(layoutInflater,R.layout.item_containeer_tv_show,parent,false);
        return new TVShowViewHolder(tvShowBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull TVShowViewHolder holder, int position) {

        holder.bindTVShow(tvShows.get(position));
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    public void setList(ArrayList<TVShow> tvShows) {
        this.tvShows = tvShows;
        notifyDataSetChanged();
    }



 class TVShowViewHolder extends RecyclerView.ViewHolder{

        private ItemContaineerTvShowBinding itemContaineerTvShowBinding;

        public TVShowViewHolder(ItemContaineerTvShowBinding itemContaineerTvShowBinding){
            super(itemContaineerTvShowBinding.getRoot());
            this .itemContaineerTvShowBinding =itemContaineerTvShowBinding;

        }

        public void bindTVShow(TVShow tvShow){
            itemContaineerTvShowBinding.setTvShow(tvShow);
            itemContaineerTvShowBinding.executePendingBindings();
            itemContaineerTvShowBinding.getRoot().setOnClickListener(v -> watchlistListener.onTVShowClicked(tvShow));
            itemContaineerTvShowBinding.imageDelete.setOnClickListener(v -> watchlistListener
                    .removeTVShowFromWatchlist(tvShow,getAdapterPosition()));
            itemContaineerTvShowBinding.imageDelete.setVisibility(View.VISIBLE);

}
}
}
