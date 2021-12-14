package com.example.tvshow.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.tvshow.database.TVShowDatabase;
import com.example.tvshow.models.TVShow;
import com.example.tvshow.reposetories.TvShowDetailsRepository;
import com.example.tvshow.responses.TVShowDetailsResponse;

import io.reactivex.Completable;


public class TVShowDetailsViewModel extends AndroidViewModel {
    private TvShowDetailsRepository tvShowDetailsRepository;
    private TVShowDatabase tvShowDatabase;

     public TVShowDetailsViewModel(@NonNull Application application)
     {
         super(application);
         tvShowDetailsRepository =new TvShowDetailsRepository();
         tvShowDatabase = TVShowDatabase.getTvShowDatabase(application);

     }
     public LiveData<TVShowDetailsResponse> getTVShowDetails (String tvShowId){
         return tvShowDetailsRepository.getTVShowDetails(tvShowId);
     }
     public Completable addToWatchList (TVShow tvShow){
         return  tvShowDatabase.tvShowDao().addToWatchList(tvShow);
     }
}
