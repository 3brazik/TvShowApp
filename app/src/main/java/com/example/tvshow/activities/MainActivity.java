package com.example.tvshow.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tvshow.R;
import com.example.tvshow.adapters.TVShowAdapter;
import com.example.tvshow.databinding.ActivityMainBinding;
import com.example.tvshow.listeners.TVShowListeners;
import com.example.tvshow.models.TVShow;
import com.example.tvshow.viewmodels.MostPopularTVShowsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TVShowListeners {

    private MostPopularTVShowsViewModel viewModel;
    private ActivityMainBinding binding;
    private List<TVShow> tvShows = new ArrayList<>();
    private TVShowAdapter adapter;
    private int currentPage = 1;
    private int totalAvailablePages=1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        doInitialization();

    }

    private void doInitialization() {
        binding.tvShowRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);
         adapter = new TVShowAdapter(tvShows ,this);
        binding.tvShowRecyclerView.setAdapter(adapter);
        binding.tvShowRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if ( !binding.tvShowRecyclerView.canScrollVertically(1)){
                    if (currentPage<= totalAvailablePages ){
                        currentPage +=1 ;
                        getMostPopularTVShows();
                    }
                }
            }
        });
        binding.imageWatchList.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),WatchlistActivity.class)));
          getMostPopularTVShows();

    }

    private void getMostPopularTVShows() {
        toggleLoading();
        viewModel.getMostPopularTVShows(currentPage).observe(this, mostPopularTVshowsResponse -> {
            toggleLoading();

            if (mostPopularTVshowsResponse != null) {
                totalAvailablePages =mostPopularTVshowsResponse.getPages();
                if (mostPopularTVshowsResponse.getTv_shows() != null) {
                    int oldCount = tvShows.size();
                    tvShows.addAll(mostPopularTVshowsResponse.getTv_shows());
                    adapter.notifyItemRangeInserted(oldCount , tvShows.size());
                }
            }
        });


    }

    private void toggleLoading() {
        if (currentPage == 1) {
            if (binding.getIsLoading() != null && binding.getIsLoading()) {
                binding.setIsLoading(false);
            } else {
                binding.setIsLoading(true);
            }
        } else {
            if (binding.getIsLoadingMore() != null && binding.getIsLoadingMore()) {
                binding.setIsLoadingMore(false);

            } else {
                binding.setIsLoadingMore(true);
            }
        }
    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent =new Intent(getApplicationContext(),TVShowDetailsActivity.class);
       intent.putExtra("tvShow" ,tvShow);
        startActivity(intent);

    }
}