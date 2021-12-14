package com.example.tvshow.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tvshow.reposetories.MostPopularTvShowRepository;
import com.example.tvshow.responses.TVShowResponse;

public class MostPopularTVShowsViewModel extends ViewModel {

    public MostPopularTvShowRepository mostPopularTvShowRepository;

    public MostPopularTVShowsViewModel (){
        mostPopularTvShowRepository =new MostPopularTvShowRepository();

    }
    public LiveData<TVShowResponse> getMostPopularTVShows(int page){
        return mostPopularTvShowRepository.getMostPopularTvShows(page);

    }
}
