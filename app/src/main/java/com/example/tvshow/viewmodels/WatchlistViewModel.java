package com.example.tvshow.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;


import com.example.tvshow.database.TVShowDatabase;
import com.example.tvshow.models.TVShow;

import java.util.List;

import io.reactivex.Flowable;


public class WatchlistViewModel extends AndroidViewModel {
    private TVShowDatabase tvShowDatabase;
    public WatchlistViewModel (@NonNull Application  application){
        super(application);
        tvShowDatabase=TVShowDatabase.getTvShowDatabase(application);


    }

    public Flowable<List<TVShow>> loadWatchlist(){
        return tvShowDatabase.tvShowDao().getWatchList();
    }
}
